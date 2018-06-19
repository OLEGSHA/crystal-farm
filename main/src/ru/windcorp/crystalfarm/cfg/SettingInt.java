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
package ru.windcorp.crystalfarm.cfg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SettingInt extends ConfigurationNode {
	
	public static final String ATTR_MIN = "min";
	public static final String ATTR_MAX = "max";
	public static final String ATTR_STEP = "step";

	private int value;
	private int defaultValue;
	
	private final int max, min, step;
	
	public SettingInt(String name, String description, int defaultValue, int max, int min, int step) {
		super(name, description);
		
		if (min > max) {
			throw new IllegalArgumentException("Minimum " + min + " is greater than maximum " + max + " in setting " + getName());
		}
		
		if (defaultValue < min) {
			throw new IllegalArgumentException("Default value " + defaultValue + " is less than the minimum value " + min + " in setting " + getName());
		}
		
		if (defaultValue > max) {
			throw new IllegalArgumentException("Default value " + defaultValue + " is greater than the maximum value " + max + " in setting " + getName());
		}
		
		if (step <= 0) {
			throw new IllegalArgumentException("Step " + step + " is non-positive in setting " + getName());
		}
		
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.max = max;
		this.min = min;
		this.step = step;
	}

	public synchronized int get() {
		return this.value;
	}
	
	public synchronized void set(int value) {
		setRaw(value);
		
		if (getElement() != null) {
			getElement().setTextContent(Integer.toString(value));
		}
	}
	
	protected synchronized void setRaw(int value) {
		if (this.value == value) {
			return;
		}
		
		if (value < getMin()) {
			throw new IllegalArgumentException("Value " + value + " is less than minimum " + getMin() + " for setting " + getName());
		}
		if (value > getMax()) {
			throw new IllegalArgumentException("Value " + value + " is greater than maximum " + getMax() + " for setting " + getName());
		}
		
		this.value = value;
		fireEvent();
	}
	
	public synchronized int getDefaultValue() {
		return this.defaultValue;
	}
	
	public synchronized void setDefaultValue(int defaultValue) {
		if (value < getMin()) {
			throw new IllegalArgumentException("Value " + value + " is less than minimum " + getMin() + " for setting " + getName());
		}
		if (value > getMax()) {
			throw new IllegalArgumentException("Value " + value + " is greater than maximum " + getMax() + " for setting " + getName());
		}
		
		this.defaultValue = defaultValue;
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public int getStep() {
		return step;
	}

	@Override
	protected void loadImpl() throws ConfigurationSyntaxException {
		if (getElement() == null) {
			setRaw(getDefaultValue());
		} else {
			try {
				setRaw(Integer.parseInt(getElement().getTextContent()));
			} catch (NumberFormatException e) {
				throw new ConfigurationSyntaxException("\"" + getElement().getTextContent() + "\" is not an integer",
						e, this);
			} catch (IllegalArgumentException e) {
				throw new ConfigurationSyntaxException(getElement().getTextContent() + " is not a valid value integer",
						e, this);
			}
		}
	}
	
	@Override
	protected void updateElement(Element element) {
		super.updateElement(element);
		element.setAttribute(ATTR_MIN, Integer.toString(getMin()));
		element.setAttribute(ATTR_MAX, Integer.toString(getMax()));
		element.setAttribute(ATTR_STEP, Integer.toString(getStep()));
	}
	
	@Override
	public Element createElement(Document doc) {
		Element result = super.createElement(doc);
		result.setTextContent(Integer.toString(getDefaultValue()));
		return result;
	}

}
