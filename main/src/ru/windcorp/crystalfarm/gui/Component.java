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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.gui.listener.ComponentFocusListener;
import ru.windcorp.crystalfarm.gui.listener.ComponentHierarchyListener;
import ru.windcorp.crystalfarm.gui.listener.ComponentInputListener;
import ru.windcorp.crystalfarm.input.Input;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.tge2.util.Nameable;

public class Component extends Nameable {

	private final List<Component> children = Collections.synchronizedList(new ArrayList<>());
	private WeakReference<Component> parent = new WeakReference<>(null);
	
	private final Collection<ComponentFocusListener> focusListeners = Collections.synchronizedList(new ArrayList<>());
	private final Collection<ComponentHierarchyListener> hierarchyListeners = Collections.synchronizedList(new ArrayList<>());
	private final Collection<ComponentInputListener<?>> inputListeners = Collections.synchronizedList(new ArrayList<>());
	
	private int x, y;
	private int width, height;
	private boolean valid = false;
	
	private Size preferredSize = null;
	
	private Object layoutHint = null;
	private Layout layout = null;
	
	private boolean isFocusable = false;
	private boolean isFocused = false;
	
	private boolean isHovered = false;

	public Component(String name) {
		super(name);
	}
	
	public Component getParent() {
		return parent.get();
	}
	
	protected void setParent(Component parent) {
		this.parent = new WeakReference<>(parent);
	}
	
	public List<Component> getChildren() {
		return children;
	}
	
	public Component getChild(int index) {
		synchronized (getChildren()) {
			if (index < 0 || index >= getChildren().size()) return null;
			return getChildren().get(index);
		}
	}
	
	public int getChildIndex(Component child) {
		return getChildren().indexOf(child);
	}
	
	public int getOwnIndex() {
		Component parent = getParent();
		if (parent != null) {
			return parent.getChildIndex(this);
		}
		
		return -1;
	}
	
	public void moveChild(Component child, int newIndex) {
		if (newIndex == -1) newIndex = getChildren().size() - 1;
		
		if (getChildren().remove(child)) {
			getChildren().add(newIndex, child);
			invalidate();
		}
	}
	
	public void moveSelf(int newIndex) {
		Component parent = getParent();
		if (parent != null) {
			parent.moveChild(this, newIndex);
		}
	}
	
	public void addChild(Component child, int index) {
		if (index == -1) index = getChildren().size();

		invalidate();
		getChildren().add(index, child);
		child.setParent(this);
		getHierarchyListeners().forEach(listener -> listener.onChildAdded(this, child));
	}
	
	public void addChild(Component child) {
		addChild(child, -1);
	}
	
	public void removeChild(Component child) {
		if (!getChildren().contains(child)) {
			return;
		}
		
		if (child.isFocused()) {
			child.focusNext();
		}

		invalidate();
		getChildren().remove(child);
		child.setParent(null);
		getHierarchyListeners().forEach(listener -> listener.onChildRemoved(this, child));
	}
	
	public synchronized int getX() {
		return x;
	}
	
	public synchronized int getY() {
		return y;
	}
	
	public synchronized void setPosition(int x, int y) {
		invalidate();
		this.x = x;
		this.y = y;
	}
	
	public synchronized int getWidth() {
		return width;
	}
	
	public synchronized int getHeight() {
		return height;
	}
	
	public synchronized void setSize(int width, int height) {
		invalidate();
		this.width = width;
		this.height = height;
	}
	
	public void setSize(Size size) {
		setSize(size.width, size.height);
	}
	
	public synchronized void setBounds(int x, int y, int width, int height) {
		setPosition(x, y);
		setSize(width, height);
	}
	
