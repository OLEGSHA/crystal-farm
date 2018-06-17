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
package ru.windcorp.crystalfarm.gui;

import java.util.function.Consumer;

import ru.windcorp.crystalfarm.graphics.GraphicsDesign;
import ru.windcorp.crystalfarm.translation.TString;

import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;

public class SliderInt extends ArrowedComponent implements GraphicsDesign {
	
	private class TStringDisplay extends TString {
		@Override
		protected String compute() {
			return Integer.toString(getValue());
		}
		
		@Override
		public void update() {
			super.update();
		}
	}

	private final int min, max, step;
	private int value;
	
	private final TStringDisplay display;

	public SliderInt(String name, int min, int max, int step, int value, Consumer<?> action) {
		super(name, action, null);
		this.min = min;
		this.max = max;
		this.step = step;
		this.value = value;
		this.display = new TStringDisplay();
		getDisplay().setText(display.toFont());
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValueSilently(int value) {
		if (getValue() == value) {
			return;
		}
		this.value = value;
		display.update();
	}
	
	public void setValue(int value) {
		if (getValue() == value) {
			return;
		}
		setValueSilently(value);
		accept(null);
	}
	
	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}
	
	public int getStep() {
		return step;
	}
	
	@Override
	protected void renderSelf() {
		fillRectangle(
				getX(),
				getY(),
				getWidth(),
				getHeight(),
				isHovered() ? FOREGROUND_COLOR_LIGHTER : FOREGROUND_COLOR,
				isFocused() ? BORDER_COLOR_DARKER : BORDER_COLOR,
				LINE_THICKNESS);
		
		fillRectangle(
				getX() + LINE_THICKNESS,
				getY() + LINE_THICKNESS,
				(int) ((getWidth() - 2*LINE_THICKNESS) * ((value - min) / (double) (max - min))),
				getHeight() - 2*LINE_THICKNESS,
				isHovered() ? BORDER_COLOR_LIGHTER : BORDER_COLOR);
	}

	@Override
	public void selectNext() {
		setValue(Math.min(getMax(), getValue() + getStep()));
	}

	@Override
	public void selectPrevious() {
		setValue(Math.max(getMin(), getValue() - getStep()));
	}
	
}
