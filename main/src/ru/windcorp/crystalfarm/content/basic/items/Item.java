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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.ref.WeakReference;

import ru.windcorp.crystalfarm.logic.UpdateableModID;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.translation.TString;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public abstract class Item extends UpdateableModID {
	
	private TString name = null;
	
	private boolean isTickable = false;
	
	private WeakReference<ItemStack> stack = null;

	public Item(Mod mod, String id) {
		super(mod, id);
	}
	
	protected String getPrefix() {
		return "";
	}
	
	public String getResourceId() {
		return "item." + getMod().getName() + "." + getPrefix() + getRawId();
	}
	
	public TString getName() {
		return name;
	}
	
	public Item setName(TString name) {
		this.name = name;
		return this;
	}
	
	public Item setName(String name) {
		if (name == null) {
			setName(TString.translated(getResourceId() + ".name"));
		} else {
			setName(TString.translated(getResourceId() + ".name." + name));
		}
		
		return this;
	}
	
	public Item setDefaultName() {
		return setName((String) null);
	}
	
	public boolean isTickable() {
		return isTickable;
	}

	public void setTickable(boolean isTickable) {
		this.isTickable = isTickable;
	}
	
	public synchronized ItemStack getStack() {
		if (this.stack == null) {
			return null;
		}
		
		return this.stack.get();
	}
	
	synchronized void setStack(ItemStack stack) {
		ItemStack old = getStack();
		this.stack = new WeakReference<ItemStack>(stack);
		onStackChanged(old, stack);
	}
	
	private void checkStack(ItemStack stack) {
		if (stack != null) {
			if (!stack.contains(this)) {
				throw new IllegalStateException("Item " + this + " is out of sync with its ItemStack " +
						stack + ": item not present in stack annotated as its holder");
			}
		}
	}
	
	/**
	 * Attempts to move this item from its current ItemStack to <tt>destination</tt>.
	 * This item must be present in some ItemStack (<tt>getStack() != null</tt>).
	 * If the move is permitted, this method performs the move and returns <tt>null</tt>.
	 * If the move is not permitted, this method does nothing and returns a human-readable
	 * description of the problem.
	 * <p>
	 * Use this method in preference to {@link #move(ItemStack)} when a fail is expected to
	 * avoid exceptions. If the move is expected to succeed, use {@link #move(ItemStack)} to
	 * throw an exception when it does not.
	 * 
	 * @param destination ItemStack to attempt moving this item to
	 * 
	 * @return <tt>null</tt> in case of a successful transfer,
	 * {@link ItemStack#canAddItem(Item) destination.canAddItem(this)} otherwise
	 * 
	 * @throws IllegalStateException if this item is out of sync with its stack
	 * @throws NullPointerException if <tt>getStack() == null || destination == null</tt>
	 * 
	 * @see #move(ItemStack)
	 * @see #attemptInsert(ItemStack)
	 * @see #destroy()
	 */
	public synchronized String attemptMove(ItemStack destination) {
		ItemStack origin = getStack();

		// Implicit null-checks
		synchronized (origin.getItems()) {
			synchronized (destination.getItems()) {
				
				// Verify that we can leave our stack - crash otherwise, we should never be out of sync
				checkStack(origin);
				
				// Verify that we can enter destination - return nicely otherwise, we are attempting only
				String problem = destination.canAddItem(this);
				if (problem != null) {
					return problem;
				}
				
				// Move
				origin.removeItem0(this);
				destination.addItem0(this);
				return null;
			}
		}
	}
	
	/**
	 * Moves this item from its current ItemStack to <tt>destination</tt>.
	 * This item must be present in some ItemStack (<tt>getStack() != null</tt>).
	 * If the move is permitted, this method performs the move.
	 * If the move is not permitted, this method does nothing and throws an IllegalStackOperationException
	 * with a description of the problem.
	 * <p>
	 * Use this method in preference to {@link #attemptMove(ItemStack)} when a fail is not expected.
	 * If the move is expected to fail, use {@link #attemptMove(ItemStack)} to avoid using exceptions.
	 * 
	 * @param destination ItemStack to move this item to
	 * 
	 * @thorws IllegalStackOprationException if this item could not be inserted into <tt>destination</tt>
	 * @throws IllegalStateException if this item is out of sync with its stack
	 * @throws NullPointerException if <tt>getStack() == null || destination == null</tt>
	 * 
	 * @see #attemptMove(ItemStack)
	 * @see #insert(ItemStack)
	 * @see #destroy()
	 */
	public synchronized void move(ItemStack destination) {
		String problem = attemptMove(destination);
		if (problem != null) {
			throw new IllegalStackOperationException(
					"Moving failed: ItemStack " + destination + " has rejected item " + this + ": " + problem,
					destination,
					this);
		}
	}
	
	/**
	 * Attempts to insert this item into <tt>destination</tt>.
	 * This item must not be present in any other stack (<tt>getStack() == null</tt>).
	 * If the insertion is permitted, this method adds this item to the given ItemStack and returns <tt>null</tt>.
	 * If the insertion is not permitted, this method does nothing and returns a human-readable
	 * description of the problem.
	 * <p>
	 * Use this method in preference to {@link #insert(ItemStack)} when a fail is expected to
	 * avoid exceptions. If the insert is expected to succeed, use {@link #insert(ItemStack)} to
	 * throw an exception when it does not.
	 * 
	 * @param destination ItemStack to attempt inserting this item into
	 * 
	 * @return <tt>null</tt> in case of a successful insertion,
	 * {@link ItemStack#canAddItem(Item) destination.canAddItem(this)} otherwise
	 * 
	 * @throws IllegalStateException if this item is already present in a stack
	 * @throws NullPointerException if <tt>destination == null</tt>
	 * 
	 * @see #insert(ItemStack)
	 * @see #attemptMove(ItemStack)
	 * @see #destroy()
	 */
	public synchronized String attemptInsert(ItemStack destination) {
		ItemStack current = getStack();
		if (current != null) {
			throw new IllegalStateException(
					"Attempted to inject item " + this + 
					" already present in ItemStack " + current +
					" into ItemStack " + destination);
		}
		
		// Implicit null-check
		synchronized (destination.getItems()) {
			// Verify that we can enter destination - return nicely otherwise, we are attempting only
			String problem = destination.canAddItem(this);
			if (problem != null) {
				return problem;
			}
			
			// Move
			destination.addItem0(this);
			return null;
		}
	}
	
	/**
	 * Inserts this item into <tt>destination</tt>.
	 * This item must not be present in any other stack (<tt>getStack() == null</tt>).
	 * If the insertion is permitted, this method adds this item to the given ItemStack.
	 * If the insertion is not permitted, this method does nothing and throws an IllegalStackOperationException
	 * with a description of the problem.
	 * <p>
	 * Use this method in preference to {@link #attemptMove(ItemStack)} when a fail is not expected.
	 * If the move is expected to fail, use {@link #attemptMove(ItemStack)} to avoid using exceptions.
	 * 
	 * @param destination ItemStack to insert this item into
	 * 
	 * @throws IllegalStackOprationException if this item could not be inserted into <tt>destination</tt>
	 * @throws IllegalStateException if this item is out of sync with its stack
	 * @throws NullPointerException if <tt>destination == null</tt>
	 * 
	 * @see #attemptInsert(ItemStack)
	 * @see #attemptMove(ItemStack)
	 * @see #destroy()
	 */
	public synchronized void insert(ItemStack destination) {
		String problem = attemptInsert(destination);
		if (problem != null) {
			throw new IllegalStackOperationException(
					"Inserting failed: ItemStack " + destination + " has rejected item " + this + ": " + problem,
					destination,
					this);
		}
	}
	
	public synchronized void destroy() {
		ItemStack current = getStack();
		checkStack(current);
		if (current != null) {
			current.removeItem0(this);
		}
	}
	
	public ItemSlot getSlot() {
		ItemStack stack = getStack();
		if (stack == null) {
			return null;
		}
		
		return stack.getSlot();
	}
	
	public String canStackWith(Item item, ItemStack stack) {
		return equals(item) ? null : "item types do not match, expected " + getId();
	}

	protected Item clone() {
		return (Item) super.clone();
	}
	
	public final void render(int x, int y) {
		// Reserved for future use
		renderImpl(x, y);
	}
	
	protected abstract void renderImpl(int x, int y);
	
	public void tick(long length, long time) {
		// Do nothing
	}
	
	public void onUse(ItemUser user) {
		// Do nothing
	}
	
	protected void onStackChanged(ItemStack old, ItemStack current) {
		// Do nothing
	}
	
	protected void onSlotChanged(ItemSlot old, ItemSlot current) {
		// Do nothing
	}

	@Override
	public void read(DataInput input, int change) throws IOException, SyntaxException {
		// Do nothing
	}

	@Override
	public void write(DataOutput output, int change) throws IOException {
		// Do nothing
	}
	
	public String toDebugString() {
		return toString() + " (" + getId() + ")";
	}

}
