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

import java.util.Arrays;
import ru.windcorp.crystalfarm.gui.Component;
import ru.windcorp.crystalfarm.gui.Layout;
import ru.windcorp.crystalfarm.gui.Size;

public class LayoutGrid implements Layout {
	
	private class GridDimensions {
		int[] columns = new int[4];
		int[] rows = new int[10];
		boolean isSummed = false;
		
		void add(int column, int row, Size size) {
			if (isSummed) throw new IllegalStateException("Already summed");
			columns = update(columns, column, size.width);
			rows = update(rows, row, size.height);
		}

		private int[] update(int[] array, int index, int value) {
			if (array.length <= index) {
				array = Arrays.copyOf(array, ((index / 10) + 1) * 10);
			}
			
			if (array[index] < value) {
				array[index] = value;
			}
			
			return array;
		}
		
		Size getBounds() {
			if (isSummed) throw new IllegalStateException("Already summed");
			Size result = new Size(2*margin*gdGetLine() - gap*gdGetLine(), 2*margin*gdGetLine() - gap*gdGetLine());
			
			for (int i = 0; i < columns.length; ++i) {
				if (columns[i] != 0) {
					result.width += columns[i] + gap*gdGetLine();
				}
			}
			
			for (int i = 0; i < rows.length; ++i) {
				if (rows[i] != 0) {
					result.height += rows[i] + gap*gdGetLine();
				}
			}
			
			return result;
		}
		
		void sum() {
			if (isSummed) throw new IllegalStateException("Already summed");
			
			int accumulator = margin*gdGetLine();
			int buffer;
			
			for (int i = 0; i < columns.length; ++i) {
				buffer = columns[i];
				columns[i] = accumulator;
				accumulator += buffer + gap*gdGetLine();
			}
			
			accumulator = margin*gdGetLine();
			
			for (int i = 0; i < rows.length; ++i) {
				buffer = rows[i];
				rows[i] = accumulator;
				accumulator += buffer + gap*gdGetLine();
			}
			
			isSummed = true;
		}
		
		void setBounds(int column, int row, Component child, Component parent) {
			if (!isSummed) throw new IllegalStateException("Not summed yet");
			
			child.setBounds(
					parent.getX() + columns[column],
					parent.getY() + rows[row],
					
					(column != (columns.length-1) ?
						(columns[column + 1] - columns[column] - gap*gdGetLine()) :
						(parent.getWidth() - margin*gdGetLine() - columns[column])),
						
					(row != (rows.length-1) ?
						(rows[row + 1] - rows[row] - gap*gdGetLine()) :
						(parent.getHeight() - margin*gdGetLine() - rows[row]))
					);
		}
	}

	private int gap, margin;
	
	public LayoutGrid(int margin, int gap) {
		this.margin = margin;
		this.gap = gap;
	}
	
	public LayoutGrid(int gap) {
		this(gap, gap);
	}
	
	public LayoutGrid() {
		this(1);
	}

	@Override
	public void layout(Component c) {
		synchronized (c.getChildren()) {
			GridDimensions grid = calculateGrid(c);
			grid.sum();
			
			int[] coords;
			for (Component child : c.getChildren()) {
				coords = (int[]) child.getLayoutHint();
				grid.setBounds(coords[0], coords[1], child, c);
			}
		}
	}

	@Override
	public Size calculatePreferredSize(Component c) {
		synchronized (c.getChildren()) {
			return calculateGrid(c).getBounds();
		}
	}
	
	private GridDimensions calculateGrid(Component parent) {
		GridDimensions result = new GridDimensions();
		int[] coords;
		
		for (Component child : parent.getChildren()) {
			coords = (int[]) child.getLayoutHint();
			result.add(coords[0], coords[1], child.getPreferredSize());
		}
		
		return result;
	}

}
