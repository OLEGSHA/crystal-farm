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

import ru.windcorp.crystalfarm.graphics.GraphicsDesign;
import ru.windcorp.crystalfarm.gui.Component;
import ru.windcorp.crystalfarm.gui.Layout;
import ru.windcorp.crystalfarm.gui.Size;

public class LayoutCenter implements Layout {

	private final int margin;
	
	public LayoutCenter(int margin) {
		this.margin = margin;
	}
	
	public LayoutCenter() {
		this(GraphicsDesign.LINE_THICKNESS);
	}

	@Override
	public void layout(Component c) {
		c.getChildren().forEach(child -> {
			
			Size size = child.getPreferredSize();
			
			child.setBounds(
					c.getX() + (c.getWidth() - size.width) / 2,
					c.getY() + (c.getHeight() - size.height) / 2,
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
		
		result.width += 2 * margin;
		result.height += 2 * margin;
		
		return result;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + margin + ")";
	}

}
