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

import ru.windcorp.crystalfarm.graphics.notifier.ModuleNotifier;
import ru.windcorp.crystalfarm.gui.GuiSettingEditors;
import ru.windcorp.crystalfarm.gui.Table;

public class SettingsNotifierMenu extends MenuLayer {
	public SettingsNotifierMenu() {
		super("SettingsNotifierMenu", true);
		Table table = new Table(getName() + ".table", 3);
		
		table.addRow("menu.SettingsNotifierMenu.volume",
				GuiSettingEditors.createEditor(ModuleNotifier.SETTING_ALERT_GAIN),
				GuiSettingEditors.createResetter(ModuleNotifier.SETTING_ALERT_GAIN));
		table.addRow("menu.SettingsNotifierMenu.shakeinterval",
				GuiSettingEditors.createEditor(ModuleNotifier.SETTING_SHAKE_INTERVAL),
				GuiSettingEditors.createResetter(ModuleNotifier.SETTING_SHAKE_INTERVAL));
		table.addRow("menu.SettingsNotifierMenu.timeout",
				GuiSettingEditors.createEditor(ModuleNotifier.SETTING_TIMEOUT),
				GuiSettingEditors.createResetter(ModuleNotifier.SETTING_TIMEOUT));
		add(table);
	}
}
