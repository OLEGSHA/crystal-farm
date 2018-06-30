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

public class LayoutAlign implements Layout {

	private final int margin;
	private double alignX, alignY;
	
	public LayoutAlign(double alignX, double alignY, int margin) {
		this.alignX = alignX;
		this.alignY = alignY;
		this.margin = margin;
	}
	
	public LayoutAlign(int margin) {
		this(0.5, 0.5, margin);
	}
	
	public LayoutAlign() {
		this(1);
	}

	@Override
	public void layout(Component c) {
		c.getChildren().forEach(child -> {
			
			Size size = child.getPreferredSize();
			size.width = Math.min(size.width, c.getWidth() - 2*margin*gdGetLine());
			size.height = Math.min(size.height, c.getHeight() - 2*margin*gdGetLine());
			
			child.setBounds(
					c.getX() + (int) ((c.getWidth() - size.width) * alignX),
					c.getY() + (int) ((c.getHeight() - size.height) * alignY),
					size);
			
		});
	}

	@Override
	public Size calculatePreferredSize(Component c) {
		Size result = new Size(0, 0);
		
		c.getChildren().stream()
			.map(child -> child.getPreferredSize())
			.forEach(size -> {
				result.width = Math.max(size.width, result.width);
				result.height = Math.max(size.height, result.height);
			});
		
		result.width += 2 * margin*gdGetLine();
		result.height += 2 * margin*gdGetLine();
		
		return result;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + margin + ")";
	}

}
