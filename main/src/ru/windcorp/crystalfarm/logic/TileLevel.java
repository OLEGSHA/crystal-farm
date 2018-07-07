/**
 * Crystal Farm the game
 * Copyright (C) 2018  Crystal Farm Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ru.windcorp.crystalfarm.logic;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import ru.windcorp.crystalfarm.logic.server.World;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.stream.CountingDataInput;
import ru.windcorp.tge2.util.stream.CountingDataOutput;

public abstract class TileLevel<T extends Tile> extends Level {
	
	private final TileRegistry<T> tileRegistry = new TileRegistry<>(getName());
	private final Class<T> clazz;
	
	private final Collection<Tile> tickingTiles = new CopyOnWriteArrayList<>();
	private final Collection<Collideable> collideables = new CopyOnWriteArrayList<>();

	public TileLevel(Mod mod, String name, Class<T> clazz) {
		super(mod, name);
		this.clazz = clazz;
	}
	
	public TileRegistry<T> getTileRegistry() {
		return tileRegistry;
	}
	
	@Override
	public void read(CountingDataInput input) throws IOException, SyntaxException {
		int tileMapSize = input.readInt();
		if (tileMapSize <= 0) {
			throw new SyntaxException("Tile map size is non-positive (0x" + Integer.toHexString(tileMapSize) + ")");
		}
		
		@SuppressWarnings("unchecked")
		T[] tileMap = (T[]) Array.newInstance(clazz, tileMapSize);
		
		for (int i = 0; i < tileMapSize; ++i) {
			
			String id = input.readUTF();
			tileMap[i] = getTileRegistry().get(id);
			
			if (tileMap[i] == null) {
				ExecutionReport.reportWarning(null, null, "Tile with ID %s has not been registered in %s",
						id, getTileRegistry().toString());
				tileMap[i] = getTileRegistry().getFallbackTile();
				
				if (tileMap[i] == null) {
					throw new SyntaxException("Tile with ID " + id + " has not been registered in "
							+ getTileRegistry() + " and no fallback tile specified");
				}
			}
			
		}
		
		readTiles(input, tileMap);
	}
	
	protected abstract void readTiles(CountingDataInput input, T[] tileMap) throws IOException, SyntaxException;
	
	@Override
	public void write(CountingDataOutput output) throws IOException {
		String[] tileMap = getTileRegistry().giveNIDs();
		
		output.writeInt(tileMap.length);
		for (String name : tileMap) {
			output.writeUTF(name);
		}
		
		writeTiles(output);
	}
	
	protected abstract void writeTiles(CountingDataOutput output) throws IOException;
	
	protected T readTile(CountingDataInput input, T[] tileMap) throws IOException, SyntaxException {
		int nid = input.readInt();
		
		if (nid < 0) {
			throw new SyntaxException("NID is negative (0x" + Integer.toHexString(nid) + ")");
		} else if (nid >= tileMap.length) {
			throw new SyntaxException("No tile corresponds to NID 0x" + Integer.toHexString(nid) + " (NID out of range)");
		}
		
		@SuppressWarnings("unchecked")
		T tile = (T) tileMap[nid].clone();
		
		input.pushCounter();
		tile.readAll(input);
		int read = (int) input.popCounter();
		int length = input.readInt();
		if (length < 0) {
			throw new SyntaxException("Tile length is negative (0x" + Integer.toHexString(length) + ")");
		}
		
		if (read != length) {
			throw new SyntaxException("Tile data corrupted: expected length 0x" + Integer.toHexString(length) + ", got length 0x" + Integer.toHexString(read));
		}
		
		return tile;
	}
	
	protected void writeTile(CountingDataOutput output, T tile) throws IOException {
		output.writeInt(tile.getNid());
		output.pushCounter();
		tile.writeAll(output);
		output.writeInt((int) output.popCounter());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected TileLevel<T> clone() {
		return (TileLevel<T>) super.clone();
	}
	
	@Override
	public void tick(World world, Island island, long length, long time) {
		Iterator<Tile> it = getTickableTiles().iterator();
		Tile tile;
		
		while (it.hasNext()) {
			tile = it.next();
			
			if (tile.getLevel() != this) {
				it.remove();
				continue;
			}
			
			tile.tick(world, island, this, length, time);
		}
	}
	
	public Collection<Tile> getTickableTiles() {
		return tickingTiles;
	}
	
	public Collection<Collideable> getCollideables() {
		return collideables;
	}

	@Override
	public boolean checkCollisions(Collideable col) {
		for (Collideable c : getCollideables()) {
			if (c.collides(col)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void pushOutside(DynamicCollideable col) {
		for (Collideable c : getCollideables()) {
			c.pushOutside(col);
		}
	}
	
	protected static void failRender(Exception e, Tile tile) {
		GameManager.failToMainMenu(e, "client.renderTile",
				"Could not render tile %s due to a runtime exception: %s",
				tile.toString(),
				e.toString());
	}
	
}
