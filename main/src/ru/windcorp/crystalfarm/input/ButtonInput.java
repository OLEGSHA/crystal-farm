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

import org.lwjgl.glfw.GLFW;

public abstract class ButtonInput extends Input {
	
	public static final int PRESSED = GLFW.GLFW_PRESS;
	public static final int RELEASED = GLFW.GLFW_RELEASE;
	public static final int REPEATED = GLFW.GLFW_REPEAT;

	private final int action;
	private final int mods;

	public ButtonInput(int action, int mods) {
		this.action = action;
		this.mods = mods;
	}
	
	public int getAction() {
		return action;
	}

	public int getMods() {
		return mods;
	}
	
	public boolean isPressed() {
		return getAction() == PRESSED;
	}
	
	public boolean isReleased() {
		return getAction() == RELEASED;
	}
	
	public boolean isRepeated() {
		return getAction() == REPEATED;
	}
	
	public boolean hasCtrl() {
		return (getMods() & GLFW.GLFW_MOD_CONTROL) != 0;
	}
	
	public boolean hasShift() {
		return (getMods() & GLFW.GLFW_MOD_SHIFT) != 0;
	}
	
	public boolean hasAlt() {
		return (getMods() & GLFW.GLFW_MOD_ALT) != 0;
	}

}
