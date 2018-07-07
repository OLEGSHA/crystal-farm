package ru.windcorp.crystalfarm.gui.menu;

import ru.windcorp.crystalfarm.gui.Button;
import ru.windcorp.crystalfarm.translation.TString;

public class SettingsAdvancedMenu extends MenuLayer {
	public SettingsAdvancedMenu() {
		super("SettingsAdvancedMenu", true);
		
		add(new Button(
				"SettingsAdvancedMenu.notifier",
				TString.translated("menu.SettingsNotifierMenu.title").toFont(),
				button -> new SettingsNotifierMenu().show(false)
				));
		add(new Button(
				"SettingsAdvancedMenu.design",
				TString.translated("menu.SettingsDesignMenu.title").toFont(),
				button -> new SettingsDesignMenu().show(false)
				));
		
	}
}
