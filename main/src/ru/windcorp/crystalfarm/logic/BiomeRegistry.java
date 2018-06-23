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
package ru.windcorp.crystalfarm.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class BiomeRegistry {
	
	private static final Map<String, Biome> MAP = Collections.synchronizedMap(new HashMap<>());
	
	private static Biome fallback;
	
	public static Map<String, Biome> getBiomeMap() {
		return MAP;
	}
	
	public static Biome get(String name) {
		return getBiomeMap().get(name);
	}
	
	public static void register(Biome biome) {
		synchronized (getBiomeMap()) {
			if (getBiomeMap().containsKey(biome.getName())) {
				ExecutionReport.reportError(null, null, "Biome with name %s has already been registered", biome.getName());
				// Continue
			}
			
			getBiomeMap().put(biome.getName(), biome);
		}
	}

	public static Biome getFallback() {
		return fallback;
	}

	public static void setFallback(Biome fallback) {
		BiomeRegistry.fallback = fallback;
	}

}