	public void setBounds(int x, int y, Size size) {
		setBounds(x, y, size.width, size.height);
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public synchronized void invalidate() {
		valid = false;
		getChildren().forEach(child -> child.invalidate());
	}
	
	public synchronized void validate() {
		Component parent = getParent();
		
		if (isValid() || parent == null) {
			invalidate();
			layoutSelf();
		} else {
			parent.validate();
		}
	}
	
	public synchronized void layoutSelf() {
		if (getLayout() != null) {
			getLayout().layout(this);
		}
		
		getChildren().forEach(child -> {
			child.layoutSelf();
		});
		
		valid = true;
	}
	
	public synchronized Size getPreferredSize() {
		if (preferredSize != null) {
			return preferredSize;
		}
		
		if (getLayout() != null) {
			return getLayout().calculatePreferredSize(this);
		}
		
		return null;
	}
	
	public synchronized void setPreferredSize(Size preferredSize) {
		this.preferredSize = preferredSize;
	}
	
	public void setPreferredSize(int width, int height) {
		setPreferredSize(new Size(width, height));
	}
	
	public Layout getLayout() {
		return layout;
	}
	
	public synchronized void setLayout(Layout layout) {
		invalidate();
		this.layout = layout;
	}
	
	public Object getLayoutHint() {
		return layoutHint;
	}
	
	public void setLayoutHint(Object hint) {
		this.layoutHint = hint;
	}
	
	public boolean isFocusable() {
		return isFocusable;
	}
	
	public void setFocusable(boolean focusable) {
		this.isFocusable = focusable;
	}
	
	public boolean isFocused() {
		return isFocused;
	}
	
	protected synchronized void setFocused(boolean focus) {
		if (focus != this.isFocused) {
			this.isFocused = focus;
			getFocusListeners().forEach(listener -> listener.onFocusChange(this, focus));
		}
	}
	
	public void focusNext() {
		Component component = this;
		
		while (true) {
			
			component = component.getNextFocusCandidate(true);
			if (component == this) {
				return;
			}
			
			if (component.isFocusable()) {
				setFocused(false);
				component.setFocused(true);
				return;
			}
			
		}
	}
	
	private Component getNextFocusCandidate(boolean canUseChildren) {
		if (canUseChildren) synchronized (getChildren()) {
			if (!getChildren().isEmpty()) {
				return getChild(0);
			}
		}
		
		Component parent = getParent();
		if (parent != null) {
			synchronized (parent.getChildren()) {
				int ownIndex = parent.getChildIndex(this);
				if (ownIndex != parent.getChildren().size() - 1) {
					return parent.getChild(ownIndex + 1);
				}
			}
			
			return parent.getNextFocusCandidate(false);
		}
		
		return this;
	}
	
	public void focusPrevious() {
		Component component = this;
		
		while (true) {
			
			component = component.getPreviousFocusCandidate();
			if (component == this) {
				return;
			}
			
			if (component.isFocusable()) {
				setFocused(false);
				component.setFocused(true);
				return;
			}
			
		}
	}
	
	private Component getPreviousFocusCandidate() {
		Component parent = getParent();
		if (parent != null) {
			synchronized (parent.getChildren()) {
				int ownIndex = parent.getChildIndex(this);
				if (ownIndex != 0) {
					return parent.getChild(ownIndex - 1).getLastDeepChild();
				}
			}
			
			return parent;
		}
		
		return getLastDeepChild();
	}
	
	private Component getLastDeepChild() {
		synchronized (getChildren()) {
			if (!getChildren().isEmpty()) {
				return getChild(getChildren().size() - 1).getLastDeepChild();
			}
			
			return this;
		}
	}
	
	public synchronized Component findFocused() {
		if (isFocused()) {
			return this;
		}
		
		Component result;
		
		synchronized (getChildren()) {
			for (Component c : getChildren()) {
				result = c.findFocused();
				if (result != null) {
					return result;
				}
			}
		}
		
		return null;
	}
	
	public boolean isHovered() {
		return isHovered;
	}

	protected void setHovered(boolean isHovered) {
		if (this.isHovered != isHovered) {
			this.isHovered = isHovered;
			
			if (!isHovered && !getChildren().isEmpty()) {
				
				synchronized (getChildren()) {
					for (Component child : getChildren()) {
						if (child.isHovered()) {
							child.setHovered(false);
							return;
						}
					}
				}
			}
		}
	}

	public Collection<ComponentHierarchyListener> getHierarchyListeners() {
		return hierarchyListeners;
	}
	
	public void addHierarchyListener(ComponentHierarchyListener listener) {
		getHierarchyListeners().add(listener);
	}
	
	public void removeHierarchyListener(ComponentHierarchyListener listener) {
		getHierarchyListeners().remove(listener);
	}
	
	public Collection<ComponentFocusListener> getFocusListeners() {
		return focusListeners;
	}
	
	public void addFocusListener(ComponentFocusListener listener) {
		getFocusListeners().add(listener);
	}
	
	public void removeFocusListener(ComponentFocusListener listener) {
		getFocusListeners().remove(listener);
	}
	
	public Collection<ComponentInputListener<?>> getInputListeners() {
		return inputListeners;
	}
	
	public void addInputListener(ComponentInputListener<?> listener) {
		getInputListeners().add(listener);
	}
	
	public void removeInputListener(ComponentInputListener<?> listener) {
		getInputListeners().remove(listener);
	}
	
	@SuppressWarnings("unchecked")
	protected boolean dispatchInput(Input input) {
		
		synchronized (getInputListeners()) {
			for (ComponentInputListener<?> listener : getInputListeners()) {
				if (listener.getInputClass().isInstance(input)) {
					((ComponentInputListener<Input>) listener).onInput(this, input);
					if (input.isConsumed()) return false;
				}
			}
		}
		
		return true;
		
	}
	
	public boolean onInput(Input input) {
		switch (input.getTarget()) {
		
		case FOCUSED:
			Component c = findFocused();
			
			if (c == null) {
				return false;
			}
			
			if (attemptFocusTransfer(input, c)) {
				return false;
			}
			
			while (c != null && c.dispatchInput(input)) {
				c = c.getParent();
			}
			
			break;
			
		case HOVERED:
			
			synchronized (getChildren()) {
				for (Component child : getChildren()) {
					if (child.contains(input.getCursorX(), input.getCursorY())) {
						
						child.setHovered(true);
						
						if (!input.isConsumed()) {
							child.onInput(input);
						}
					} else {
						child.setHovered(false);
					}
				}
			}

			if (!input.isConsumed()) dispatchInput(input);
			
			break;
			
		case ALL:
		default:
			synchronized (getChildren()) {
				for (Component child : getChildren()) {
					if (!child.onInput(input)) {
						return false;
					}
				}
			}

			if (!input.isConsumed()) dispatchInput(input);
			
			break;
			
		}

		return input.isConsumed();
	}
	
	private boolean attemptFocusTransfer(Input input, Component focused) {
		if (!(input instanceof KeyInput)) {
			return false;
		}
		
		KeyInput keyInput = (KeyInput) input;
		
		if (keyInput.getKey() == GLFW.GLFW_KEY_TAB && !keyInput.isReleased()) {
			input.consume();
			if (keyInput.hasShift()) {
				focused.focusPrevious();
			} else {
				focused.focusNext();
			}
			return true;
		}
		
		return false;
	}
	
	public synchronized boolean contains(int x, int y) {
		return x >= getX() && x < getX() + getWidth() &&
				y >= getY() && y < getY() + getHeight();
	}

	public synchronized void render() {
		if (width == 0 || height == 0) {
			return;
		}
		
		if (!isValid()) {
			validate();
		}
		
		renderSelf();
		renderChildren();
	}
	
	protected void renderSelf() {
		// Do nothing - override expected
	}
	
	protected void renderChildren() {
		getChildren().forEach(child -> child.render());
	}

}
