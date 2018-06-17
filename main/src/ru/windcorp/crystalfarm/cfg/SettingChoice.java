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

import java.util.Collection;

import ru.windcorp.crystalfarm.cfg.rw.SettingReader;
import ru.windcorp.crystalfarm.cfg.rw.SettingWriter;

public class SettingChoice<T> extends SettingFiltered<T> {

	public SettingChoice(String name, String description, Class<T> type, T defaultValue, Collection<?> choices) {
		super(name, description, type, defaultValue,
				choices::contains);
	}

	public SettingChoice(String name, String description, Class<T> type, T defaultValue, SettingReader<T> reader,
			SettingWriter<T> writer, Collection<?> choices) {
		super(name, description, type, defaultValue, reader, writer,
				choices::contains);
	}

}
