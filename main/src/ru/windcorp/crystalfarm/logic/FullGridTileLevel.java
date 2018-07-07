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

import ru.windcorp.crystalfarm.client.View;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.stream.CountingDataInput;
import ru.windcorp.tge2.util.stream.CountingDataOutput;

public class FullGridTileLevel<T extends FullGridTile> extends TileLevel<T> {
	
	private final int size;
	private final T[][] tiles;

	@SuppressWarnings("unchecked")
	public FullGridTileLevel(Mod mod, String name, Class<T> clazz, int size) {
		super(mod, name, clazz);
		
		this.size = size;
		this.tiles = (T[][]) Array.newInstance(clazz, size, size);
		for (int x = 0; x < size; ++x) {
			this.tiles[x] = (T[]) Array.newInstance(clazz, size);
		}
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
			tile.adopt(this, x, y);
		}
	}

	@Override
	public void render(View view) {
		synchronized (getTiles()) {
			int minX = Math.max(0, view.getMinX()/TEXTURE_SIZE);
			int minY = Math.max(0, view.getMinY()/TEXTURE_SIZE);
			int maxX = Math.min(getSize(), (view.getMaxX()-1)/TEXTURE_SIZE+1);
			int maxY = Math.min(getSize(), (view.getMaxY()-1)/TEXTURE_SIZE+1);
			
			renderLoop:
			for (int x = minX; x < maxX; ++x) {
				for (int y = minY; y < maxY; ++y) {
					T tile = getTile(x, y);
					try {
						tile.render(view, x*TEXTURE_SIZE, y*TEXTURE_SIZE);
					} catch (Exception e) {
						failRender(e, tile);
						break renderLoop;
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
