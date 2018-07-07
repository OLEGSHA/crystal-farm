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
package ru.windcorp.crystalfarm.gui.menu;

import ru.windcorp.crystalfarm.client.Proxy;
import ru.windcorp.crystalfarm.content.basic.UnpauseAction;
import ru.windcorp.crystalfarm.gui.Button;
import ru.windcorp.crystalfarm.logic.GameManager;
import ru.windcorp.crystalfarm.translation.TString;

public class PauseMenu extends MenuLayer {

	public PauseMenu() {
		super("PauseMenu", false);
		
		add(new Button(
				"PauseMenu.toMainMenu",
				TString.translated("menu.MainMenu.title").toFont(),
				button -> GameManager.shutdownClient()
				));
		add(new Button(
				"PauseMenu.settings",
				TString.translated("menu.SettingsMenu.title").toFont(),
				button -> new SettingsMenu().show(false)
				));
	}
	
	@Override
	public void postClose() {
		Proxy proxy = GameManager.getLocalClient();
		if (proxy != null) proxy.sendAction(new UnpauseAction(), null);
	}

}
