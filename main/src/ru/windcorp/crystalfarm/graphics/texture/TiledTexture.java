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

public interface TiledTexture extends Texture {

	int getTileSize();
	int getTileY();
	int getTileX();
	
	@Override
	default int getWidth() {
		return getTileSize();
	}
	
	@Override
	default int getHeight() {
		return getTileSize();
	}

	@Override
	default void render(int x, int y, int width, int height, Color filter, Direction direction) {
		GraphicsInterface.drawTexture(
				x,
				y,
				width,
				height,
				this,
				 getTileX()		 * getTileSize() / (double) getTextureWidth(),
				 getTileY()		 * getTileSize() / (double) getTextureHeight(),
				(getTileX() + 1) * getTileSize() / (double) getTextureWidth(),
				(getTileY() + 1) * getTileSize() / (double) getTextureHeight(),
				filter,
				direction);
	}
	
}
