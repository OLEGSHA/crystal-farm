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

public class TileRegistries {
	
	private static final Map<String, TileRegistry<?>> MAP = Collections.synchronizedMap(new HashMap<>());
	
	@SuppressWarnings("unchecked")
	public static <T extends Tile> TileRegistry<T> getRegistry(TileLevel<T> level) {
		TileRegistry<?> reg = MAP.get(level.getName());
		
		if (reg == null) {
			ExecutionReport.reportCriticalError(null, null,
					"No tile registry found for level %s",
					level.getName());
			return null;
		}
		
		if ((Class<?>) reg.getTileClass() != (Class<?>) level.getTileClass()) {
			ExecutionReport.reportCriticalError(null, null,
					"The tile class %s of tile registry %s does not match the class of the level %s",
					reg.getTileClass().getName(),
					level.getName(),
					level.getTileClass().getName());
			return null;
		}
		
		return (TileRegistry<T>) reg;
	}
	
	public static void register(TileRegistry<?> registry) {
		synchronized (MAP) {
			if (MAP.containsKey(registry.getName())) {
				ExecutionReport.reportError(null, null, "Tile registry with name %s has already been registered", registry.getName());
				// Continue
			}
			
			MAP.put(registry.getName(), registry);
		}
	}

}
