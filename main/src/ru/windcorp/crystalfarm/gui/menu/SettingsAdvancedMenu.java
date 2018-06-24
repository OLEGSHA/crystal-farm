package ru.windcorp.crystalfarm.gui.menu;

import ru.windcorp.crystalfarm.gui.Button;
import ru.windcorp.crystalfarm.translation.TString;

public class SettingsAdvancedMenu extends MenuLayer {
	public SettingsAdvancedMenu() {
		super("SettingsAdvancedMenu", true);
		
		add(new Button(
				"SettingsAdvancedMenu.notifier",
				TString.translated("menu.SettingsAdvancedMenu.notifier").toFont(),
				button -> new SettingsNotifierMenu().show()
				));
	}
}
