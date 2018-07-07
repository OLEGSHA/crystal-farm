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

import java.util.ArrayList;
import java.util.List;

import ru.windcorp.tge2.util.Version;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.unixarg.UnixArgumentSystem;

public class CrystalFarm {

	public static final String INTERNAL_NAME = "crystal-farm";
	public static final String FULL_NAME = "Crystal Farm";
	
	public static final String CLI_DESCRIPTION = "A farming game about growing alive crytals";
	
	public static final String LICENSE = "GNU General Public License version 3 or any later version";
	public static final List<String> DEVELOPERS = new ArrayList<>();
	public static final String YEARS = "2018";
	
	public static final Version VERSION = new Version(
			// Stage: 0 = pre-release, 1 = release
			0,
			// Major version: changes on big updates
			0,
			// Minor version: changes on every update
			0,
			// Build: changes every time a JAR is exported
			0);
	
	public static final String VERSION_CODENAME = "Derpy";
	
	private static String[] launchArgs = null;
	
	public static final UnixArgumentSystem ARGUMENT_SYSTEM = new UnixArgumentSystem(FULL_NAME, CLI_DESCRIPTION, null, VERSION);
	
	static {
		DEVELOPERS.add("OLEGSHA");
		DEVELOPERS.add("Neiroc");
	}

	public static String[] getLaunchArgs() {
		return launchArgs;
	}

	static void setLaunchArgs(String[] launchArgs) {
		CrystalFarm.launchArgs = launchArgs;
	}
	
	public static void exit(String reason, int status) {
		// TODO handle exit gracefully
		
		Log.topic("Exit");
		Log.info("Exiting: " + reason + " (exit status " + status + ")");
		//System.exit(status);
		ExecutionReport.exitAsynch(status);
	}
	
}
