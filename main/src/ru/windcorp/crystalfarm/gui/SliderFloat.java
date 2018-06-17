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
import ru.windcorp.tge2.util.StringUtil;
import ru.windcorp.tge2.util.debug.Log;

import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;
import static java.lang.Math.*;

public class SliderFloat extends ArrowedComponent implements GraphicsDesign {
	
	private class TStringDisplay extends TString {
		@Override
		protected String compute() {
			int signs = round(getValue() * power);
			return signs / power + "." + StringUtil.padToRight(abs(signs) % power + "", decimals, '0');
		}
		
		@Override
		public void update() {
			super.update();
		}
	}

	private final float min, max, step;
	private int multiplier;
	
	private int power;
	private int decimals;
	
	private final TStringDisplay display;

	public SliderFloat(String name, float min, float max, float step, float value, Consumer<?> action) {
		super(name, action, null);
		this.min = min;
		this.max = max;
		this.step = step;
		this.multiplier = (int) (value / step);
		this.decimals = max(0, (int) -ceil(log10(step)));
		this.power = pow10(decimals);
		Log.info(power + "");
		this.display = new TStringDisplay();
		getDisplay().setText(display.toFont());
	}
	
	private int pow10(int power) {
		int result = 1;
		for (int i = 0; i < power; ++i) {
			result *= 10;
		}
		return result;
	}

	public int getMultiplier() {
		return multiplier;
	}
	
	public float getValue() {
		return getStep() * multiplier;
	}
	
	public void setMultiplierSilently(int value) {
		if (getMultiplier() == value) {
			return;
		}
		this.multiplier = value;
		display.update();
	}
	
	public void setValueSilently(float value) {
		setMultiplierSilently((int) (value / getStep()));
	}
	
	public void setMultiplier(int value) {
		if (getMultiplier() == value) {
			return;
		}
		setMultiplierSilently(value);
		accept(null);
	}
	
	public void setValue(float value) {
		setMultiplier((int) (value / getStep()));
	}
	
	public float getMin() {
		return min;
	}
	
	public float getMax() {
		return max;
	}
	
	public float getStep() {
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
				(int) ceil((getWidth() - 2*LINE_THICKNESS) * ((getValue() - min) / (double) (max - min))),
				getHeight() - 2*LINE_THICKNESS,
				isHovered() ? BORDER_COLOR_LIGHTER : BORDER_COLOR);
	}

	@Override
	public void selectNext() {
		setMultiplier((int) min(getMax() / getStep(), multiplier + 1));
	}

	@Override
	public void selectPrevious() {
		setMultiplier((int) max(getMin() / getStep(), multiplier - 1));
	}
	
}
