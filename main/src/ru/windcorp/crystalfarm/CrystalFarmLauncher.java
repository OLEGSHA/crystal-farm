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

import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class CrystalFarmLauncher {

	public static void main(String[] args) {
		System.out.println("Starting " + CrystalFarm.FULL_NAME + " version " + CrystalFarm.VERSION);
		
		setupExecutionReport();
		setupLog();
	}

	public static void setupExecutionReport() {
		ExecutionReport.enableReportAtShutdown();
		ExecutionReport.enableUnhandledThrowableHandling();
		ExecutionReport.addDefaults();
	}
	
	private static void setupLog() {
		
	}

}
