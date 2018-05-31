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
package ru.windcorp.crystalfarm.cfg;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.crystalfarm.struct.modules.ModuleRegistry;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.jobs.JobManager;

public class ModuleConfiguration extends Module {
	
	static Configuration mainConfig = null;
	
	static DocumentBuilder defaultBuilder = null;
	static Transformer defaultTransformer = null;
	
	public static Configuration getMainConfiguration() {
		if (mainConfig == null) {
			IllegalStateException e = new IllegalStateException("Main configuration not loaded");
			
			ExecutionReport.reportError(e, null,
					"%s.getMainConfiguration() method was invoked before main configuration has been loaded",
					ModuleConfiguration.class.getName());
			throw e;
		}
		
		return mainConfig;
	}

	public static DocumentBuilder getDefaultBuilder() {
		if (defaultBuilder == null) {
			IllegalStateException e = new IllegalStateException("Default builder not initialized");
			
			ExecutionReport.reportError(e, null,
					"%s.getDefaultBuilder() method was invoked before default builder has been initialized",
					ModuleConfiguration.class.getName());
			throw e;
		}
		
		return defaultBuilder;
	}

	public static Transformer getDefaultTransformer() {
		if (defaultTransformer == null) {
			IllegalStateException e = new IllegalStateException("Default transformer not initialized");
			
			ExecutionReport.reportError(e, null,
					"%s.getDefaultTransformer() method was invoked before default transformer has been initialized",
					ModuleConfiguration.class.getName());
			throw e;
		}
		
		return defaultTransformer;
	}
	
	public static void registerConfiguration(Mod mod) {
		Log.debug("Registering configuration for mod " + mod);
		
		final Section modSection = new Section(mod.getName(), "Configuration related to mod " + mod.getMetadata().userFriendlyName);
		
		ModuleRegistry.getModulesByMod(mod).forEach(module -> {
			Log.debug("Registering configuration for module " + module);
			
			Section moduleSection = new Section(module.getName(), "Configuration related to module " + module.toString());
			module.registerConfiguration(moduleSection);
			modSection.add(moduleSection);
		});

		getMainConfiguration().add(modSection);
	}
	
	public static boolean save() {
		try {
			getMainConfiguration().write();
			return true;
			
		} catch (IllegalStateException e) {
			ExecutionReport.reportError(e, null,
					"Could not save configuration because it is in an invalid state");
		} catch (IOException e) {
			ExecutionReport.reportError(e,
					ExecutionReport.rscUnrch("Main configuration", "Could not save main configuration to file"),
					null);
		} catch (TransformerException e) {
			ExecutionReport.reportError(e, null, "Could not output main configuration as XML");
		}
		
		return false;
	}

	public ModuleConfiguration() {
		super("Configuration", InbuiltMod.INST);
	}

	@Override
	public void registerJobs(JobManager<ModuleJob> manager) {
		manager.addJob(new JobLoadConfig(this));
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (mainConfig != null) {
					
					save();
					
				}
			}
		});
	}

}
