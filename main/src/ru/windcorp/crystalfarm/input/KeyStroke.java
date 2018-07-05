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

public class KeyStroke {
	
	private final int key;
	private final int mods;
	private final int action;
	
	public KeyStroke(int key, int mods, int action) {
		this.key = key;
		this.mods = mods;
		this.action = action;
	}

	public int getKey() {
		return key;
	}

	public int getMods() {
		return mods;
	}

	public int getAction() {
		return action;
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
	
	public boolean matches(KeyInput input, boolean matchAction) {
		return input.getKey() == getKey()
				&& (matchAction ? input.getAction() == getAction() : true)
				&& (getMods() == 0 ? true : (input.getMods() & getMods()) != 0);
	}
	
	@Override
	public String toString() {
		return actionToString() + " " + KeyInput.getNameForKey(getKey()) + modsToString();
	}

	private String actionToString() {
		switch (getAction()) {
		case KeyInput.PRESSED: return "PRESS";
		case KeyInput.RELEASED: return "RELEASE";
		default: return "REPEAT";
		}
	}

	private String modsToString() {
		if (getMods() == 0) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder("+");
		
		if (hasCtrl()) {
			sb.append("C");
		}
		if (hasShift()) {
			sb.append("S");
		}
		if (hasAlt()) {
			sb.append("A");
		}
		
		return sb.toString();
	}
	
	public static KeyStroke fromString(String declar) throws IllegalArgumentException {
		int action;
		
		if (declar.startsWith("PRESS ")) {
			action = GLFW.GLFW_PRESS;
			declar = declar.substring("PRESS ".length());
		} else if (declar.startsWith("RELEASE ")) {
			action = GLFW.GLFW_RELEASE;
			declar = declar.substring("RELEASE ".length());
		} else if (declar.startsWith("REPEAT ")) {
			action = GLFW.GLFW_REPEAT;
			declar = declar.substring("REPEAT ".length());
		} else {
			throw new IllegalArgumentException("\"" + declar + "\" does not contain a valid action (PRESS, RELEASE, REPEAT)");
		}
		
		int mods = 0;
		
		int index = declar.indexOf('+');
		if (index != -1) {
			if (index == 0) {
				throw new IllegalArgumentException("\"" + declar + "\" does not contain a key, only modifiers");
			}
			
			for (int i = index + 1; i < declar.length(); ++i) {
				switch (declar.charAt(i)) {
				case 'C': mods |= GLFW.GLFW_MOD_CONTROL; break;
				case 'S': mods |= GLFW.GLFW_MOD_SHIFT; break;
				case 'A': mods |= GLFW.GLFW_MOD_ALT; break;
				default: throw new IllegalArgumentException("\"" + declar + "\" has illegal modifier '" + declar.charAt(i) + "'");
				}
			}
			
			declar = declar.substring(0, index);
		}
		
		return new KeyStroke(KeyInput.getKey(declar), mods, action);
	}
	
}
