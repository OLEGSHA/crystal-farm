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

import ru.windcorp.crystalfarm.gui.listener.ComponentMouseButtonInputListener;
import ru.windcorp.crystalfarm.translation.TString;

import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;
import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.*;

public class SliderInt extends ArrowedComponent {
	
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
		
		addInputListener((ComponentMouseButtonInputListener) (comp, input) -> {
			if (input.isPressed() && input.isLeftButton()) {
				setValue(getMin() + getStep() * (int) Math.round(((double) getMax() - getMin()) * (input.getCursorX() - getX()) / getWidth() / getStep()));
				input.consume();
			}
		});
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
				isHovered() ? gdGetForegroundAltColor() : gdGetForegroundColor(),
				isFocused() ? gdGetBorderFocusedColor() : gdGetBorderColor(),
				gdGetLine());
		
		fillRectangle(
				getX() + gdGetLine(),
				getY() + gdGetLine(),
				(int) ((getWidth() - 2*gdGetLine()) * ((value - min) / (double) (max - min))),
				getHeight() - 2*gdGetLine(),
				isHovered() ? gdGetBorderHoveredColor() : gdGetBorderColor());
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
