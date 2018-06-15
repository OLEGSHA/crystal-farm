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
import java.util.function.Consumer;
import java.util.function.Function;

import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.fonts.Font;
import ru.windcorp.crystalfarm.graphics.fonts.FontString;
import ru.windcorp.crystalfarm.graphics.fonts.FontStyle;

public abstract class TString implements Comparable<TString> {

	private Collection<Consumer<? super TString>> changeListeners = null;
	private String cache = null;
	
	public final String get() {
		if (cache == null) {
			cache = compute();
		}
		
		return cache;
	}
	
	protected abstract String compute();
	
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
	
	protected void update() {
		cache = null;
		if (changeListeners != null) {
			changeListeners.forEach(listener -> listener.accept(this));
		}
	}

	@Override
	public String toString() {
		return get();
	}

	@Override
	public int hashCode() {
		return get().hashCode();
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
		String otherValue = other.get();
		String ownValue = get();
		
		if (ownValue == null) {
			if (otherValue != null)
				return false;
		} else if (!ownValue.equals(otherValue))
			return false;
		return true;
	}

	@Override
	public int compareTo(TString o) {
		return get().compareTo(o.get());
	}
	
	/*
	 * Builders
	 */
	
	public static TString wrap(Object obj) {
		return new TStringWrapper(obj);
	}
	
	public static TString translated(String key) {
		return new TStringTranslated(key);
	}
	
	public static TString concat(Object... objects) {
		return new TStringConcat(objects);
	}
	
	public static TString formatted(Object format, Object... args) {
		return new TStringFormatter(format, args);
	}
	
	public TString toFormatted(Object... args) {
		return formatted(this, args);
	}
	
	public FontString toFont(Font font, FontStyle style, boolean bold, Color color) {
		return new FontString(this, font, style, bold, color);
	}
	
	public FontString toFont() {
		return new FontString(this);
	}
	
	public TString apply(Function<String, String> function) {
		return new TStringFunc(this, function);
	}
	
}
