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

import java.util.Collection;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Section extends ConfigurationNode {
	
	private final SortedMap<String, ConfigurationNode> nodes = Collections.synchronizedSortedMap(new TreeMap<>());

	public Section(String name, String description) {
		super(name, description);
	}

	public SortedMap<String, ConfigurationNode> getNodeMap() {
		return nodes;
	}
	
	public Collection<ConfigurationNode> getNodes() {
		return getNodeMap().values();
	}
	
	public Section add(ConfigurationNode node) {
		getNodeMap().put(node.getName(), node);
		if (getElement() != null) loadNode(node);
		
		fireEvent();
		
		return this;
	}
	
	public Section add(ConfigurationNode... nodes) {
		for (ConfigurationNode node : nodes) {
			add(node);
		}
		
		return this;
	}
	
	public Section remove(ConfigurationNode node) {
		getNodeMap().remove(node.getName());
		fireEvent();
		
		return this;
	}
	
	public ConfigurationNode get(String name) {
		return getNodeMap().get(name);
	}
	
	public Section getSection(String name) {
		ConfigurationNode node = get(name);
		
		if (node instanceof Section) {
			return (Section) node;
		}
		
		return null;
	}
	
	public <C> C get(String name, Class<C> clazz) {
		ConfigurationNode node = get(name);
		
		if (clazz.isInstance(clazz)) {
			return clazz.cast(node);
		}
		
		return null;
	}

	@Override
	protected void loadImpl() throws ConfigurationSyntaxException {
		if (getElement() == null) {
			getNodes().forEach(node -> node.load(null));
		} else {
			getNodes().forEach(node -> loadNode(node));
		}
	}
	
	protected void loadNode(ConfigurationNode node) {
		for (Node next = getElement().getFirstChild(); next != null; next = next.getNextSibling()) {
			if (next instanceof Element && ((Element) next).getTagName().equals(node.getName())) {
				node.load((Element) next);
				return;
			}
		}
		
		Element element = node.createElement(getElement().getOwnerDocument());
		getElement().appendChild(element);
		node.load(element);
	}

}
