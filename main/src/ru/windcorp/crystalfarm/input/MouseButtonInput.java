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

public class MouseButtonInput extends ButtonInput {
	
	public static final int BUTTON_LEFT = GLFW.GLFW_MOUSE_BUTTON_LEFT;
	public static final int BUTTON_RIGHT = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
	public static final int BUTTON_MIDDLE = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

	private final int button;
	
	public MouseButtonInput(int button, int action, int mods) {
		super(action, mods);
		this.button = button;
	}

	@Override
	public InputTarget getTarget() {
		return InputTarget.FOCUSED;
	}
	
	public int getButton() {
		return button;
	}

}
