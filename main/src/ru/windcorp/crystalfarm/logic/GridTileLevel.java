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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Array;

import ru.windcorp.crystalfarm.client.View;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.stream.CountingDataInput;
import ru.windcorp.tge2.util.stream.CountingDataOutput;

public class GridTileLevel<T extends GridTile> extends TileLevel<T> {
	
	private final int size;
	private final int textureSize;
	private final T[][] tiles;

	@SuppressWarnings("unchecked")
	public GridTileLevel(String name, Class<T> clazz, int size, int textureSize) {
		super(name, clazz);
		
		this.size = size;
		this.tiles = (T[][]) Array.newInstance(clazz, size, size);
		for (int x = 0; x < size; ++x) {
			this.tiles[x] = (T[]) Array.newInstance(clazz, size);
		}
		
		this.textureSize = textureSize;
	}

	public int getTextureSize() {
		return textureSize;
	}

	public int getSize() {
		return size;
	}

	public T[][] getTiles() {
		return tiles;
	}
	
	public T getTile(int x, int y) {
		synchronized (getTiles()) {
			return getTiles()[x][y];
		}
	}
	
	public void setTile(T tile, int x, int y) {
		synchronized (getTiles()) {
			Tile previous = getTile(x, y);
			if (previous != null) {
				previous.setLevel(null);
			}
			
			getTiles()[x][y] = tile;
			
			if (tile != null) {
				tile.adopt(this, x, y);
			}
		}
	}

	@Override
	public void render(View view) {
		synchronized (getTiles()) {
			for (int x = 0; x < size; ++x) {
				for (int y = 0; y < getSize(); ++y) {
					T tile = getTile(x, y);
					if (tile != null) {
						tile.render(view, x*textureSize, y*textureSize);
					}
				}
			}
		}
	}

	@Override
	protected void readTiles(CountingDataInput input, T[] tileMap) throws IOException, SyntaxException {
		synchronized (getTiles()) {
			for (int x = 0; x < getSize(); ++x) {
				for (int y = 0; y < getSize(); ++y) {
					setTile(readTile(input, tileMap), x, y);
				}
			}
		}
	}

	@Override
	protected void writeTiles(CountingDataOutput output) throws IOException {
		synchronized (getTiles()) {
			for (int x = 0; x < getSize(); ++x) {
				for (int y = 0; y < getSize(); ++y) {
					writeTile(output, getTile(x, y));
				}
			}
		}
	}

	@Override
	public void readUpdate(DataInput input) throws IOException, SyntaxException {
		synchronized (getTiles()) {
			int x, y;
			while ((x = input.readInt()) != -1) {
				y = input.readInt();
				getTile(x, y).readUpdate(input);
			}
		}
	}

	@Override
	public void writeUpdate(DataOutput output) throws IOException {
		synchronized (getTiles()) {
			for (int x = 0; x < getSize(); ++x) {
				for (int y = 0; y < getSize(); ++y) {
					T tile = getTile(x, y);
					if (tile.getChange() != 0) {
						output.writeInt(x);
						output.writeInt(y);
						tile.writeUpdate(output);
					}
				}
			}
			
			output.writeInt(-1);
		}
	}

}
