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
package ru.windcorp.crystalfarm.gui;

import ru.windcorp.crystalfarm.graphics.*;
import ru.windcorp.crystalfarm.input.Input;

public class GuiLayer extends Layer implements InputListener, WindowResizeListener {
	
	private Component root;

	public GuiLayer(String name) {
		super(name);
	}
	
	public Component getRoot() {
		return root;
	}
	
	public void setRoot(Component root) {
		this.root = root;
		root.setBounds(0, 0, GraphicsInterface.getWindowSize());
		root.focusNext();
	}

	@Override
	public void render() {
		Component root = getRoot();
		if (root != null) {
			root.render();
		}
	}

	@Override
	public void onInput(Input input) {
		Component root = getRoot();
		if (root != null) {
			root.onInput(input);
		}
	}

	@Override
	public void onWindowResize(int width, int height) {
		root.setBounds(0, 0, width, height);
	}

}
