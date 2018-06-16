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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;

import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.fonts.Font;
import ru.windcorp.crystalfarm.graphics.fonts.FontString;
import ru.windcorp.crystalfarm.graphics.fonts.FontStyle;

/**
 * Represents a mutable {@link String} capable of discrete changes.
 * <p>
 * Create new instances with the following static methods:
 * <ul>
 * <li>{@link #wrap(Object)} - wrap a static object</li>
 * <li>{@link #translated(String)} - create a reference to a translated string</li>
 * <li>{@link #concat(Object, Object...)} - concatenate several objects or TStrings</li>
 * <li>{@link #formatted(Object, Object...)} - format the given object with given arguments as per {@link String#format(String, Object...)}</li>
 * </ul>
 * 
 * @author OLEGSHA
 */
public abstract class TString implements Comparable<TString> {
	
	private class Updater implements Consumer<Object> {
		final WeakReference<TString> parent;
		
		Updater(TString parent) {
			this.parent = new WeakReference<TString>(parent);
		}
		
		@Override
		public void accept(Object t) {
			TString parent = this.parent.get();
			if (parent != null) {
				parent.update();
			}
		}
	}

	private Collection<Consumer<? super TString>> changeListeners = null;
	private String cache = null;
	
	/**
	 * Gets the current String representation of this TString.
	 * @return the value of this TString
	 */
	public String get() {
		if (cache == null) {
			cache = compute();
		}
		
		return cache;
	}
	
	/**
	 * Computes the current String representation of this TString.
	 * This method is only normally invoked once per update.
	 * This method is expected to evaluate equally between updates.
	 * @return the value of this TString
	 * @see {@link #update}
	 */
	protected abstract String compute();
	
	/**
	 * Adds a change listener. Change listeners are notified when this TString changes.
	 * Change listeners are provided the updated TString when invoked.
	 * @param listener the listener to add
	 */
	public synchronized void addChangeListener(Consumer<? super TString> listener) {
		if (changeListeners == null) {
			changeListeners = Collections.synchronizedCollection(new ArrayList<>());
		}
		
		changeListeners.add(listener);
	}
	
	/**
	 * Removes the given change listener. Fails silently if the listener was not present.
	 * @param listener the listener to remove
	 */
	public synchronized void removeChangeListener(Consumer<? super TString> listener) {
		if (changeListeners != null) {
			changeListeners.remove(listener);
		}
	}
	
	/**
	 * Gets the added change listeners.
	 * @return a synchronized collection with all added change listeners.
	 */
	public Collection<Consumer<? super TString>> getListeners() {
		return changeListeners;
	}
	
	/**
	 * Notifies change listeners of a change and clears cache.
	 */
	protected void update() {
		cache = null;
		if (changeListeners != null) {
			changeListeners.forEach(listener -> listener.accept(this));
		}
	}
	
	/**
	 * If the given object is a TString, adds a change listener that updates
	 * this TString. Useful when the given object is used to evaluate this TString.
	 * @param obj the object to check
	 */
	protected void listenForUpdates(Object obj) {
		if (obj instanceof TString) {
			((TString) obj).addChangeListener(new Updater(this));
		}
	}

	/**
	 * Gets the current String representation of this TString.
	 * @return the value of this TString
	 */
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
	
	/**
	 * Wraps the given object. The returned TString will always
	 * evaluate to {@code object.toString()}.
	 * The returned TString will never change.
	 * @param obj the object to wrap
	 * @return a TString representation of this object.
	 */
	public static TString wrap(Object obj) {
		return new TStringWrapper(obj);
	}
	
	/**
	 * Creates a TString that evaluates to the translation mapped to the given key.
	 * @param key the key to look up
	 * @return a locale-specific TString
	 */
	public static TString translated(String key) {
		return new TStringTranslated(key);
	}
	
	/**
	 * Creates a concatenation of the given objects' {@code toString()} methods.
	 * The returned TString updates when any of the given objects is a TString and updates.
	 * @param first the first object
	 * @param others any number of other objects
	 * @return a TString the evaluates to the concatenation of the given objects
	 * @see {@link #concat(Object[])}
	 */
	public static TString concat(Object first, Object... others) {
		return new TStringConcat(first, others);
	}
	
	/**
	 * Single-array version of {@link TString #concat(Object, Object...)}.
	 * @param the objects to concatenate
	 * @return a TString the evaluates to the concatenation of the given objects
	 */
	public static TString concat(Object[] objects) {
		if (objects.length == 0) {
			return new TStringConcat("");
		}
		
		Object[] parts = new Object[objects.length - 1];
		System.arraycopy(objects, 1, parts, 0, parts.length);
		
		return concat(objects[0], parts);
	}
	
	/**
	 * Creates a formatted TString that uses the given format and arguments.
	 * Format is applied as per {@link String#format(String, Object...)}.
	 * If the format cannot be applied, the raw format is returned.
	 * The returned TString updates when any of the given objects is a TString and updates.
	 * @param format a format string
	 * @param args format arguments
	 * @return a TString that evaluates to {@code String.format(format, args)}
	 */
	public static TString formatted(Object format, Object... args) {
		return new TStringFormatter(format, args);
	}
	
	/**
	 * Creates a TString that formats this TString with the given arguments.
	 * A convenience method for {@code TString.formatted(this, args)}.
	 * @param args format arguments
	 * @return a TString that evaluates to {@code String.format(this, args)}
	 * @see {@link #formatted(Object, Object...)}
	 */
	public TString toFormatted(Object... args) {
		return formatted(this, args);
	}
	
	/**
	 * Creates a {@link FontString} initialized with this TString.
	 * @param font the font to use
	 * @param style the font style to use
	 * @param bold whether text is bold
	 * @param color the color of text
	 * @return a FontString representing this TString.
	 * @see {@link FontString#FontString(TString, Font, FontStyle, boolean, Color)}
	 */
	public FontString toFont(Font font, FontStyle style, boolean bold, Color color) {
		return new FontString(this, font, style, bold, color);
	}
	
	
	/**
	 * Creates a {@link FontString} initialized with this TString.
	 * @return a FontString representing this TString.
	 * @see {@link FontString#FontString(TString)}
	 */
	public FontString toFont() {
		return new FontString(this);
	}
	
	/**
	 * Creates a TString that evaluates to {@code function.apply(this.get())}.
	 * @param the transformation to apply
	 * @return a transformed version of this TString
	 */
	public TString apply(Function<String, String> function) {
		return new TStringFunc(this, function);
	}
	
	/**
	 * Creates a TString that evaluates to a concatenation of this TString and given objects.
	 * A convenience method for {@code TString.concat(this, objects)}.
	 * @param objects the objects to append to this TString
	 * @return a concatenation of this TString and the given objects
	 * @see {@link #concat(Object, Object...)}
	 */
	public TString append(Object... objects) {
		return new TStringConcat(this, objects);
	}
	
}
