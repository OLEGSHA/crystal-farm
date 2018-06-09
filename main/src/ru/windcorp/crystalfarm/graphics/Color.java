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

public class Color implements Cloneable {
	
	public static final Color
			BLACK				= new Color(  0,   0,   0,   1),
			WHITE				= new Color(  1,   1,   1,   1),
			TRANSPARENT			= new Color(  0,   0,   0,   0),
			RED					= new Color(  1,   0,   0,   1),
			GREEN				= new Color(  0,   1,   0,   1),
			BLUE				= new Color(  0,   0,   1,   1),
			BRIGHT_YELLOW		= new Color(  1,   1,   0,   1),
			BRIGHT_CYAN			= new Color(  0,   1,   1,   1),
			BRIGHT_MAGENTA		= new Color(  1,   0,   1,   1),
			GRAY				= new Color(0.5, 0.5, 0.5,   1),
			LIGHT_GRAY			= new Color(.75, .75, .75,   1),
			DARK_GRAY			= new Color(.25, .25, .25,   1);
	
	public static final int STACK_SIZE = 4;
	private static final int STACK_R = 0, STACK_G = 1, STACK_B = 2, STACK_A = 3;
	
	public double r, g, b, a;
	
	private double[][] stack = null;
	private int stackPointer = 0;
	
	public Color(double r, double g, double b, double a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color(int rgba) {
		this(
				(rgba >>> (3*Byte.SIZE) & 0xFF) / 256.0,
				(rgba >>> (2*Byte.SIZE) & 0xFF) / 256.0,
				(rgba >>> (1*Byte.SIZE) & 0xFF) / 256.0,
				(rgba 					& 0xFF) / 256.0);
	}

	public void copyFrom(Color other) {
		r = other.r;
		g = other.g;
		b = other.b;
		a = other.a;
	}
	
	public Color toGray() {
		r = g = b = (r + g + b) / 3;
		return this;
	}
	
	public Color multiply(double coef) {
		r *= coef;
		g *= coef;
		b *= coef;
		return this;
	}
	
	public Color clone() {
		try {
			Color clone = (Color) super.clone();
			clone.stack = null;
			clone.stackPointer = 0;
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	public Color save() {
		if (stack == null) {
			stack = new double[STACK_SIZE][];
		}
		
		if (stack[stackPointer] == null) {
			stack[stackPointer] = new double[4];
		}
		
		stack[stackPointer][STACK_R] = r;
		stack[stackPointer][STACK_G] = g;
		stack[stackPointer][STACK_B] = b;
		stack[stackPointer][STACK_A] = a;
		stackPointer++;
		
		return this;
	}
	
	public Color revert() {
		stackPointer--;
		r = stack[stackPointer][STACK_R];
		g = stack[stackPointer][STACK_G];
		b = stack[stackPointer][STACK_B];
		a = stack[stackPointer][STACK_A];
		
		return this;
	}
	
	public Color reset() {
		if (stack == null || stackPointer == 0) {
			return this;
		}
		
		stackPointer = 0;
		r = stack[0][STACK_R];
		g = stack[0][STACK_G];
		b = stack[0][STACK_B];
		a = stack[0][STACK_A];
		
		return this;
	}
	
}
