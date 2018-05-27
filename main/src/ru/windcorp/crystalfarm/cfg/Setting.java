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

import ru.windcorp.crystalfarm.cfg.rw.*;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class Setting<T> extends ConfigurationNode {
	
	private final Class<T> type;
	
	private T defaultValue;
	private T value;
	
	private final Function<String, T> reader;
	private final Function<T, String> writer;
	
	public Setting(String name, String description, Class<T> type, T defaultValue,
			Function<String, T> reader, Function<T, String> writer) {
		super(name, description);
		this.type = type;
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.reader = reader;
		this.writer = writer;
	}
	
	public Setting(String name, String description, Class<T> type, T defaultValue) {
		super(name, description);
		
		SettingRW<T> rw = SettingRWRegistry.get(type);
		if (rw == null) {
			ExecutionReport.reportCriticalError(null,
					ExecutionReport.rscNotSupp(getClass().getName() + " of type " + type.getName(),
							"No registered %s in %s for class %s",
							SettingRW.class.getName(), SettingRWRegistry.class.getName(), type.getName()),
					null);
			
			// That should not return
		}
		
		this.type = type;
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.reader = rw.getReader();
		this.writer = rw.getWriter();
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
		setRaw(value);
		if (getElement() != null) {
			getElement().setTextContent(getWriter().apply(value));
		}
	}
	
	protected synchronized void setRaw(T value) {
		this.value = value;
		fireEvent();
	}

	public Class<T> getType() {
		return type;
	}

	public Function<String, T> getReader() {
		return reader;
	}

	public Function<T, String> getWriter() {
		return writer;
	}

	@Override
	protected synchronized void loadImpl() {
		if (getElement() == null) {
			setRaw(getDefaultValue());
		} else {
			setRaw(getReader().apply(getElement().getTextContent()));
		}
	}

}
