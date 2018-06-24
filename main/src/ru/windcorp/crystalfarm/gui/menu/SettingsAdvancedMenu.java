package ru.windcorp.crystalfarm.gui.menu;


import ru.windcorp.crystalfarm.graphics.ModuleGraphicsInterface;

import ru.windcorp.crystalfarm.gui.*;

public class SettingsAdvancedMenu extends MenuLayer {

	public SettingsAdvancedMenu() {
		super("AdvancedSettingsMenu", true);
		
		Table table = new Table(getName() + ".table", 3);
		
		table.addRow("menu.AdvancedSettingsMenu.showfps",
			GuiSettingEditors.createEditor(ModuleGraphicsInterface.SHOW_FPS),
			GuiSettingEditors.createResetter(ModuleGraphicsInterface.SHOW_FPS));
		table.addRow("menu.AdvancedSettingsMenu.vsync",
			GuiSettingEditors.createEditor(ModuleGraphicsInterface.VSYNC),
			GuiSettingEditors.createResetter(ModuleGraphicsInterface.VSYNC));
		add(table);
	}
	
}
