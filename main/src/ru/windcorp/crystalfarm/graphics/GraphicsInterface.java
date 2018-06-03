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
package ru.windcorp.crystalfarm.graphics;

import static org.lwjgl.opengl.GL11.*;

public class GraphicsInterface {
	
	private static final Color CURRENT_COLOR = new Color(0, 0, 0, 0);
	
	public static void applyColor(Color color) {
		CURRENT_COLOR.copyFrom(color);
		glColor4d(color.r, color.g, color.b, color.a);
	}
	
	public static Color getColor() {
		return CURRENT_COLOR;
	}
	
	public static void fillRectangle(int x, int y, int width, int height) {
		width += x;
		height += y;
		
		glVertex2i(x, y);
		glVertex2i(width, height);
		glVertex2i(width, y);
		
		glVertex2i(x, y);
		glVertex2i(x, height);
		glVertex2i(width, height);
	}

}
