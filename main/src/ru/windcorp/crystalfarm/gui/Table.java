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

import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.*;

import ru.windcorp.crystalfarm.gui.layout.LayoutGrid;
import ru.windcorp.crystalfarm.translation.TString;

public class Table extends Component {
	
	private int row = 0;

	public Table(String name, int gap) {
		super(name);
		setLayout(new LayoutGrid(0, gap));
	}
	
	public synchronized Table addRow(Component first, Component... children) {
		addChild(first.setLayoutHint(new int[] {0, row}));
		
		for (int i = 0; i < children.length; ++i) {
			if (children[i] == null) {
				continue;
			}
			
			addChild(children[i].setLayoutHint(new int[] {i + 1, row}));
		}
		
		row++;
		return this;
	}
	
	public Table addRow(String tagKey, Component... children) {
		addRow(
				TString.translated(tagKey)
				.toFont().setColor(gdGetFontAltColor())
				.toLabel(children[0].getName() + ".tag").align(0, 0.5),
				children);
		return this;
	}
	
}
