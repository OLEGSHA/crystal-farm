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
package ru.windcorp.crystalfarm.input;

import ru.windcorp.crystalfarm.graphics.GraphicsInterface;

public abstract class Input {
	
	public static enum InputTarget {
		ALL, FOCUSED, HOVERED;
	}
	
	private boolean isConsumed = false;
	
	private final int cursorX, cursorY;
	
	public Input() {
		cursorX = GraphicsInterface.getCursorX();
		cursorY = GraphicsInterface.getCursorY();
	}
	
	public void consume() {
		this.isConsumed = true;
	}

	public boolean isConsumed() {
		return isConsumed;
	}
	
	public abstract InputTarget getTarget();

	public int getCursorX() {
		return cursorX;
	}

	public int getCursorY() {
		return cursorY;
	}

}
