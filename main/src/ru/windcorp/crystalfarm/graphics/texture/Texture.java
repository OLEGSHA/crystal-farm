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
import ru.windcorp.crystalfarm.util.Direction;

public interface Texture {
	
	String getName();
	
	int getTextureId();
	
	int getUsableWidth();
	int getUsableHeight();
	int getTextureWidth();
	int getTextureHeight();
	
	default void render(int x, int y, int width, int height, Color filter, Direction direction) {
		GraphicsInterface.drawTexture(
				x, y,
				width, height,
				this,
				0, 0,
				getWidth() / (double) getTextureWidth(),
				getHeight() / (double) getTextureHeight(),
				filter, direction);
	}
	
	default int getWidth() {
		return getUsableWidth();
	}
	
	default int getHeight() {
		return getUsableHeight();
	}
	
	default void render(int x, int y, Color filter, Direction direction) {
		render(x, y, getWidth(), getHeight(), filter, direction);
	}
	
	default void render(int x, int y, int width, int height) {
		render(x, y, width, height, null, Direction.UP);
	}
	
	default void render(int x, int y) {
		render(x, y, getWidth(), getHeight(), null, Direction.UP);
	}

}
