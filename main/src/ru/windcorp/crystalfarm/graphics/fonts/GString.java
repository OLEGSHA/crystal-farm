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

import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.gui.Size;
import ru.windcorp.crystalfarm.translation.TString;

public class GString extends TString {
	
	private Font font;
	private FontStyle style;
	private boolean bold;
	private Color color;
	
	private char[] chars;
	private Size size;
	
	public GString(String name, Font font, FontStyle style, boolean bold, Color color, Object... args) {
		super(name, args);
		this.font = font;
		this.style = style;
		this.color = color;
	}
	
	public GString(String name, Object... args) {
		this(name, FontManager.getDefaultFont(), FontStyle.PLAIN, false, Color.WHITE, args);
	}
	
	@Override
	protected synchronized void load() {
		super.load();
		this.chars = get().toCharArray();
		
		if (getFont() != null) {
			calculateSize();
		} else {
			this.size = null;
		}
	}

	public void render(int x, int y) {
		getFont().render(chars, x, y, isBold(), getStyle(), getColor());
	}
	
	public Size getBounds() {
		if (size == null)
			calculateSize();
		return size;
	}
	
	private Size calculateSize() {
		return this.size = getFont().getSize(chars, isBold());
	}
	
	public Font getFont() {
		return font == null ? FontManager.getDefaultFont() : font;
	}

	public GString setFont(Font font) {
		this.font = font;
		return this;
	}

	public FontStyle getStyle() {
		return style;
	}

	public GString setStyle(FontStyle style) {
		this.style = style;
		return this;
	}

	public Color getColor() {
		return color;
	}

	public GString setColor(Color color) {
		this.color = color;
		return this;
	}

	public boolean isBold() {
		return bold;
	}

	public GString setBold(boolean bold) {
		this.bold = bold;
		return this;
	}

}
