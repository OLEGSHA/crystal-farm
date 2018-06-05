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
package ru.windcorp.crystalfarm.graphics;

public class Color {
	
	public static final Color
			BLACK				= new Color(0, 0, 0, 0),
			WHITE				= new Color(1, 1, 1, 0),
			TRANSPARENT			= new Color(0, 0, 0, 1),
			RED					= new Color(1, 0, 0, 0),
			GREEN				= new Color(0, 1, 0, 0),
			BLUE				= new Color(0, 0, 1, 0);
	
	public double r, g, b, a;
	
	public Color(double r, double g, double b, double a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public void copyFrom(Color other) {
		r = other.r;
		g = other.g;
		b = other.b;
		a = other.a;
	}
	
}
