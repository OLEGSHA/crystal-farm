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
package ru.windcorp.crystalfarm.graphics.fonts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.gui.Label;
import ru.windcorp.crystalfarm.gui.Size;
import ru.windcorp.crystalfarm.translation.TString;

public class FontString implements Consumer<TString> {
	
	private static final char[] NULL_TEXT = "null".toCharArray();
	
	private TString text;
	
	private Font font;
	private FontStyle style;
	private boolean bold;
	private Color color;
	
	private char[] chars;
	private Size size;
	
	public FontString(TString text, Font font, FontStyle style, boolean bold, Color color) {
		this.font = font;
		this.style = style;
		this.color = color;
		setText(text);
	}
	
	public FontString(TString text) {
		this(text, FontManager.getDefaultFont(), FontStyle.PLAIN, false, Color.WHITE);
	}

	public void render(int x, int y) {
		getFont().render(chars, x, y, isBold(), getStyle(), getColor());
	}
	
	public TString getText() {
		return text;
	}
	
	public void setText(TString text) {
		if (this.text != null) {
			this.text.removeChangeListener(this);
		}
		this.text = text;
		if (this.text != null) {
			this.text.addChangeListener(this);
		}
		accept(text);
	}
	
	public Size getBounds() {
		if (size == null) {
			calculateSize();
		}
		return size;
	}
	
	private Size calculateSize() {
		return this.size = getFont().getSize(chars, isBold());
	}
	
	public Font getFont() {
		return font == null ? FontManager.getDefaultFont() : font;
	}

	public FontString setFont(Font font) {
		this.font = font;
		return this;
	}

	public FontStyle getStyle() {
		return style;
	}

	public FontString setStyle(FontStyle style) {
		this.style = style;
		return this;
	}

	public Color getColor() {
		return color;
	}

	public FontString setColor(Color color) {
		this.color = color;
		return this;
	}

	public boolean isBold() {
		return bold;
	}

	public FontString setBold(boolean bold) {
		this.bold = bold;
		this.size = null;
		return this;
	}

	@Override
	public void accept(TString x) {
		if (getText() != null) {
			this.chars = toString().toCharArray();
			
			if (getFont() != null) {
				calculateSize();
			} else {
				this.size = null;
			}
		} else {
			chars = NULL_TEXT;
		}
		
		update();
	}
	
	@Override
	public String toString() {
		return getText() == null ? "null" : getText().toString();
	}
	
	private Collection<Consumer<? super FontString>> changeListeners = null;
	
	public synchronized void addChangeListener(Consumer<? super FontString> listener) {
		if (changeListeners == null) {
			changeListeners = Collections.synchronizedCollection(new ArrayList<>());
		}
		
		changeListeners.add(listener);
	}
	
	public synchronized void removeChangeListener(Consumer<? super FontString> listener) {
		if (changeListeners != null) {
			changeListeners.remove(listener);
		}
	}
	
	public Collection<Consumer<? super FontString>> getListeners() {
		return changeListeners;
	}
	
	protected void update() {
		if (changeListeners != null) {
			changeListeners.forEach(listener -> listener.accept(this));
		}
	}
	
	public Label toLabel(String name) {
		return new Label(name, this);
	}

}
