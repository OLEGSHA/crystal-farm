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

import ru.windcorp.tge2.util.debug.Debug;

public class KeyInput extends ButtonInput {
	
	public static final int PRESSED = GLFW.GLFW_PRESS;
	public static final int RELEASED = GLFW.GLFW_RELEASE;
	public static final int REPEATED = GLFW.GLFW_REPEAT;
	
	private final int key;
	
	public KeyInput(int key, int action, int mods) {
		super(action, mods);
		this.key = key;
	}
	
	public InputTarget getTarget() {
		return InputTarget.FOCUSED;
	}

	public int getKey() {
		return key;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Input[Key, ");
		
		switch (getAction()) {
		case PRESSED:  sb.append("pressed "); break;
		case RELEASED: sb.append("released "); break;
		case REPEATED: sb.append("repeated "); break;
		default: sb.append("? "); break;
		}
		
		sb.append(getNameForKey(getKey()));
		
		if (getMods() != 0) sb.append(' ');
		if (hasCtrl()) sb.append('C');
		if (hasShift()) sb.append('S');
		if (hasAlt()) sb.append('A');
		
		sb.append(']');
		return sb.toString();
	}
	
	public static String getNameForKey(int key) {
		return Debug.getConstantName(GLFW.class, "GLFW_KEY_", Integer.TYPE, key);
	}

}