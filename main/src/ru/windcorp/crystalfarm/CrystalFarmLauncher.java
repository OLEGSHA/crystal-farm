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

import ru.windcorp.tge2.util.debug.DebugUnixArgument;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.LogUnixArgument;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.unixarg.UnixArgumentSystem;

public class CrystalFarmLauncher {

	public static void main(String[] args) {
		CrystalFarm.setLaunchArgs(args);
		
		setupBasicArguments();
		
		Queue<String> argQueue = new LinkedList<>();
		for (String s : args) {
			argQueue.add(s);
		}
		
		processArguments1(argQueue);
		
		setupExecutionReport();
		
		try {
			setupLog();
			logWelcome();
			
			Log.topic("Init");
			initializeConfig();
		} finally {
			Log.endAll();
		}
	}

	private static void setupBasicArguments() {
		ARGUMENT_SYSTEM.addArgument(new DebugUnixArgument());
		ARGUMENT_SYSTEM.addArgument(new LogUnixArgument());
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
	}
	
	private static void setupLog() {
		// Do nothing
	}

	private static void logWelcome() {
		Log.info("Starting " + FULL_NAME + " version " + VERSION_CODENAME + "/" + VERSION + " ...");
		Log.info("Licensed under the terms of " + LICENSE);
	}

	private static void initializeConfig() {
		// TODO Auto-generated method stub
		System.err.println("Called auto-generated method CrystalFarmLauncher.CrystalFarmLauncher");
		
	}

}
