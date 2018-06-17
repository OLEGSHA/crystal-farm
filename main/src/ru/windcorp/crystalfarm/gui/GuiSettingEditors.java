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

import java.util.List;

import ru.windcorp.crystalfarm.cfg.Setting;

public class GuiSettingEditors {
	
//	public static Component createEditor(ConfigurationNode setting) {
//		if (setting instanceof Setting) {
//			
//		}
//	}
	
	public static <T> Component createLimitedChoiceEditor(Setting<T> setting, List<T> choices) {
		ChoiceButton<T> button = new ChoiceButton<T>(
				setting.getName() + ".editor",
				null,
				choices.indexOf(setting.get()),
				choices);
		
		button.addAction(x -> setting.set(button.getSelection()));
		setting.addListener(x -> button.selectSilently(choices.indexOf(setting.get())));
		
		return button;
	}

}
