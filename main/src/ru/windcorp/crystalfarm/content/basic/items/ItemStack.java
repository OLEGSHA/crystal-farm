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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemStack {
	
	private final List<Item> contents = Collections.synchronizedList(new ArrayList<>());
	private final Item template;
	private WeakReference<ItemSlot> slot = null;
	
	public ItemStack(Item template) {
		this.template = template;
	}
	
	public List<Item> getItems() {
		return contents;
	}
	
	public boolean contains(Item item) {
		return getItems().contains(item);
	}
	
	void addItem0(Item item) {
		getItems().add(item);
		item.setStack(this);
	}
	
	void removeItem0(Item item) {
		getItems().remove(item);
		item.setStack(null);
	}
	
	public int getItemCount() {
		return getItems().size();
	}
	
	public String canAddItem(Item item) {
		if (item == null) {
			return "item is null";
		}
		
		synchronized (getItems()) {
			if (getItems().contains(item)) {
				return "item already present";
			}
			
			Item template = getTemplate();
			
			String problem;
			if ((problem = template.canStackWith(item, this)) != null) {
				return "[template] " + problem;
			}
			
			if ((problem = item.canStackWith(template, this)) != null) {
				return "[item] " + problem;
			}
			
			ItemSlot slot = getSlot();
			if (slot != null && (problem = slot.canAddItem(item)) != null) {
				return "[slot] " + problem;
			}
			
			return null;
		}
	}

	public Item getTemplate() {
		return template;
	}

	public ItemSlot getSlot() {
		if (this.slot == null) {
			return null;
		}
		
		return this.slot.get();
	}
	
	protected void setSlot(ItemSlot slot) {
		ItemSlot old = getSlot();
		this.slot = new WeakReference<>(slot);
		
		getItems().forEach(item -> item.onSlotChanged(old, slot));
	}

	private void checkSlot(ItemSlot slot) {
		if (slot != null) {
			if (slot.getStack() != this) {
				throw new IllegalStateException("ItemStack " + this + " is out of sync with its ItemSlot " +
						slot + ": ItemStack not present in slot annotated as its holder");
			}
		}
	}
	
	public String attemptMove(ItemSlot destination) {
		ItemSlot origin = getSlot();
		
		synchronized (origin) {
			synchronized (destination) {
				
				checkSlot(origin);
				
				if (destination.getStack() != null) {
					return "Destination stack is full";
				}
				
				String problem = destination.canSetStack(this);
				if (problem != null) {
					return problem;
				}
				
				origin.setStack(null);
				destination.setStack(this);
				return null;
			}
		}
		
		// TODO: move(), attemptInsert(), insert(), destroy(), ItemSlot.swap(ItemSlot, ItemSlot)
	}

}
