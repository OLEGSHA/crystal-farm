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
import ru.windcorp.crystalfarm.graphics.GraphicsInterface;
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
		int sum = 0, maxSum = 0;
		
		for (char c : chars) {
			if (c == '\n') {
				if (maxSum < sum) {
					maxSum = sum;
				}
				sum = 0;
			} else {
				sum += getSymbol(c).getWidth();
			}
		}
		if (maxSum < sum) {
			maxSum = sum;
		}
		
		if (bold) {
			maxSum += chars.length;
		}
		
		return maxSum + 2;
	}
	
	public Size getSize(char[] chars, boolean bold) {
		Size result = new Size(0, getHeight());
		int maxWidth = 0;
		
		for (char c : chars) {
			if (c == '\n') {
				if (maxWidth < result.width) {
					maxWidth = result.width;
				}
				result.width = 0;
				result.height += getHeight();
			} else {
				result.width += getSymbol(c).getWidth();
			}
		}
		if (maxWidth < result.width) {
			maxWidth = result.width;
		}
		
		result.width = maxWidth + 2;
		if (bold) result.width += chars.length;
		result.height += 2;
		return result;
	}
	
	protected int render(FontSymbol symbol, int x, int y, boolean bold, Color color) {
		GraphicsInterface.drawTexture(x, y, symbol, 0, 0, color, Direction.UP);
		
		if (bold) {
			GraphicsInterface.drawTexture(x + 1, y, symbol, 0, 0, color, Direction.UP);
			return symbol.getWidth() + 1;
		} else {
			return symbol.getWidth();
		}
	}
	
	protected void renderUnstyled(char[] chars, int x, int y, boolean bold, Color color) {
		x++; y++;
		int startX = x;
		
		for (char c : chars) {
			if (c == '\n') {
				x = startX;
				y += getHeight();
			} else {
				x += render(getSymbol(c), x, y, bold, color);
			}
		}
	}
	
	public void render(char[] chars, int x, int y, boolean bold, FontStyle style, Color color) {
		if (color == null) color = Color.WHITE;
		
		switch (style) {
		case ENGRAVED:
			color.save();
			color.multiply(0.75);
			color.save();
			renderUnstyled(chars, x - 1, y - 1, bold, color.multiply(2));
			color.revert();
			//$FALL-THROUGH$
			
		case SHADOW:
			color.save();
			renderUnstyled(chars, x + 1, y + 1, bold, color.multiply(0.25));
			color.revert();
			break;
			
		default:
			break;
		}
		
		renderUnstyled(chars, x, y, bold, color);
		color.reset();
	}
	
	public void render(String str, int x, int y, boolean bold, FontStyle style, Color color) {
		render(str.toCharArray(), x, y, bold, style, color);
	}

}
