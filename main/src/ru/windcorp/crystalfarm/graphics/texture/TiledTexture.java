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

public class TiledTexture extends SimpleTexture implements AbstractTiledTexture, Cloneable {
	
	private int tileX = 0;
	private int tileY = 0;
	private final int tileSize;

	protected TiledTexture(TexturePrimitive texture, int tileSize) {
		super(texture);
		this.tileSize = tileSize;
	}

	public static TiledTexture get(String name, int tileSize) {
		synchronized (TextureManager.class) {
			TiledTexture texture = TextureManager.get(name, TiledTexture.class);
			if (texture == null) {
				texture = new TiledTexture(TextureManager.loadTexture(name), tileSize);
				TextureManager.register(texture);
			}
			return texture;
		}
	}

	@Override
	public int getTileSize() {
		return tileSize;
	}

	@Override
	public int getTileX() {
		return tileX;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	@Override
	public int getTileY() {
		return tileY;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}
	
	public TiledTexture clone() {
		try {
			return (TiledTexture) super.clone();
		} catch (CloneNotSupportedException e) {
			// Never happens
			return null;
		}
	}

}
