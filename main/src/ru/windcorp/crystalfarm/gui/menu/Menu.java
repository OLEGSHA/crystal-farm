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
package ru.windcorp.crystalfarm.gui.menu;

import ru.windcorp.crystalfarm.graphics.GraphicsInterface;
import ru.windcorp.crystalfarm.graphics.fonts.FontStyle;
import ru.windcorp.crystalfarm.graphics.fonts.GString;
import ru.windcorp.crystalfarm.gui.Component;
import ru.windcorp.crystalfarm.gui.Label;
import ru.windcorp.crystalfarm.gui.layout.LayoutVertical;

public class Menu extends Component {

	public Menu(String name, GString title) {
		super(name);
		setLayout(new LayoutVertical(3*LINE_THICKNESS));
		addChild(new Label(name + ".label", title.setBold(true).setStyle(FontStyle.ENGRAVED)));
	}
	
	@Override
	protected void renderSelf() {
		GraphicsInterface.fillRectangle(
				getX(),
				getY(),
				getWidth(),
				getHeight(),
				BACKGROUND_COLOR,
				BACKGROUND_COLOR_DARKER,
				LINE_THICKNESS);
	}

}
