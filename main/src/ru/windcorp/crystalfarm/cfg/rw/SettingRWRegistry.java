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
package ru.windcorp.crystalfarm.cfg.rw;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class SettingRWRegistry {
	
	private static final Map<Class<?>, SettingRW<?>> MAP = Collections.synchronizedMap(new HashMap<>());
	
	static {
		register(new SettingRWString());
		register(new SettingRWColor());
		register(new SettingRWKeyStroke());
	}
	
	public static void register(SettingRW<?> setting) {
		SettingRW<?> previous = MAP.put(setting.getClazz(), setting);
		
		if (previous != null) {
			ExecutionReport.reportWarning(null, null,
					"The SettingRW for class %s has been changed from %s to %s",
					setting.getClazz().getName(), previous.toString(), setting.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> SettingRW<T> get(Class<T> clazz) {
		return (SettingRW<T>) MAP.get(clazz);
	}

}
