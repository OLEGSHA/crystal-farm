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
package ru.windcorp.crystalfarm.client;

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.graphics.InputListener;
import ru.windcorp.crystalfarm.graphics.Layer;
import ru.windcorp.crystalfarm.input.Input;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.crystalfarm.logic.Island;

public class GameLayer extends Layer implements InputListener {

	private Island island;
	private final View view = new View(0, 0, 1);
	
	public GameLayer() {
		super("Game");
	}

	@Override
	public void render() {
		getView().pushMatrix();
		getIsland().render(view);
		getView().popMatrix();
	}

	public Island getIsland() {
		return island;
	}

	public void setIsland(Island island) {
		this.island = island;
	}
	
	public View getView() {
		return view;
	}

	@Override
	public void onInput(Input input) {
		if (input instanceof KeyInput) {
			KeyInput ki = (KeyInput) input;
			
			if (ki.isPressed()) {
				switch (ki.getKey()) {
				case GLFW.GLFW_KEY_UP:
					getView().move(  0, -40); break;
				case GLFW.GLFW_KEY_DOWN:
					getView().move(  0, +40); break;
				case GLFW.GLFW_KEY_LEFT:
					getView().move(-40,   0); break;
				case GLFW.GLFW_KEY_RIGHT:
					getView().move(+40,   0); break;
				case GLFW.GLFW_KEY_LEFT_BRACKET:
					getView().zoom(  1.25); break;
				case GLFW.GLFW_KEY_RIGHT_BRACKET:
					getView().zoom(1/1.25); break;
				}
			}
		}
		
		input.consume();
	}

}
