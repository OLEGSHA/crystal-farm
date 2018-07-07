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

import static ru.windcorp.crystalfarm.logic.GameManager.TEXTURE_SIZE;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import ru.windcorp.crystalfarm.client.View;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.tge2.util.NumberUtil;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.stream.CountingDataInput;
import ru.windcorp.tge2.util.stream.CountingDataOutput;

public class GridTileLevel<T extends GridTile> extends TileLevel<T> {
	
	private final int size;
	
	private final T[][] tilesByCoordinate;
	private final Collection<T> tiles = new CopyOnWriteArrayList<>();

	@SuppressWarnings("unchecked")
	public GridTileLevel(Mod mod, String name, Class<T> clazz, int size) {
		super(mod, name, clazz);
		
		this.size = size;
		this.tilesByCoordinate = (T[][]) Array.newInstance(clazz, size, size);
		for (int x = 0; x < size; ++x) {
			this.tilesByCoordinate[x] = (T[]) Array.newInstance(clazz, size);
		}
	}

	public int getSize() {
		return size;
	}

	private T[][] getTilesByCoordinate() {
		return tilesByCoordinate;
	}
	
	public Collection<T> getTiles() {
		return tiles;
	}
	
	public T getTile(int x, int y) {
		synchronized (getTiles()) {
			return getTilesByCoordinate()[x][y];
		}
	}
	
	public void addTile(T tile, int x, int y) {
		synchronized (getTiles()) {
			Tile previous = getTile(x, y);
			if (previous != null) {
				previous.setLevel(null);
				getTiles().remove(previous);
			}
			
			getTilesByCoordinate()[x][y] = tile;
			getTiles().add(tile);
			tile.adopt(this, x, y);
		}
	}
	
	public void removeTile(T tile) {
		removeTile(tile.getX(), tile.getY());
	}
	
	public void removeTile(int x, int y) {
		synchronized (getTiles()) {
			T tile = getTile(x, y);
			
			if (tile == null) {
				return;
			}
			
			getTilesByCoordinate()[x][y] = null;
			getTiles().remove(tile);
			tile.setLevel(null);
		}
	}
	
	public void clearTiles() {
		getTiles().forEach(tile -> removeTile(tile));
	}

	@Override
	public void render(View view) {
		synchronized (getTiles()) {
			for (T tile : getTiles()) {
				
				int x = tile.getX();
				int y = tile.getY();
				
				if (
						(x + tile.getRenderSize())	* TEXTURE_SIZE < view.getMinX() ||
						 x							* TEXTURE_SIZE > view.getMaxX() ||
						(y + tile.getRenderSize())	* TEXTURE_SIZE < view.getMinY() ||
						 y							* TEXTURE_SIZE > view.getMaxY()
						) {
					continue;
				}
				
				try {
					tile.render(view, tile.getX()*TEXTURE_SIZE, tile.getY()*TEXTURE_SIZE);
				} catch (Exception e) {
					failRender(e, tile);
					break;
				}
			}
		}
	}

	@Override
	protected void readTiles(CountingDataInput input, T[] tileMap) throws IOException, SyntaxException {
		synchronized (getTiles()) {
			clearTiles();
			
			int x, y;
			while ((x = input.readInt()) >= 0) {
				y = input.readInt();
				if (y < 0) {
					throw new SyntaxException("Tile with x = " + new String(NumberUtil.toFullHex(x)) + " has negative y = " + new String(NumberUtil.toFullHex(y)));
				}
				
				addTile(readTile(input, tileMap), x, y);
			}
		}
	}

	@Override
	protected void writeTiles(CountingDataOutput output) throws IOException {
		synchronized (getTiles()) {
			for (T tile : getTiles()) {
				output.writeInt(tile.getX());
				output.writeInt(tile.getY());
				writeTile(output, tile);
			}
		}
		
		output.writeInt(-1);
	}

	@Override
	public void readUpdate(DataInput input) throws IOException, SyntaxException {
		synchronized (getTiles()) {
			// TODO: handle updates for GL (including tile removal)
			throw new Error("Not implemented");
		}
	}

	@Override
	public void writeUpdate(DataOutput output) throws IOException {
		synchronized (getTiles()) {
			throw new Error("Not implemented");
		}
	}

}
