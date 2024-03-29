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
package ru.windcorp.crystalfarm.graphics.fonts;

import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.gui.Size;
import ru.windcorp.crystalfarm.util.Direction;
import ru.windcorp.tge2.util.Nameable;

public class Font extends Nameable {

	private final FontSymbol[][] symbols;
	private final FontSymbol unknown;
	
	private final int height;
	
	public Font(String name, FontSymbol[][] symbols, FontSymbol unknown, int height) {
		super(name);
		this.symbols = symbols;
		this.unknown = unknown;
		this.height = height;
	}

	public FontSymbol[][] getSymbols() {
		return symbols;
	}
	
	public FontSymbol getUnknownSymbol() {
		return unknown;
	}
	
	public int getHeight() {
		return height;
	}
	
	public FontSymbol getSymbol(char c) {
		FontSymbol result = null;
		
		if (getSymbols()[c / 0x100] != null) {
				result = (getSymbols()[c / 0x100])[c % 0x100];
		}
		
		return result == null ? getUnknownSymbol() : result;
	}
	
	public int getLength(char[] chars, boolean bold) {
		return getLength(chars, 0, chars.length, bold);
	}
	
	public int getLength(char[] chars, int offset, int length, boolean bold) {
		int sum = 0, maxSum = 0;
		
		for (int i = offset; i < length; ++i) {
			if (chars[i] == '\n') {
				if (maxSum < sum) {
					maxSum = sum;
				}
				sum = 0;
			} else {
				sum += getSymbol(chars[i]).getUsableWidth() + (bold ? 1 : 0);
			}
		}
		if (maxSum < sum) {
			maxSum = sum;
		}
		
		return maxSum + 2;
	}
	
	public Size getSize(char[] chars, boolean bold) {
		return getSize(chars, 0, chars.length, bold);
	}
	
	public Size getSize(char[] chars, int offset, int length, boolean bold) {
		Size result = new Size(0, getHeight());
		int maxWidth = 0;

		for (int i = offset; i < length; ++i) {
			if (chars[i] == '\n') {
				if (maxWidth < result.width) {
					maxWidth = result.width;
				}
				result.width = 0;
				result.height += getHeight();
			} else {
				result.width += getSymbol(chars[i]).getUsableWidth() + (bold ? 1 : 0);
			}
		}
		if (maxWidth < result.width) {
			maxWidth = result.width;
		}
		
		result.width = maxWidth + 2;
		result.height += 2;
		return result;
	}
	
	protected int render(FontSymbol symbol, int x, int y, boolean bold, Color color) {
		symbol.render(x, y, color, Direction.UP);
		
		if (bold) {
			symbol.render(x + 1, y, color, Direction.UP);
			return symbol.getUsableWidth() + 1;
		} else {
			return symbol.getUsableWidth();
		}
	}
	
	protected void renderUnstyled(char[] chars, int offset, int length, int x, int y, boolean bold, Color color) {
		x++; y++;
		int startX = x;

		for (int i = offset; i < length; ++i) {
			if (chars[i] == '\n') {
				x = startX;
				y += getHeight();
			} else {
				x += render(getSymbol(chars[i]), x, y, bold, color);
			}
		}
	}

	public void render(char[] chars, int x, int y, boolean bold, FontStyle style, Color color) {
		render(chars, 0, chars.length, x, y, bold, style, color);
	}
	
	public void render(char[] chars, int offset, int length, int x, int y, boolean bold, FontStyle style, Color color) {
		if (color == null) color = Color.WHITE;
		
		switch (style) {
		case ENGRAVED:
			color.save();
			color.multiply(0.75);
			color.save();
			renderUnstyled(chars, offset, length, x - 1, y - 1, bold, color.multiply(2));
			color.revert();
			//$FALL-THROUGH$
			
		case SHADOW:
			color.save();
			renderUnstyled(chars, offset, length, x + 1, y + 1, bold, color.multiply(0.25));
			color.revert();
			break;
			
		default:
			break;
		}
		
		renderUnstyled(chars, offset, length, x, y, bold, color);
		color.reset();
	}
	
	public void render(String str, int x, int y, boolean bold, FontStyle style, Color color) {
		render(str.toCharArray(), x, y, bold, style, color);
	}

}
