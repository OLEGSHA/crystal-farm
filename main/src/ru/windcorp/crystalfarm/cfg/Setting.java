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
package ru.windcorp.crystalfarm.cfg;

import java.util.function.Function;

public class Setting<T> extends ConfigurationNode {
	
	private final Class<T> type;
	
	private T defaultValue;
	private T value;
	
	private final Function<T, String> reader;
	private final Function<String, T> writer;
	
	public Setting(String name, String description, Class<T> type, T defaultValue, Function<T, String> reader,
			Function<String, T> writer) {
		super(name, description);
		this.type = type;
		this.defaultValue = defaultValue;
		this.reader = reader;
		this.writer = writer;
	}

	public synchronized T getDefaultValue() {
		return defaultValue;
	}

	public synchronized void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
	}

	public synchronized T get() {
		return value;
	}

	public synchronized void set(T value) {
		this.value = value;
	}

	public Class<T> getType() {
		return type;
	}

	public Function<T, String> getReader() {
		return reader;
	}

	public Function<String, T> getWriter() {
		return writer;
	}

}
