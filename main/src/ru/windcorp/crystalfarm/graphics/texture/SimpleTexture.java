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

import ru.windcorp.tge2.util.Nameable;

public class SimpleTexture extends Nameable implements Texture {
	
	private final TexturePrimitive texture;

	protected SimpleTexture(TexturePrimitive texture) {
		super(texture.getName());
		this.texture = texture;
	}
	
	public static SimpleTexture get(String name) {
		synchronized (TextureManager.class) {
			SimpleTexture texture = TextureManager.get(name, SimpleTexture.class);
			if (texture == null) {
				texture = new SimpleTexture(TextureManager.loadTexture(name, true));
				TextureManager.register(texture);
			}
			return texture;
		}
	}

	@Override
	public int getTextureId() {
		return texture.getTextureId();
	}

	@Override
	public int getUsableWidth() {
		return texture.getWidth();
	}

	@Override
	public int getUsableHeight() {
		return texture.getHeight();
	}
	
	@Override
	public int getTextureWidth() {
		return texture.getTextureWidth();
	}
	
	@Override
	public int getTextureHeight() {
		return texture.getTextureHeight();
	}

}
