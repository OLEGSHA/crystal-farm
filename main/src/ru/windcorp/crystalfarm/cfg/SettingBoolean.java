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

public class SettingBoolean extends ConfigurationNode {

	private boolean value;
	private boolean defaultValue;

	public SettingBoolean(String name, String description, boolean defaultValue) {
		super(name, description);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}
	
	public synchronized boolean get() {
		return this.value;
	}
	
	public synchronized void set(boolean value) {
		setRaw(value);
		
		if (getElement() != null) {
			getElement().setTextContent(Boolean.toString(value));
		}
	}
	
	protected synchronized void setRaw(boolean value) {
		this.value = value;
		fireEvent();
	}
	
	public synchronized boolean getDefaultValue() {
		return this.defaultValue;
	}
	
	public synchronized void setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	protected void loadImpl() throws ConfigurationSyntaxException {
		if (getElement() == null) {
			setRaw(getDefaultValue());
		} else {
			setRaw(Boolean.parseBoolean(getElement().getTextContent()));
		}
	}
	
	@Override
	public Element createElement(Document doc) {
		Element result = super.createElement(doc);
		result.setTextContent(Boolean.toString(getDefaultValue()));
		return result;
	}

}
