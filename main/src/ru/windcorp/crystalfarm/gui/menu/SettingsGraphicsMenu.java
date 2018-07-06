package ru.windcorp.crystalfarm.gui.menu;

import ru.windcorp.crystalfarm.graphics.ModuleGraphicsInterface;
import ru.windcorp.crystalfarm.gui.Button;
import ru.windcorp.crystalfarm.gui.GuiSettingEditors;
import ru.windcorp.crystalfarm.gui.Table;
import ru.windcorp.crystalfarm.translation.TString;

public class SettingsGraphicsMenu extends MenuLayer {
	public SettingsGraphicsMenu() {
		super("SettingsGraphicsMenu", true);
		Table table = new Table(getName() + ".table", 3);
		
		table.addRow("menu.SettingsGraphicsMenu.fullscreen",
				GuiSettingEditors.createEditor(ModuleGraphicsInterface.WINDOW_FULLSCREEN),
				GuiSettingEditors.createResetter(ModuleGraphicsInterface.WINDOW_FULLSCREEN));
		add(table);
		add(new Button(
				"SettingsGraphicsMenu.advanced",
				TString.translated("menu.SettingsGraphicsAdvancedMenu.title").toFont(),
				button -> new SettingsGraphicsAdvancedMenu().show(false)
				));
	}

}
