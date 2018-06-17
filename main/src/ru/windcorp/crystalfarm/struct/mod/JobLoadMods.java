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
package ru.windcorp.crystalfarm.struct.mod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import ru.windcorp.crystalfarm.CrystalFarm;
import ru.windcorp.crystalfarm.CrystalFarmLauncher;
import ru.windcorp.crystalfarm.CrystalFarmResourceManagers;
import ru.windcorp.crystalfarm.cfg.ModuleConfiguration;
import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.crystalfarm.struct.modules.ModuleRegistry;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.grh.ClassLoaderResourceSupplier;
import ru.windcorp.tge2.util.grh.ResourceManager;

public class JobLoadMods extends ModuleJob {

	public JobLoadMods(Module module) {
		super("LoadMods", "Loads external mods from disk", module);
		
		addDependency("Inbuilt:Configuration:LoadConfig");
	}

	@Override
	protected void runImpl() {
		File rootDirectory = new File(ModuleModLoader.getPath());
		
		if (!rootDirectory.exists()) {
			ExecutionReport.reportNotification(null,
					ExecutionReport.rscMissing(rootDirectory.getAbsolutePath(), "Mod directory does not exist"),
					null);
			
			if (!rootDirectory.mkdirs()) {
				ExecutionReport.reportError(null,
						ExecutionReport.rscUnrch(rootDirectory.getAbsolutePath(), "Mod directory does not exist and could not be created"),
						null);
				
				Log.error("No mod directory available, skipping mod loading");
				return;
			}
			
			Log.info("Empty mod directory, skipping mod loading");
			return;
		}
		
		if (!rootDirectory.isDirectory()) {
			ExecutionReport.reportError(null,
					ExecutionReport.rscCorrupt(rootDirectory.getAbsolutePath(), "Path specified as mod directory does not denote a directory"),
					null);
			
			Log.error("No mod directory available, skipping mod loading");
			return;
		}
		
		Log.info("Loading mods from folder " + ModuleModLoader.getPath());
		
		processDirectory(rootDirectory);
		processDirectory(new File(rootDirectory, CrystalFarm.VERSION.toString()));
		processDirectory(new File(rootDirectory, CrystalFarm.VERSION_CODENAME));
		CrystalFarmLauncher.processArguments3();
	}
	
	private void processDirectory(File directory) {
		if (!directory.isDirectory()) {
			return;
		}
		
		Log.debug("Searching for mod candidates in " + directory.getAbsolutePath());
		
		AtomicBoolean candidatesExist = new AtomicBoolean(false);
		
		Arrays.stream(
				directory.listFiles(file ->
								file.isFile() &&
								file.getName().endsWith(".jar")
						)
				).forEach(file -> {
					candidatesExist.set(true);
					processCandidate(file);
				});
		
		if (!candidatesExist.get()) {
			Log.info("No mods found");
		}
	}
	
