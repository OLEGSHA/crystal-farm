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

import ru.windcorp.crystalfarm.graphics.GraphicsDesign;
import ru.windcorp.crystalfarm.gui.GuiSettingEditors;
import ru.windcorp.crystalfarm.gui.Table;

public class SettingsDesignMenu extends MenuLayer {
	public SettingsDesignMenu() {
		super("SettingsDesignMenu", true);
		Table table = new Table(getName() + ".table", 3);
		
		table.addRow("menu.SettingsDesignMenu.foreground",
				GuiSettingEditors.createEditor(GraphicsDesign.SETTING_FOREGROUND_COLOR),
				GuiSettingEditors.createResetter(GraphicsDesign.SETTING_FOREGROUND_COLOR));
		table.addRow("menu.SettingsDesignMenu.foregroundalt",
				GuiSettingEditors.createEditor(GraphicsDesign.SETTING_FOREGROUND_ALT_COLOR),
				GuiSettingEditors.createResetter(GraphicsDesign.SETTING_FOREGROUND_ALT_COLOR));
		table.addRow("menu.SettingsDesignMenu.background",
				GuiSettingEditors.createEditor(GraphicsDesign.SETTING_BACKGROUND_COLOR),
				GuiSettingEditors.createResetter(GraphicsDesign.SETTING_BACKGROUND_COLOR));
		table.addRow("menu.SettingsDesignMenu.backgroundalt",
				GuiSettingEditors.createEditor(GraphicsDesign.SETTING_BACKGROUND_ALT_COLOR),
				GuiSettingEditors.createResetter(GraphicsDesign.SETTING_BACKGROUND_ALT_COLOR));
		table.addRow("menu.SettingsDesignMenu.border",
				GuiSettingEditors.createEditor(GraphicsDesign.SETTING_BORDER_COLOR),
				GuiSettingEditors.createResetter(GraphicsDesign.SETTING_BORDER_COLOR));
		table.addRow("menu.SettingsDesignMenu.borderfocused",
				GuiSettingEditors.createEditor(GraphicsDesign.SETTING_BORDER_FOCUSED_COLOR),
				GuiSettingEditors.createResetter(GraphicsDesign.SETTING_BORDER_FOCUSED_COLOR));
		table.addRow("menu.SettingsDesignMenu.borderhovered",
				GuiSettingEditors.createEditor(GraphicsDesign.SETTING_BORDER_HOVERED_COLOR),
				GuiSettingEditors.createResetter(GraphicsDesign.SETTING_BORDER_HOVERED_COLOR));
		table.addRow("menu.SettingsDesignMenu.cover",
				GuiSettingEditors.createEditor(GraphicsDesign.SETTING_COVER_COLOR),
				GuiSettingEditors.createResetter(GraphicsDesign.SETTING_COVER_COLOR));
		table.addRow("menu.SettingsDesignMenu.fontcolor",
				GuiSettingEditors.createEditor(GraphicsDesign.SETTING_FONT_COLOR),
				GuiSettingEditors.createResetter(GraphicsDesign.SETTING_FONT_COLOR));
		table.addRow("menu.SettingsDesignMenu.fontaltcolor",
				GuiSettingEditors.createEditor(GraphicsDesign.SETTING_FONT_ALT_COLOR),
				GuiSettingEditors.createResetter(GraphicsDesign.SETTING_FONT_ALT_COLOR));
		table.addRow("menu.SettingsDesignMenu.line",
				GuiSettingEditors.createEditor(GraphicsDesign.SETTING_LINE),
				GuiSettingEditors.createResetter(GraphicsDesign.SETTING_LINE));
		add(table);
	}
}
