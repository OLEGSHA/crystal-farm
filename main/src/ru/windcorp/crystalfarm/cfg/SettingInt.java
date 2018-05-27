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

	private int value;
	private int defaultValue;

	public SettingInt(String name, String description, int defaultValue) {
		super(name, description);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
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
		this.value = value;
		fireEvent();
	}
	
	public synchronized int getDefaultValue() {
		return this.defaultValue;
	}
	
	public synchronized void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
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
			}
		}
	}
	
	@Override
	public Element createElement(Document doc) {
		Element result = super.createElement(doc);
		result.setTextContent(Integer.toString(getDefaultValue()));
		return result;
	}

}
