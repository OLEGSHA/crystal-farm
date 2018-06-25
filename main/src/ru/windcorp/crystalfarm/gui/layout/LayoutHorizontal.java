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

public class LayoutHorizontal implements Layout {
	
	private final int margin, gap;

	public LayoutHorizontal(int margin, int gap) {
		this.margin = margin;
		this.gap = gap;
	}
	
	public LayoutHorizontal(int gap) {
		this(gap, gap);
	}
	
	public LayoutHorizontal() {
		this(1);
	}

	@Override
	public void layout(Component c) {
		int x = c.getX() + margin*gdGetLine(),
			y = c.getY() + margin*gdGetLine();
		
		int width;
		
		synchronized (c.getChildren()) {
			for (Component child : c.getChildren()) {
				
				width = child.getPreferredSize().width;
				child.setBounds(x, y, width, c.getHeight() - 2 * margin*gdGetLine());
				x += gap*gdGetLine() + width;
				
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
					size.width += gap*gdGetLine();
				}
				
				size.height = Math.max(size.height, childPreferredSize.height);
				size.width += childPreferredSize.width;
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
