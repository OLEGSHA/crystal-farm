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
package ru.windcorp.crystalfarm.gui;

import java.util.Map;

import ru.windcorp.crystalfarm.graphics.fonts.FontString;

public class Label extends Component {

	private FontString text;
	private int margin = 5;
	
	public Label(String name, FontString text) {
		super(name);
		setText(text);
	}

	public FontString getText() {
		return text;
	}

	public void setText(FontString label) {
		this.text = label;
		
		if (label != null) {
			label.addChangeListener(str -> recalculateSize());
		}
		
		recalculateSize();
	}
	
	public int getMargin() {
		return margin;
	}

	public Label setMargin(int margin) {
		this.margin = margin;
		return this;
	}

	protected synchronized void recalculateSize() {
		if (getText() == null) {
			setPreferredSize(getMargin() * 2, getMargin() * 2);
			return;
		}
		
		Size strSize = getText().getBounds();
		setPreferredSize(strSize.width + getMargin() * 2, strSize.height + getMargin() * 2);
		invalidate();
	}

	@Override
	protected void renderSelf() {
		if (getText() != null)
			getText().render(getX() + getMargin(), getY() + getMargin());
	}
	
	@Override
	protected void getDumpCharacteristics(Map<String, String> map) {
		super.getDumpCharacteristics(map);
		if (text != null) {
			map.put("text", text.toString());
			map.put("font", text.getFont().getName());
			map.put("color", String.valueOf(text.getColor()));
			map.put("style", String.valueOf(text.getStyle()));
			map.put("bold", String.valueOf(text.isBold()));
		}
	}

}
