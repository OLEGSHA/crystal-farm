package ru.windcorp.tge2.util.config.uf;

import ru.windcorp.tge2.util.config.Config;
import ru.windcorp.tge2.util.config.Setting;

public class UFConfig extends Config {
	
	public static final String DESCRIPTION_SETTING_NAME = "@ufc_dsc";
	
	private final Setting<String> description;

	public UFConfig(String name, String description) {
		super(name);
		add(this.description = new Setting<String>(DESCRIPTION_SETTING_NAME, String.class, description));
	}
	
	public String getDescription() {
		return this.description.getValue();
	}
	
	public UFConfig createUFSubconfig(String name, String description) {
		return addSubconfig(new UFConfig(name, description));
	}
	
	public <T> UFSetting<T> createUFSetting(String name, Class<T> type, T defaultValue, String description) {
		UFSetting<T> setting = new UFSetting<T>(name, type, defaultValue, description);
		add(setting);
		return setting;
	}

}
