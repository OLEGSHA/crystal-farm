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
import java.util.Arrays;

import ru.windcorp.crystalfarm.client.View;
import ru.windcorp.crystalfarm.logic.exception.OutOfDynIdsException;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.stream.CountingDataInput;
import ru.windcorp.tge2.util.stream.CountingDataOutput;

public class DynamicTileLevel<T extends DynamicTile> extends TileLevel<T> {
	
	private final T[] tiles;
	private int nextDynId = 0;
	private long nextStatId = 0;

	@SuppressWarnings("unchecked")
	public DynamicTileLevel(String name, Class<T> clazz, int size) {
		super(name, clazz);
		this.tiles = (T[]) Array.newInstance(clazz, size);
	}

	public T[] getTiles() {
		return tiles;
	}
	
	public T getTileByDynId(int dynId) {
		return getTiles()[dynId];
	}
	
	public T getTileByStatId(long statId) {
		synchronized (getTiles()) {
			for (int i = 0; i < getTiles().length; ++i) {
				T tile = getTileByDynId(i);
				if (tile != null && tile.getStatId() == statId) {
					return tile;
				}
			}
		}
		
		return null;
	}
	
	public void addTile(T tile) {
		addTile(tile, nextStatId++);
	}
	
	protected void addTile(T tile, long statId) {
		synchronized (getTiles()) {
			int i = nextDynId;
			
			while (getTileByDynId(i) != null) {
				if (i == nextDynId) {
					throw new OutOfDynIdsException("Could not add " + tile + " to " + this + ": out of dynamic IDs");
				}
				
				i = (i + 1) % getTiles().length;
			}

			getTiles()[i] = tile;
			tile.adopt(this, i, statId);
			nextDynId = (i + 1) % getTiles().length;
		}
	}
	
	public void removeTile(T tile) {
		getTiles()[tile.getDynId()] = null;
		tile.setLevel(null);
	}

	@Override
	protected void readTiles(CountingDataInput input, T[] tileMap) throws IOException, SyntaxException {
		synchronized (getTiles()) {
			Arrays.fill(getTiles(), null);
			nextDynId = 0;
			nextStatId = input.readLong();
			
			long statId;
			while ((statId = input.readLong()) != Long.MAX_VALUE) {
				addTile(readTile(input, tileMap), statId);
			}
		}
	}

	@Override
	protected void writeTiles(CountingDataOutput output) throws IOException {
		synchronized (getTiles()) {
			output.writeLong(nextStatId);
			
			for (int i = 0; i < getTiles().length; ++i) {
				T tile = getTileByDynId(i);
				if (tile != null) {
					output.writeLong(tile.getStatId());
					writeTile(output, tile);
				}
			}
		}
		
		output.writeLong(Long.MAX_VALUE);
	}

	@Override
	public void render(View view) {
		synchronized (getTiles()) {
			for (int i = 0; i < getTiles().length; ++i) {
				T tile = getTileByDynId(i);
				if (tile != null) {
					tile.render(view,
							(int) ((tile.getX() - 0.5)*TEXTURE_SIZE),
							(int) ((tile.getY() - 0.5)*TEXTURE_SIZE));
				}
			}
		}
	}

	@Override
	public void readUpdate(DataInput input) throws IOException, SyntaxException {
		synchronized (getTiles()) {
			// TODO: handle updates for DTL (including tile removal)
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
