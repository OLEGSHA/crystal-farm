package ru.windcorp.crystalfarm.gui.menu;


import ru.windcorp.crystalfarm.graphics.ModuleGraphicsInterface;

import ru.windcorp.crystalfarm.gui.*;

public class SettingsAdvancedMenu extends MenuLayer {

	public SettingsAdvancedMenu() {
		super("AdvancedSettingsMenu", true);
		
		add(GuiSettingEditors.createEditor(ModuleGraphicsInterface.SHOW_FPS));
		add(GuiSettingEditors.createEditor(ModuleGraphicsInterface.VSYNC));
	}
	
}
