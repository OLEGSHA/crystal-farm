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
import ru.windcorp.crystalfarm.gui.GuiSettingEditors;
import ru.windcorp.crystalfarm.gui.Table;

public class SettingsSoundsMenu extends MenuLayer {
	public SettingsSoundsMenu() {
		super("SettingsSoundsMenu", true);
		Table table = new Table(getName() + ".table", 3);
		
		table.addRow("menu.SettingsSoundsMenu.volume",
				GuiSettingEditors.createEditor(ModuleAudioInterface.GAIN),
				GuiSettingEditors.createResetter(ModuleAudioInterface.GAIN));
		table.addRow("menu.SettingsSoundsMenu.musicvolume",
				GuiSettingEditors.createEditor(ModuleAudioInterface.GAIN_MUSIC),
				GuiSettingEditors.createResetter(ModuleAudioInterface.GAIN_MUSIC));
		add(table);
	}
}
