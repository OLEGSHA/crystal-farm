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
package ru.windcorp.crystalfarm.translation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class TString implements Cloneable, Comparable<TString> {
	
	private static AtomicInteger cleanRejectCounter = new AtomicInteger(0);
	private static final int MAX_REJECTED_CLEANS = 10;
	
	private final String key;
	private String value = null;
	private Object[] args = null;
	
	private Collection<Consumer<? super TString>> changeListeners = null;

	public TString(String key, Object... args) {
		if (args != null && args.length == 0) args = null;
		this.key = key;
		this.setArgs(args);
		ModuleTranslation.register(this);
	}
	
	public TString(String key) {
		this(key, (Object[]) null);
	}
	
	public synchronized void addChangeListener(Consumer<? super TString> listener) {
		if (changeListeners == null) {
			changeListeners = Collections.synchronizedCollection(new ArrayList<>());
		}
		
		changeListeners.add(listener);
	}
	
	public synchronized void removeChangeListener(Consumer<? super TString> listener) {
		if (changeListeners != null) {
			changeListeners.remove(listener);
		}
	}
	
	public Collection<Consumer<? super TString>> getListeners() {
		return changeListeners;
	}
	
	public synchronized String getRaw() {
		if (value == null) {
			return getKey();
		}
		
		return value;
	}
	
	public String get() {
		return format(getArgs());
	}
	
	public String format(Object... args) {
		String raw = getRaw();
		if (args == null || raw.equals(getKey())) {
			return raw;
		}
		
		try {
			return String.format(raw, getArgs());
		} catch (Exception e) {
			// Fail silently
			return raw;
		}
	}
	
	protected synchronized void load() {
		value = ModuleTranslation.getValueForKey(getKey());
	}
	
	public String getKey() {
		return key;
	}
	
	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	@Override
	public String toString() {
		return get();
	}

	@Override
	public int hashCode() {
		return key == null ? 0 : key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TString other = (TString) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public int compareTo(TString o) {
		return getKey().compareTo(o.getKey());
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (cleanRejectCounter.getAndUpdate(i -> i == MAX_REJECTED_CLEANS ? i = 0 : i++) == MAX_REJECTED_CLEANS) {
			ModuleTranslation.clean();
		}
	}

}
