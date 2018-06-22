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
package ru.windcorp.crystalfarm.gui.layout;

import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.*;

import ru.windcorp.crystalfarm.gui.Component;
import ru.windcorp.crystalfarm.gui.Layout;
import ru.windcorp.crystalfarm.gui.Size;

public class LayoutVertical implements Layout {
	
	private final int margin, gap;

	public LayoutVertical(int margin, int gap) {
		this.margin = margin;
		this.gap = gap;
	}
	
	public LayoutVertical(int gap) {
		this(gap, gap);
	}
	
	public LayoutVertical() {
		this(1);
	}

	@Override
	public void layout(Component c) {
		int x = c.getX() + margin*gdGetLine(),
			y = c.getY() + margin*gdGetLine();
		
		int height;
		
		synchronized (c.getChildren()) {
			for (Component child : c.getChildren()) {
				
				height = child.getPreferredSize().height;
				child.setBounds(x, y, c.getWidth() - 2 * margin*gdGetLine(), height);
				y += gap*gdGetLine() + height;
				
			}
		}
	}

	@Override
	public Size calculatePreferredSize(Component c) {
		Size size = new Size(0, 0);
		Size childPreferredSize;
		
		synchronized (c.getChildren()) {
			for (int i = 0; i < c.getChildren().size(); ++i) {
				childPreferredSize = c.getChild(i).getPreferredSize();
				
				if (i > 0) {
					size.height += gap*gdGetLine();
				}
				
				size.width = Math.max(size.width, childPreferredSize.width);
				size.height += childPreferredSize.height;
			}
		}
		
		size.width += 2 * margin*gdGetLine();
		size.height += 2 * margin*gdGetLine();
		
		return size;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + gap + ", " + margin + ")";
	}

}
