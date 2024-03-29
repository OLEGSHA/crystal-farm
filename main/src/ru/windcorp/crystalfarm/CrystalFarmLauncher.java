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
package ru.windcorp.crystalfarm;

import static ru.windcorp.crystalfarm.CrystalFarm.*;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Queue;

import ru.windcorp.crystalfarm.debug.CrystalFarmExecutionReportBackend;
import ru.windcorp.crystalfarm.debug.CrystalFarmExecutionReportListener;
import ru.windcorp.crystalfarm.debug.JobReportPart;
import ru.windcorp.crystalfarm.debug.LWJGLValueSection;
import ru.windcorp.crystalfarm.debug.LayersDumpReportPart;
import ru.windcorp.crystalfarm.debug.LoadThreadsUnixArgument;
import ru.windcorp.crystalfarm.debug.ModReportPart;
import ru.windcorp.crystalfarm.debug.ModlistSuggestionProvider;
import ru.windcorp.crystalfarm.debug.ModuleReportPart;
import ru.windcorp.crystalfarm.struct.job.LowLevelJobListener;
import ru.windcorp.crystalfarm.struct.mod.ModRegistry;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.crystalfarm.struct.modules.ModuleRegistry;
import ru.windcorp.tge2.util.debug.Debug;
import ru.windcorp.tge2.util.debug.DebugUnixArgument;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.LogUnixArgument;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.jobs.JobManager;
import ru.windcorp.tge2.util.unixarg.UnixArgumentSystem;

public class CrystalFarmLauncher {
	
	private static JobManager<ModuleJob> jobManager = null;
	private static int loadThreads = Runtime.getRuntime().availableProcessors();
	
	private static final Queue<String> ARG_QUEUE = new LinkedList<>();

	public static void main(String[] args) {
		CrystalFarm.setLaunchArgs(args);
		
		setupBasicArguments();
		
		for (String s : args) {
			ARG_QUEUE.add(s);
		}
		
		processArguments1(ARG_QUEUE);
		
		setupExecutionReport();
		
		try {
			setupLog();
			logWelcome();
			
			Log.topic("Init");
			Log.info("Initializing");
			
			registerInbuiltMod();
			processArguments2(ARG_QUEUE);
			
			Log.debug("Creating job manager");
			createJobManager();
			
			Log.info("Running load jobs");
			runJobs();
			
		} finally {
			Log.endAll();
		}
	}

	private static void createJobManager() {
		jobManager = new JobManager<ModuleJob>();
		
		jobManager.addJobListener(new LowLevelJobListener());
	}

	private static void setupBasicArguments() {
		ARGUMENT_SYSTEM.addArgument(new DebugUnixArgument());
		ARGUMENT_SYSTEM.addArgument(new LogUnixArgument());
		ARGUMENT_SYSTEM.addArgument(new LoadThreadsUnixArgument());
	}

	private static void processArguments1(Queue<String> argQueue) {
		try {
			if (ARGUMENT_SYSTEM.run(argQueue.iterator(),
					UnixArgumentSystem.UnknownArgumentPolicy.IGNORE,
					UnixArgumentSystem.InvalidSyntaxPolicy.IGNORE,
					false)) {
				System.exit(0);
			}
		} catch (InvocationTargetException e) {
			System.err.println("Unhandled exception while processing arguments encountered");
			e.printStackTrace();
			
			// Not using CrystalFarm.exit() since it depends on Log
			System.exit(e.hashCode());
		}
	}

	public static void setupExecutionReport() {
		ExecutionReport.enableReportAtShutdown();
		ExecutionReport.enableUnhandledThrowableHandling();
		ExecutionReport.addDefaults();
		
		ExecutionReport.setBackend(new CrystalFarmExecutionReportBackend());
		ExecutionReport.addListener(new CrystalFarmExecutionReportListener());
		
		if (Debug.allowDebug) {
			ExecutionReport.setForcedReport(true);
		}
		
		ExecutionReport.addDebugValueSection(new LWJGLValueSection());

		ExecutionReport.addExtraReportPart(new ModReportPart());
		ExecutionReport.addExtraReportPart(new ModuleReportPart());
		ExecutionReport.addExtraReportPart(new JobReportPart());
		ExecutionReport.addExtraReportPart(new LayersDumpReportPart());
		
		ExecutionReport.addSuggestionProvider(new ModlistSuggestionProvider());
	}
	
	private static void setupLog() {
		// Do nothing
	}

	private static void logWelcome() {
		Log.info("Starting " + FULL_NAME + " version " + VERSION_CODENAME + "/" + VERSION + " ...");
		Log.info("Licensed under the terms of " + LICENSE);
	}

	private static void registerInbuiltMod() {
		InbuiltMod inbuiltMod = InbuiltMod.INST;
		
		Log.debug("Registering " + inbuiltMod);
		ModRegistry.register(inbuiltMod);
		Log.debug("Done");
	}
	
	private static void processArguments2(Queue<String> argQueue) {
		try {
			if (ARGUMENT_SYSTEM.run(argQueue.iterator(),
					UnixArgumentSystem.UnknownArgumentPolicy.IGNORE,
					UnixArgumentSystem.InvalidSyntaxPolicy.IGNORE,
					false)) {
				System.exit(0);
			}
		} catch (InvocationTargetException e) {
			ExecutionReport.reportCriticalError(e, null, "Unhandled exception while processing arguments encountered");
		}
	}
	
	private static void runJobs() {
		Log.topic("Registering first jobs");
		ModuleRegistry.getModules().forEach((name, module) -> module.registerJobs(getJobManager()));
		Log.end("Registering first jobs");
		
		getJobManager().doJobs(loadThreads);
	}
	
	public static JobManager<ModuleJob> getJobManager() {
		if (jobManager == null) {
			IllegalStateException e = new IllegalStateException("JobManager no longer exists");
			ExecutionReport.reportError(e, null,
					"%s.getJobManager() method was invoked after job manager had been destroyed",
					CrystalFarmLauncher.class.getName());
			throw e;
		}
		
		return jobManager;
	}
	
	public static boolean doesJobManagerExist() {
		return jobManager != null;
	}

	public static int getLoadThreads() {
		return getJobManager().getWorkers();
	}
	
	public static void setLoadThreads(int loadThreads) {
		CrystalFarmLauncher.loadThreads = loadThreads;
	}

	public static void processArguments3() {
		try {
			if (ARGUMENT_SYSTEM.run(ARG_QUEUE.iterator(),
					UnixArgumentSystem.UnknownArgumentPolicy.WARN,
					UnixArgumentSystem.InvalidSyntaxPolicy.TERMINATE,
					false)) {
				System.exit(0);
			}
		} catch (InvocationTargetException e) {
			ExecutionReport.reportCriticalError(e, null, "Unhandled exception while processing arguments encountered");
		}
	}

}
