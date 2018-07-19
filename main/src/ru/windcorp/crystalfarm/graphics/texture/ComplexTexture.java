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
package ru.windcorp.crystalfarm.graphics.texture;

import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.GraphicsInterface;
import ru.windcorp.crystalfarm.logic.Units;
import ru.windcorp.crystalfarm.util.Direction;

public class ComplexTexture extends TiledTexture {
	
	private static final int[] STATIC_DATA = new int[] { 1, 1 * Units.SECONDS };
	
	private final int[] data;
	private long start = 0;

	protected ComplexTexture(TexturePrimitive texture, int tileSize, int[] data) {
		super(texture, tileSize);
		
		if (data == null || data.length == 0) {
			this.data = STATIC_DATA;
		} else if ((data.length & 1) != 0) {
			throw new IllegalArgumentException("Data for ComplexTexture " + texture.getName() + " has odd length");
		} else {
			this.data = data;
		}
	}
	
	public static ComplexTexture get(String name, int tileSize, int... data) {
		synchronized (TextureManager.class) {
			ComplexTexture texture = TextureManager.get(name, ComplexTexture.class);
			if (texture == null) {
				texture = new ComplexTexture(TextureManager.loadTexture(name, false), tileSize, data);
				TextureManager.register(texture);
			}
			return texture;
		}
	}
	
	public int getLength(int state) {
		return data[state * 2];
	}
	
	public int getLength() {
		return getLength(getState());
	}
	
	public int getDelay(int state) {
		return data[state * 2 + 1];
	}
	
	public int getDelay() {
		return getDelay(getState());
	}
	
	public int getState() {
		return getTileX();
	}
	
	public void setState(int state) {
		if (state == getState()) {
			return;
		}
		setTileX(state);
		start = (long) GraphicsInterface.time();
		setTileY(0);
	}
	
	@Override
	public void render(int x, int y, int width, int height, Color filter, Direction direction) {
		if (getLength() != 1) {
			setTileY((int) (((long) GraphicsInterface.time() - start) / getDelay()) % getLength());
		}
		super.render(x, y, width, height, filter, direction);
	}
	
	@Override
	public ComplexTexture clone() {
		return (ComplexTexture) super.clone();
	}

}
