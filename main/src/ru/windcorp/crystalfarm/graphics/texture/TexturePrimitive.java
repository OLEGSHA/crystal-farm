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

public class TexturePrimitive extends Nameable {
	
	private int textureId;
	private final int width, height,
			textureWidth, textureHeight;
	private final boolean isFiltered;

	public TexturePrimitive(String name, int width, int height, int textureWidth, int textureHeight, boolean isFiltered) {
		super(name);
		this.width = width;
		this.height = height;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.isFiltered = isFiltered;
	}

	public int getTextureId() {
		return textureId;
	}

	protected void setTextureId(int textureId) {
		this.textureId = textureId;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTextureWidth() {
		return textureWidth;
	}

	public int getTextureHeight() {
		return textureHeight;
	}

	public boolean isFiltered() {
		return isFiltered;
	}

}
