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

import ru.windcorp.tge2.util.Describable;

public abstract class ConfigurationNode extends Describable {
	
	public static final String ATTR_DESCRIPTION = "desc";
	
	private Element element = null;

	public ConfigurationNode(String name, String description) {
		super(name, description);
	}
	
	public Element getElement() {
		return element;
	}

	public synchronized void load(Element element) throws ConfigurationSyntaxException {
		this.element = element;
		
		if (element != null) {
			updateElement(getElement());
		}
		
		loadImpl();
	}
	
	protected abstract void loadImpl() throws ConfigurationSyntaxException;
	
	public Element createElement(Document doc) {
		Element element = doc.createElement(getName());
		
		updateElement(element);
		
		return element;
	}
	
	protected void updateElement(Element element) {
		element.setAttribute(ATTR_DESCRIPTION, getDescription());
	}

}
