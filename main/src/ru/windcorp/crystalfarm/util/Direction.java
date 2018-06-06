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
package ru.windcorp.crystalfarm.util;

public enum Direction {
	
	UP, LEFT, DOWN, RIGHT;
	
	@SuppressWarnings("incomplete-switch")
	public void turnCoordiates(double[] coords) {
		assert coords.length == 4;
		
		if (this == UP) return;
		
		double buf;
		
		switch (this) {
		case LEFT:	// Shift by 1
			      buf = coords[0];
			coords[0] = coords[1];
			coords[1] = coords[2];
			coords[2] = coords[3];
			coords[3] = buf;
			return;
			
		case DOWN:	// Shift by 2
			      buf = coords[0];
			coords[0] = coords[2];
			coords[2] = buf;
			      buf = coords[1];
			coords[1] = coords[3];
			coords[3] = buf;
			return;
			
		case RIGHT:	// Shift by 3
			      buf = coords[3];
			coords[3] = coords[2];
			coords[2] = coords[1];
			coords[1] = coords[0];
			coords[0] = buf;
			return;
		}
	}

}
