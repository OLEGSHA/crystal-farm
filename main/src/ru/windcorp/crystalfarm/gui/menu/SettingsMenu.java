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

import ru.windcorp.crystalfarm.gui.Button;
import ru.windcorp.crystalfarm.gui.GuiSettingEditors;
import ru.windcorp.crystalfarm.gui.Table;
import ru.windcorp.crystalfarm.translation.ModuleTranslation;
import ru.windcorp.crystalfarm.translation.TString;

public class SettingsMenu extends MenuLayer {
	
	public SettingsMenu() {
		super("SettingsMenu", true);
		Table table = new Table(getName() + ".table", 3);
		
		table.addRow(
				"menu.SettingsMenu.language",
				GuiSettingEditors.createLimitedChoiceEditor(ModuleTranslation.LANGUAGE, ModuleTranslation.getAvailableLanguages()));
		add(new Button(
				"SettingsMenu.sounds",
				TString.translated("menu.SettingsSoundsMenu.title").toFont(),
				button -> new SettingsSoundsMenu().show()
				));
		add(new Button(
				"SettingsMenu.graphics",
				TString.translated("menu.SettingsGraphicsMenu.title").toFont(),
				button -> new SettingsGraphicsMenu().show()
				));
		add(new Button(
				"SettingsMenu.Advanced",
				TString.translated("menu.SettingsAdvancedMenu.title").toFont(),
				button -> new SettingsAdvancedMenu().show()
				));
		add(table);
	}
}
