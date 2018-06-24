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

import ru.windcorp.crystalfarm.audio.ModuleAudioInterface;
import ru.windcorp.crystalfarm.graphics.ModuleGraphicsInterface;
import ru.windcorp.crystalfarm.gui.Button;
import ru.windcorp.crystalfarm.gui.GuiSettingEditors;
import ru.windcorp.crystalfarm.gui.Table;
import ru.windcorp.crystalfarm.translation.TString;

public class SettingsMenu extends MenuLayer {
	
	Table table = new Table(getName() + ".table", 3);
	public SettingsMenu() {
		super("SettingsMenu", true);
		
		table.addRow("menu.SettingsMenu.volume",
				   GuiSettingEditors.createEditor(ModuleAudioInterface.GAIN),
				   GuiSettingEditors.createResetter(ModuleAudioInterface.GAIN));
		
		table.addRow("menu.SettingsMenu.fullscreen",
				GuiSettingEditors.createEditor(ModuleGraphicsInterface.WINDOW_FULLSCREEN),
				GuiSettingEditors.createResetter(ModuleGraphicsInterface.WINDOW_FULLSCREEN));
		add(table);
		add(new Button(
				"SettingsMenu.Advanced",
				TString.translated("menu.SettingsMenu.advanced").toFont(),
				button -> new SettingsAdvancedMenu().show()
				));
	}

}
