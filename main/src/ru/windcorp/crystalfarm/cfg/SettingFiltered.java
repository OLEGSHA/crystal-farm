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

import java.util.function.Predicate;

import ru.windcorp.crystalfarm.cfg.rw.SettingReader;
import ru.windcorp.crystalfarm.cfg.rw.SettingWriter;

public class SettingFiltered<T> extends Setting<T> {
	
	private final Predicate<? super T> filter;
	
	public SettingFiltered(String name, String description, Class<T> type, T defaultValue, SettingReader<T> reader,
			SettingWriter<T> writer, Predicate<? super T> filter) {
		super(name, description, type, defaultValue, reader, writer);
		this.filter = filter;
	}

	public SettingFiltered(String name, String description, Class<T> type, T defaultValue,
			Predicate<? super T> filter) {
		super(name, description, type, defaultValue);
		this.filter = filter;
	}

	public Predicate<? super T> getFilter() {
		return filter;
	}
	
	@Override
	public T read(String value) throws ConfigurationSyntaxException {
		T result = super.read(value);
		if (!getFilter().test(result)) {
			throw new ConfigurationSyntaxException("\"" + result + "\" is not a valid value", this);
		}
		return result;
	}

}
