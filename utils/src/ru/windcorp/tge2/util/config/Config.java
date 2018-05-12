package ru.windcorp.tge2.util.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import ru.windcorp.tge2.fileio.data.sbd.*;
import ru.windcorp.tge2.util.Nameable;

public class Config extends Nameable {

	private final Collection<ISetting<?>> settings = new ArrayList<ISetting<?>>(20);
	private final Collection<Config> subconfigs = new ArrayList<Config>();

	public Config(String name) {
		super(name);
	}
	
	public Collection<ISetting<?>> getSettings() {
		return settings;
	}
	
	public synchronized Config add(ISetting<?> setting) {
		getSettings().add(setting);
		return this;
	}
	
	public <T> Setting<T> createDefSetting(String name, Class<T> type, T defaultValue) {
		Setting<T> setting = new Setting<T>(name, type, defaultValue);
		add(setting);
		return setting;
	}
	
	public synchronized void remove(ISetting<?> setting) {
		getSettings().remove(setting);
	}

	public Collection<Config> getSubconfigs() {
		return subconfigs;
	}
	
	public synchronized Config createSubconfig(String name) {
		return addSubconfig(new Config(name));
	}
	
	public synchronized <T extends Config> T addSubconfig(T subconfig) {
		getSubconfigs().add(subconfig);
		return subconfig;
	}
	
	public synchronized void removeSubconfig(Config config) {
		getSubconfigs().remove(config);
	}
	
	public void load(SBDStructure parent) throws IOException {
		SBDStructure struct = parent.getStructureOrAdd(getName(), true);
		
		for (ISetting<?> setting : getSettings()) {
			setting.load(struct);
		}
		
		for (Config subconfig : getSubconfigs()) {
			subconfig.load(struct);
		}
	}
	
}
