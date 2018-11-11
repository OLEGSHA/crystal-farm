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

public abstract class ItemSlot {
	
	private ItemStack stack = null;

	public ItemStack getStack() {
		return stack;
	}

	void setStack(ItemStack stack) {
		ItemStack old = stack;
		this.stack = stack;
		onItemStackChanged(old, stack);
	}

	public boolean isEmpty() {
		return getStack() == null;
	}
	
	/**
	 * Checks whether the given item can be added to this slot and returns
	 * a brief, human-readable description of the problem otherwise.
	 * <p>
	 * This method influences the behavior of other methods unlike
	 * {@link #canAddItems()}. This method may allow addition of items
	 * even when UI-related methods do not.
	 */
	public String canAddItem(Item item) {
		return null;
	}
	
	public String canSetStack(ItemStack itemStack) {
		synchronized (itemStack.getItems()) {
			String problem;
			for (Item i : itemStack.getItems()) {
				if ((problem = canAddItem(i)) != null) {
					return problem;
				}
			}
			
			return null;
		}
	}
	
	/**
	 * Checks whether players can add items to this slot in any fashion.
	 * <p>
	 * This method only serves as a flag for UI and does not interfere
	 * with any of the methods. This method must be consulted before
	 * performing any user operation that may result in items being added
	 * to this slot including item stack changes. Such operations must be
	 * declined when this method returns <tt>false</tt>.
	 * <p>
	 * This method has lower priority than {@link #canInteract()}: this
	 * method may allow addition even when interaction is prohibited,
	 * operations must be declined in such cases.
	 * <p>
	 * The default implementation of this method allows all addition.
	 * 
	 * @return <tt>true</tt> when players can add items to this slot,
	 * <tt>false</tt> otherwise.
	 */
	public boolean canAddItems() {
		return true;
	}
	
	/**
	 * Checks whether this slot can be interacted with by the player.
	 * This method only serves as a flag for UI and does not interfere with
	 * any of the methods.
	 * <p>
	 * This method must be consulted before performing any user operation
	 * on this slot. Such operations must be declined when this method
	 * returns <tt>false</tt>.
	 * <p>
	 * The default implementation of this method allows all interaction.
	 * 
	 * @return <tt>true</tt> when player interaction with this slot is permitted,
	 * <tt>false</tt> otherwise.
	 */
	public boolean canInteract() {
		return true;
	}
	
	protected void onItemStackChanged(ItemStack old, ItemStack current) {
		// Do nothing
	}
	
}
