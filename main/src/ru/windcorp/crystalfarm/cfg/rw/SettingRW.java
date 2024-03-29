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

import ru.windcorp.tge2.util.Nameable;

public class SettingRW<T> extends Nameable {
	
	private final SettingReader<T> reader;
	private final SettingWriter<T> writer;
	
	private final Class<T> clazz;
	
	public SettingRW(String name, Class<T> clazz, SettingReader<T> reader,SettingWriter<T> writer) {
		super(name);
		this.clazz = clazz;
		this.reader = reader;
		this.writer = writer;
	}

	public SettingReader<T> getReader() {
		return reader;
	}

	public SettingWriter<T> getWriter() {
		return writer;
	}

	public Class<T> getClazz() {
		return clazz;
	}

}
