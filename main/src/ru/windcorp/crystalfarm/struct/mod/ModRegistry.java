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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class ModRegistry {
	
	private static final Map<String, Mod> MAP = Collections.synchronizedMap(new HashMap<>());
	
	public static Map<String, Mod> getMods() {
		return MAP;
	}
	
	public static void register(Mod mod) {
		Mod previous = MAP.put(mod.getName(), mod);
		if (previous != null) {
			ExecutionReport.reportCriticalError(null, null,
					"Mod identified by name %s has already been registered (mod metadata: old: %s, new: %s)",
					mod.getName(), previous.getMetadata(), mod.getMetadata());
			return;
		}
		
		Log.info("Registered mod " + mod.getName() + "; registering modules");
		Log.debug("Mod metadata: " + mod.getMetadata().toString());
		
		Log.topic(mod.getName());
		mod.registerModules();
		Log.end(mod.getName());
	}
	
	public static Mod get(String name) {
		return MAP.get(name);
	}

}
