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

import ru.windcorp.crystalfarm.graphics.fonts.GString;

public class Label extends Component {

	private GString text;
	private int margin = 5;
	
	public Label(String name, GString text) {
		super(name);
		setText(text);
	}

	public GString getText() {
		return text;
	}

	public void setText(GString label) {
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
	}

	@Override
	protected void renderSelf() {
		if (getText() != null)
			getText().render(getX() + getMargin(), getY() + getMargin());
	}

}
