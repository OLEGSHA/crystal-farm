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
import ru.windcorp.crystalfarm.logic.action.ActionRegistry;
import ru.windcorp.crystalfarm.logic.action.KeyAction;

public class GameLayer extends Layer implements InputListener {

	private final Proxy proxy;
	
	public GameLayer(Proxy proxy) {
		super("Game");
		this.proxy = proxy;
	}

	@Override
	public void render() {
		getView().pushMatrix();
		getIsland().render(getView());
		getView().popMatrix();
	}
	
	public Proxy getProxy() {
		return proxy;
	}

	public Island getIsland() {
		return getProxy().getIsland();
	}
	
	public View getView() {
		return getProxy().getView();
	}

	@Override
	public void onInput(Input input) {
		if (input instanceof KeyInput) {
			KeyInput keyInput = (KeyInput) input;
			KeyAction action = ActionRegistry.IN_GAME.getAction(keyInput, null);
			
			if (action != null) {
				if (action.isLocal()) {
					getProxy().runLocally(action, keyInput);
				} else {
					getProxy().sendAction(action, keyInput);
				}
			}
		}
		
		input.consume();
	}

}
