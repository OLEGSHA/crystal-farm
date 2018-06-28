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

import ru.windcorp.crystalfarm.graphics.InputListener;
import ru.windcorp.crystalfarm.graphics.Layer;
import ru.windcorp.crystalfarm.input.Input;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.crystalfarm.logic.Island;
import ru.windcorp.crystalfarm.logic.action.Action;
import ru.windcorp.crystalfarm.logic.action.ActionRegistry;

public class GameLayer extends Layer implements InputListener {

	private final ClientAgent agent;
	
	public GameLayer(ClientAgent agent) {
		super("Game");
		this.agent = agent;
	}

	@Override
	public void render() {
		getView().pushMatrix();
		getIsland().render(getView());
		getView().popMatrix();
	}
	
	public ClientAgent getAgent() {
		return agent;
	}

	public Island getIsland() {
		return getAgent().getIsland();
	}
	
	public View getView() {
		return getAgent().getView();
	}

	@Override
	public void onInput(Input input) {
		if (input instanceof KeyInput) {
//			KeyInput ki = (KeyInput) input;
//			
//			if (ki.isPressed()) {
//				switch (ki.getKey()) {
//				case GLFW.GLFW_KEY_UP:
//					getView().move(  0, -40); break;
//				case GLFW.GLFW_KEY_DOWN:
//					getView().move(  0, +40); break;
//				case GLFW.GLFW_KEY_LEFT:
//					getView().move(-40,   0); break;
//				case GLFW.GLFW_KEY_RIGHT:
//					getView().move(+40,   0); break;
//				case GLFW.GLFW_KEY_LEFT_BRACKET:
//					getView().zoom(  1.25); break;
//				case GLFW.GLFW_KEY_RIGHT_BRACKET:
//					getView().zoom(1/1.25); break;
//				}
//			}
			
			Action action = ActionRegistry.IN_GAME.getAction((KeyInput) input);
			
			if (action != null) {
				if (action.isLocal()) {
					action.run(getAgent());
				} else {
					getAgent().sendAction(action);
				}
			}
		}
		
		input.consume();
	}

}
