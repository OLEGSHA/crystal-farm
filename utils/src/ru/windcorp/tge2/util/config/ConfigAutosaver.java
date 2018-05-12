package ru.windcorp.tge2.util.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class ConfigAutosaver implements Runnable {

	public static final Collection<ConfigFile> FILES = Collections.synchronizedCollection(new ArrayList<ConfigFile>());
	
	private static boolean isShutdownHookAdded = false;
	
	@Override
	public void run() {
		Log.topic("Config Autosave");
		Log.debug("Saving configs...");
		
		synchronized (FILES) {
			for (ConfigFile f : FILES) {
				try {
					f.save();
					Log.debug("Config " + f.getName() + " saved");
				} catch (Exception e) {
					ExecutionReport.reportError(e,
							ExecutionReport.rscUnrch("Config \"" + f + "\" in file " + f.getDefaultResource(),
									"Config Autosaver could not save config file"),
							null);
				}
			}
		}
		
		Log.debug("Configs saved");
		Log.endAll();
	}
	
	public static void addToAutosave(ConfigFile file) {
		synchronized (FILES) {
			FILES.add(file);
			
			if (!isShutdownHookAdded) {
				addShutdownHook();
				isShutdownHookAdded = true;
			}
		}
	}
	
	public static boolean removeAutosave(ConfigFile file) {
		synchronized (FILES) {
			return FILES.remove(file);
		}
	}
	
	public static Collection<ConfigFile> getConfigs() {
		return FILES;
	}

	private static void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(
				new Thread(new ConfigAutosaver(), "Config Autosave Shutdown Hook"));
	}

}