	@SuppressWarnings("resource")
	private void processCandidate(File file) {
		Log.debug("Found mod candidate: " + file.getName());
		Log.topic(file.getName());
		
		try {
			
			if (!file.canRead()) {
				ExecutionReport.reportError(null,
						ExecutionReport.rscUnrch(file.getAbsolutePath(), "Mod candidate cannot be read"),
						null);
				return;
			}
			
			JarFile jarFile = null;
			
			try {
				jarFile = new JarFile(file);
			} catch (IOException e) {
				ExecutionReport.reportError(null,
						ExecutionReport.rscCorrupt(file.getAbsolutePath(), "Could not read file as JAR"),
						null);
				return;
			}
			
			Log.debug("JAR created");
			
			Manifest manifest = null;
			try {
				manifest = jarFile.getManifest();
			} catch (IOException e) {
				ExecutionReport.reportError(null,
						ExecutionReport.rscCorrupt(file.getAbsolutePath(), "Could not read JAR manifest file"),
						null);
				return;
			}
			
			if (manifest == null) {
				ExecutionReport.reportNotification(null,
						ExecutionReport.rscCorrupt(file.getAbsolutePath(), "JAR file is not a mod (no manifest file); ignoring"),
						null);
				return;
			}
			
			Log.debug("Manifest read");
			
			ModMeta meta = ModMeta.parse(manifest);
			if (meta == null) {
				ExecutionReport.reportNotification(null,
						ExecutionReport.rscCorrupt(file.getAbsolutePath(), "JAR file is not a mod (invalid manifest file); ignoring"),
						null);
				return;
			}
			
			Log.debug("Manifest parsed");
			Log.debug("About to initialize mod: " + meta);
			
			if (!meta.isFree) {
				if (ModuleModLoader.getLoadNonFree()) {
					ExecutionReport.reportNotification(null, null,
							"Mod %s is non-free. Non-free code restricts users making it inherently unsafe. "
							+ "Declared license: %s",
							meta.userFriendlyName, meta.copyright);
				} else {
					ExecutionReport.reportCriticalError(null, null,
							"Mod %s is non-free. Non-free code restricts users making it inherently unsafe. "
							+ "Loading non-free mods is disabled in configuration. Declared license: %s",
							meta.userFriendlyName, meta.copyright);
					return;
				}
			}
			
			String urlRepresentation = "jar:file:" + file.getAbsolutePath() + "!/";
			URL url = null;
			try {
				url = new URL(urlRepresentation);
			} catch (MalformedURLException e) {
				ExecutionReport.reportError(e, null,
						"Could not initialize URL object with URL representation \"%s\". Something has gone horribly wrong",
						urlRepresentation);
				return;
			}
			
			URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { url }, getClass().getClassLoader());
			
			Class<? extends Mod> modClass = null;
			Class<?> uncastModClass = null;
			try {
				uncastModClass = Class.forName(meta.modClass, false, urlClassLoader);
			} catch (ClassNotFoundException e) {
				ExecutionReport.reportError(null,
						ExecutionReport.rscCorrupt(
								"Manifest of mod " + meta.userFriendlyName + " in " + file.getAbsolutePath(),
								"Could not find mod class %s", meta.modClass),
						null);
				
				try {
					urlClassLoader.close();
				} catch (IOException e1) {
					ExecutionReport.reportCriticalError(e1, null, "Could not close URLClassLoader %s", urlClassLoader.toString());
				}
				return;
			}
			
			try {
				modClass = uncastModClass.asSubclass(Mod.class);
			} catch (ClassCastException e) {
				ExecutionReport.reportError(e,
						ExecutionReport.rscCorrupt(
								"Mod class " + uncastModClass.getName() + " (" + meta.userFriendlyName + " in " + file.getAbsolutePath() + ")",
								"Declared mod class %s is not a subclass of %s",
								uncastModClass.getName(),
								Mod.class.getName()),
						null);
				
				try {
					urlClassLoader.close();
				} catch (IOException e1) {
					ExecutionReport.reportCriticalError(e1, null, "Could not close URLClassLoader %s", urlClassLoader.toString());
				}
				return;
			}
			
			Log.debug("Mod class found: " + modClass.getName());
			
			Constructor<? extends Mod> modConstructor = null;
			try {
				modConstructor = modClass.getConstructor(ModMeta.class);
			} catch (NoSuchMethodException e) {
				ExecutionReport.reportError(e,
						ExecutionReport.rscCorrupt(
								"Mod class " + modClass.getName() + " (" + meta.userFriendlyName + " in " + file.getAbsolutePath() + ")",
								"Declared mod class %s does not have a correct constructor",
								modClass.getName()),
						null);
				return;
			}
			
			Mod mod = null;
			try {
				mod = modConstructor.newInstance(meta);
			} catch (InstantiationException e) {
				ExecutionReport.reportError(e,
						ExecutionReport.rscCorrupt(
								"Mod class " + modClass.getName() + " (" + meta.userFriendlyName + " in " + file.getAbsolutePath() + ")",
								"Declared mod class %s cannot be instantiated because it is an abstract class",
								modClass.getName()),
						null);
				return;
			} catch (IllegalAccessException e) {
				ExecutionReport.reportError(e,
						ExecutionReport.rscCorrupt(
								"Mod class " + modClass.getName() + " (" + meta.userFriendlyName + " in " + file.getAbsolutePath() + ")",
								"Declared mod class %s does not have a correct constructor: inaccessable from class %s",
								modClass.getName(),
								JobLoadMods.class.getName()),
						null);
				return;
			} catch (IllegalArgumentException e) {
				ExecutionReport.reportCriticalError(e,
						ExecutionReport.rscCorrupt(
								"Mod class " + modClass.getName() + " (" + meta.userFriendlyName + " in " + file.getAbsolutePath() + ")",
								"Could not use constructor %s with argument of type %s",
								modConstructor.toString(),
								ModMeta.class.getName()),
						null);
				return;
			} catch (InvocationTargetException e) {
				ExecutionReport.reportError(e,
						ExecutionReport.rscCorrupt(
								"Mod class " + modClass.getName() + " (" + meta.userFriendlyName + " in " + file.getAbsolutePath() + ")",
								"Mod %s failed to initialize",
								meta.userFriendlyName),
						null);
				return;
			}
			
			Log.debug("Mod instantiated successfully");
			
			ModRegistry.register(mod);
			CrystalFarmResourceManagers.RM_ASSETS.addBackup(
					new ResourceManager(mod.getName() + ":Assets", new ClassLoaderResourceSupplier(urlClassLoader, "assets"))
					);
			ModuleConfiguration.registerConfiguration(mod);
			ModuleRegistry.getModulesByMod(mod).forEach(module -> module.registerJobs(CrystalFarmLauncher.getJobManager()));
			
			Log.info("Mod " + mod + " initialized");
			
		} finally {
			Log.end(file.getName());
		}
		
	}

}
