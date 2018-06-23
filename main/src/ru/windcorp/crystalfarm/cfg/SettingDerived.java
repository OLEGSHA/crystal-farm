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

import ru.windcorp.crystalfarm.cfg.rw.SettingReader;
import ru.windcorp.crystalfarm.cfg.rw.SettingWriter;

public class SettingDerived<T, S> extends Setting<T> {
	
	public static final String DERIVE = "DERIVE";
	
	private final Setting<S> parent;
	private final Function<S, T> func;
	
	private final boolean isDefaultCustom;
	private boolean isValueCustom;
	
	public SettingDerived(String name, String description, Class<T> type, T defaultValue, SettingReader<T> reader,
			SettingWriter<T> writer, Setting<S> parent, Function<S, T> func) {
		super(name, description, type,
				defaultValue == null ? func.apply(parent.getDefaultValue()) : defaultValue,
				reader, writer);
		this.parent = parent;
		this.func = func;
		this.isDefaultCustom = defaultValue != null;
		
		getParent().addListener(x -> update());
	}

	public SettingDerived(String name, String description, Class<T> type, T defaultValue, Setting<S> parent,
			Function<S, T> func) {
		super(name, description, type,
				defaultValue == null ? func.apply(parent.getDefaultValue()) : defaultValue);
		this.parent = parent;
		this.func = func;
		this.isDefaultCustom = defaultValue != null;

		getParent().addListener(x -> update());
	}
	
	public Setting<S> getParent() {
		return parent;
	}
	
	public Function<S, T> getFunction() {
		return func;
	}
	
	public boolean isDefaultCustom() {
		return isDefaultCustom;
	}

	public boolean isValueCustom() {
		return isValueCustom;
	}

	public T derive(S src) {
		return getFunction().apply(src);
	}
	
	public T derive() {
		return derive(getParent().get());
	}
	
	protected void update() {
		if (!isValueCustom()) {
			setRaw(derive());
		}
	}
	
	@Override
	public synchronized void set(T value) {
		if (value == null) {
			isValueCustom = false;
			update();
			return;
		}
		isValueCustom = true;
		
		super.set(value);
	}
	
	@Override
	public T read(String value) {
		if (DERIVE.equals(value)) {
			isValueCustom = false;
			return derive();
		}

		isValueCustom = true;
		return super.read(value);
	}
	
	@Override
	public String write(T value) {
		if (isValueCustom()) {
			return super.write(value);
		}
		
		return DERIVE;
	}

}
