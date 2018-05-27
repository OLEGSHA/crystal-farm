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
package ru.windcorp.crystalfarm.struct.modules;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class ModuleRegistry {
	
	private static final Map<String, Module> MAP = Collections.synchronizedMap(new HashMap<>());
	
	public static Map<String, Module> getModules() {
		return MAP;
	}
	
	public static void register(Module module) {
		Module previous = MAP.put(module.getName(), module);
		if (previous != null) {
			ExecutionReport.reportCriticalError(null, null,
					"Module identified by name %s has already been registered (class/mod: old: %s/%s, new: %s/%s)",
					module.getName(),
					previous.getClass().getName(), previous.getMod().getName(),
					module.getClass().getName(), module.getMod().getName());
			
			return;
		}
		
		Log.info("Regsitered module " + module);
	}
	
	public static Module get(String name) {
		return MAP.get(name);
	}
	
	public static Stream<Module> getModulesByMod(Mod mod) {
		return MAP.values().stream().filter(module -> module.getMod() == mod);
	}

}
