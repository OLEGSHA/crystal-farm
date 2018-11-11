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
package ru.windcorp.crystalfarm.content.basic.items;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class ItemRegistry {
	
	private static final ItemRegistry INST = new ItemRegistry();
	
	public static ItemRegistry getRegistry() {
		return INST;
	}

	private final Map<String, Item> itemMap = Collections.synchronizedMap(new HashMap<>());
	
	private ItemRegistry() {
		// Do nothing
	}
	
	protected Map<String, Item> getItemMap() {
		return itemMap;
	}
	
	public Collection<Item> getItems() {
		return itemMap.values();
	}
	
	public Item get(String id) {
		return getItemMap().get(id);
	}
	
	public boolean exists(String id) {
		return getItemMap().containsKey(id);
	}
	
	public void register(Item item) {
		synchronized (getItemMap()) {
			if (getItemMap().containsKey(item.getId())) {
				ExecutionReport.reportError(null, null, "Item with ID %s has already been registered in %s", item.getId(), this.toString());
				// Continue
			}
			
			getItemMap().put(item.getId(), item);
		}
	}
	
	public Item createNew(String id) {
		Item template = get(id);
		if (template == null) {
			ExecutionReport.reportError(null, null, "Item with ID %s has not been registered in %s", id, this.toString());
			return null;
		}
		return template.clone();
	}
	
	@Override
	public String toString() {
		return "the Item Registry";
	}

}
