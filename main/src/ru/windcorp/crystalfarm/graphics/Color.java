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

import ru.windcorp.tge2.util.NumberUtil;
import ru.windcorp.tge2.util.StringUtil;

/**
 * Represents a color in RGBA color model. The values are stored as {@code double}s.
 */
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
	
	/**
	 * Maximum stack size
	 */
	public static final int STACK_SIZE = 4;
	private static final int STACK_R = 0, STACK_G = 1, STACK_B = 2, STACK_A = 3;
	
	/**
	 * The red channel of this color in RGBA color model. Values are between 0 and 1.
	 */
	public double r;
	/**
	 * The green channel of this color in RGBA color model. Values are between 0 and 1.
	 */
	public double g;
	/**
	 * The blue channel of this color in RGBA color model. Values are between 0 and 1.
	 */
	public double b;
	/**
	 * The alpha (transparency) channel of this color in RGBA color model. Values are between 0 and 1, where 1 is fully opaque.
	 */
	public double a;
	
	private double[][] stack = null;
	private int stackPointer = 0;
	
	/**
	 * Creates a new color with the given RGBA channels.
	 * @param r the red channel. Values are between 0 and 1
	 * @param g the green channel. Values are between 0 and 1.
	 * @param b the blue channel. Values are between 0 and 1.
	 * @param a the alpha (transparency) channel. Values are between 0 and 1, where 1 is fully opaque
	 */
	public Color(double r, double g, double b, double a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	/**
	 * Creates a new color with the given RGBA integer.
	 * The 32-bit integer must have the following layout:
	 * {@code 0xRRGGBBAA}.
	 * @param rgba the RGBA integer
	 */
	public Color(int rgba) {
		this(
				(rgba >>> (3*Byte.SIZE) & 0xFF) / 256.0,
				(rgba >>> (2*Byte.SIZE) & 0xFF) / 256.0,
				(rgba >>> (1*Byte.SIZE) & 0xFF) / 256.0,
				(rgba 					& 0xFF) / 256.0);
	}
	
	/**
	 * Creates a new color with the given hue-saturation-brightness-alpha (HSBA) coordinates.
	 * @param hue the hue of the color, between 0 and 1 (mapping to 0 through 360 degrees respectively)
	 * @param saturation the saturation of the color, between 0 and 1
	 * @param brightness the brightness of the color, between 0 and 1 (at 1 the color is most saturated, not white)
	 * @param alpha the alpha channel, between 0 and 0xFF (0xFF is fully opaque)
	 * @return a new {@link Color} representing the given value.
	 */
	public static Color fromHsba(float hue, float saturation, float brightness, int alpha) {
		return new Color(java.awt.Color.HSBtoRGB(hue, saturation, brightness) << Byte.SIZE | (alpha & 0xFF));
	}
	
	/**
	 * Creates a new color with the given hue-saturation-brightness-alpha (HSBA) coordinates.
	 * @param hue the hue of the color, between 0 and 0xFF (mapping to 0 through 360 degrees respectively)
	 * @param saturation the saturation of the color, between 0 and 0xFF
	 * @param brightness the brightness of the color, between 0 and 0xFF (at 0xFF the color is most saturated, not white)
	 * @param alpha the alpha channel, between 0 and 0xFF (0xFF is fully opaque)
	 * @return a new {@link Color} representing the given value.
	 */
	public static Color fromHsba(int hue, int saturation, int brightness, int alpha) {
		return fromHsba(hue / 256f, saturation / 256f, brightness / 256f, alpha);
	}
	
	/**
	 * Changes this color to its grayscale counterpart. RGB channels are averaged.
	 * @return this color
	 * @see {@link #save()} - to save current color state so it can be reverted to later
	 * @see {@link #clone()} - to create a copy of this color independent of this color
	 */
	public Color toGray() {
		r = g = b = (r + g + b) / 3;
		return this;
	}
	
	/**
	 * Multiplies the RGB channels of this color by {@code coef}. This methods
	 * effectively changes the color's brightness.
	 * @return this color
	 * @see {@link #save()} - to save current color state so it can be reverted to later
	 * @see {@link #clone()} - to create a copy of this color independent of this color
	 */
	public Color multiply(double coef) {
		r *= coef;
		g *= coef;
		b *= coef;
		return this;
	}
	
	/**
	 * Clones this color, creating its exact copy. Clone's stack is cleared.
	 * @return a new independent color initialized with this color's RGBA values
	 * @see {@link #save()}
	 */
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
	
	/**
	 * Saves the current RGBA values of this color in an memory- and performance-efficient manner.
	 * These values can be reverted back to with {@link #revert()}.
	 * <p>
	 * {@code Color}s have a <i>value stack</i>. The head of this stack is the color's actual RGBA value.
	 * The value stack has limited maximum size (see {@link #STACK_SIZE}). {@link #save()} pushes the
	 * current value into the stack. {@link #revert()} pops one value from the stack and sets it actual.
	 * {@link reset()} clears the stack setting the actual value to the one on the bottom of the stack.
	 * 
	 * @return this color
	 * @see {@link #clone()}
	 * @see {@link #revert()}
	 */
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
	
	/**
	 * Restores RGBA values previously saved with {@link #save()}.
	 * <p>
	 * {@code Color}s have a <i>value stack</i>. The head of this stack is the color's actual RGBA value.
	 * The value stack has limited maximum size (see {@link #STACK_SIZE}). {@link #save()} pushes the
	 * current value into the stack. {@link #revert()} pops one value from the stack and sets it actual.
	 * {@link reset()} clears the stack setting the actual value to the one on the bottom of the stack.
	 * 
	 * @return this color
	 * @see {@link #clone()}
	 * @see {@link #save()}
	 */
	public Color revert() {
		stackPointer--;
		r = stack[stackPointer][STACK_R];
		g = stack[stackPointer][STACK_G];
		b = stack[stackPointer][STACK_B];
		a = stack[stackPointer][STACK_A];
		
		return this;
	}
	
	/**
	 * Resets RGBA values to the first ones saved with {@link #save()} and cleans value stack.
	 * <p>
	 * {@code Color}s have a <i>value stack</i>. The head of this stack is the color's actual RGBA value.
	 * The value stack has limited maximum size (see {@link #STACK_SIZE}). {@link #save()} pushes the
	 * current value into the stack. {@link #revert()} pops one value from the stack and sets it actual.
	 * {@link reset()} clears the stack setting the actual value to the one on the bottom of the stack.
	 * 
	 * @return this color
	 * @see {@link #clone()}
	 * @see {@link #save()}
	 * @see {@link #revert()}
	 */
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
	
	@Override
	public String toString() {
		return r + "," + b + "," + g + "," + a;
	}
	
	public String toRGBAString() {
		return new String(NumberUtil.toFullHex(getRGBA()));
	}
	
	public int getRGBA() {
		return
				((int) (r * 0xFF) << 3*Byte.SIZE) |
				((int) (g * 0xFF) << 2*Byte.SIZE) |
				((int) (b * 0xFF) << 1*Byte.SIZE) |
				((int) (a * 0xFF) << 0*Byte.SIZE);
	}
	
	public static Color parse(String declar) throws IllegalArgumentException {
		if (declar.startsWith("0x")) {
			try {
				return new Color((int) (Long.parseLong(declar.substring("0x".length()), 0x10) & Integer.MAX_VALUE));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Could not parse \"" + declar + "\" as hex RGBA", e);
			}
		}
		
		String[] parts = StringUtil.split(declar, ',');
		if (parts.length != 4) {
			throw new IllegalArgumentException("Could not parse \"" + declar + "\" as double RGBA: incorrect value amount");
		}
		
		try {
			return new Color(
					Double.parseDouble(parts[0].trim()),
					Double.parseDouble(parts[1].trim()),
					Double.parseDouble(parts[2].trim()),
					Double.parseDouble(parts[3].trim())
			);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Could not parse \"" + declar + "\" as double RGBA: not a double", e);
		}
	}
	
}
