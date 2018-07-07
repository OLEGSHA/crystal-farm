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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.graphics.GraphicsInterface;
import ru.windcorp.crystalfarm.gui.listener.ComponentFocusListener;
import ru.windcorp.crystalfarm.gui.listener.ComponentHierarchyListener;
import ru.windcorp.crystalfarm.gui.listener.ComponentInputListener;
import ru.windcorp.crystalfarm.input.Input;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.tge2.util.IndentedStringBuilder;
import ru.windcorp.tge2.util.Nameable;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class Component extends Nameable {

	private final List<Component> children = Collections.synchronizedList(new CopyOnWriteArrayList<>());
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
	
	private long lastFrame = -1;

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
	
	public Component addChild(Component child, int index) {
		if (index == -1) index = getChildren().size();

		invalidate();
		getChildren().add(index, child);
		child.setParent(this);
		getHierarchyListeners().forEach(listener -> listener.onChildAdded(this, child));
		
		return this;
	}
	
	public Component addChild(Component child) {
		return addChild(child, -1);
	}
	
	public Component removeChild(Component child) {
		if (!getChildren().contains(child)) {
			return this;
		}
		
		if (child.isFocused()) {
			child.focusNext();
		}

		invalidate();
		getChildren().remove(child);
		child.setParent(null);
		getHierarchyListeners().forEach(listener -> listener.onChildRemoved(this, child));
		
		return this;
	}
	
	public synchronized int getX() {
		return x;
	}
	
	public synchronized int getY() {
		return y;
	}
	
	public synchronized Component setPosition(int x, int y) {
		invalidate();
		this.x = x;
		this.y = y;
		return this;
	}
	
	public synchronized int getWidth() {
		return width;
	}
	
	public synchronized int getHeight() {
		return height;
	}
	
	public synchronized Component setSize(int width, int height) {
		invalidate();
		this.width = width;
		this.height = height;
		return this;
	}
	
	public Component setSize(Size size) {
		return setSize(size.width, size.height);
	}
	
	public synchronized Component setBounds(int x, int y, int width, int height) {
		setPosition(x, y);
		setSize(width, height);
		return this;
	}
	
	public Component setBounds(int x, int y, Size size) {
		return setBounds(x, y, size.width, size.height);
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
		invalidate();
		
		if (parent == null) {
			layoutSelf();
		} else {
			parent.validate();
		}
	}
	
	protected synchronized void layoutSelf() {
		try {
			if (getLayout() != null) {
				getLayout().layout(this);
			}
			
			getChildren().forEach(child -> {
				child.layoutSelf();
			});
			
			valid = true;
		} catch (Exception e) {
			ExecutionReport.reportCriticalError(e, null,
					"Could not layout component %s with layout %s",
					this,
					String.valueOf(getLayout()));
		}
	}
	
	public synchronized Size getPreferredSize() {
		if (preferredSize != null) {
			return preferredSize;
		}
		
		if (getLayout() != null) {
			try {
				return getLayout().calculatePreferredSize(this);
			} catch (Exception e) {
				ExecutionReport.reportCriticalError(e, null,
						"Could not calculate preferred size for component %s with layout %s",
						this,
						String.valueOf(getLayout()));
			}
		}
		
		return new Size(0, 0);
	}
	
	public synchronized Component setPreferredSize(Size preferredSize) {
		this.preferredSize = preferredSize;
		return this;
	}
	
	public Component setPreferredSize(int width, int height) {
		return setPreferredSize(new Size(width, height));
	}
	
	public Layout getLayout() {
		return layout;
	}
	
	public synchronized Component setLayout(Layout layout) {
		invalidate();
		this.layout = layout;
		return this;
	}
	
	public Object getLayoutHint() {
		return layoutHint;
	}
	
	public Component setLayoutHint(Object hint) {
		this.layoutHint = hint;
		return this;
	}
	
	public boolean isFocusable() {
		return isFocusable;
	}
	
	public Component setFocusable(boolean focusable) {
		this.isFocusable = focusable;
		return this;
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
	
	public Component takeFocus() {
		if (isFocused()) {
			return this;
		}
		
		Component comp = this;
		Component focused = null;
		
		while (comp != null) {
			if ((focused = comp.findFocused()) != null) {
				focused.setFocused(false);
				setFocused(true);
				return this;
			}
			
			comp = comp.getParent();
		}
		
		setFocused(true);
		return this;
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
	
	public Component addHierarchyListener(ComponentHierarchyListener listener) {
		getHierarchyListeners().add(listener);
		return this;
	}
	
	public Component removeHierarchyListener(ComponentHierarchyListener listener) {
		getHierarchyListeners().remove(listener);
		return this;
	}
	
	public Collection<ComponentFocusListener> getFocusListeners() {
		return focusListeners;
	}
	
	public Component addFocusListener(ComponentFocusListener listener) {
		getFocusListeners().add(listener);
		return this;
	}
	
	public Component removeFocusListener(ComponentFocusListener listener) {
		getFocusListeners().remove(listener);
		return this;
	}
	
	public Collection<ComponentInputListener<?>> getInputListeners() {
		return inputListeners;
	}
	
	public Component addInputListener(ComponentInputListener<?> listener) {
		getInputListeners().add(listener);
		return this;
	}
	
	public Component removeInputListener(ComponentInputListener<?> listener) {
		getInputListeners().remove(listener);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	protected boolean dispatchInput(Input input) {
		
		synchronized (getInputListeners()) {
			for (ComponentInputListener<?> listener : getInputListeners()) {
				if (listener.getInputClass().isInstance(input)) {
					try {
						((ComponentInputListener<Input>) listener).onInput(this, input);
					} catch (Exception e) {
						ExecutionReport.reportCriticalError(e, null,
								"Component input listener %s of component %s has thrown an exception while handling input %s",
								listener.toString(),
								this,
								input.toString());
					}
					if (input.isConsumed()) return false;
				}
			}
		}
		
		return true;
		
	}
	
	public boolean onInput(Input input) {
		try {
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
		} catch (Exception e) {
			ExecutionReport.reportCriticalError(e, null,
					"Component %s has thrown an exception while handling input %s",
					this,
					input.toString());
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

	protected synchronized final void render() {
		if (width == 0 || height == 0) {
			return;
		}
		
		if (!isValid()) {
			validate();
		}
		
		try {
			renderSelf();
		} catch (Exception e) {
			ExecutionReport.reportCriticalError(e, null,
					"Could not render component %s (pre-children)",
					this);
		}
		
		renderChildren();
		
		try {
			postRenderSelf();
		} catch (Exception e) {
			ExecutionReport.reportCriticalError(e, null,
					"Could not render component %s (post-children)",
					this);
		}
		
		lastFrame = GraphicsInterface.getFrame();
	}
	
	protected void renderSelf() {
		// To be overridden
	}
	
	protected void postRenderSelf() {
		// To be overridden
	}
	
	protected void renderChildren() {
		getChildren().forEach(child -> child.render());
	}
	
	public void dump(IndentedStringBuilder sb) {
		sb.append(getClass().getSimpleName());
		sb.append(" ");
		sb.append(getName());
		sb.append(" ");
		sb.append(GraphicsInterface.dumpIsRendered(lastFrame));
		
		Map<String, String> chars = new HashMap<>();
		getDumpCharacteristics(chars);
		chars.forEach((name, value) -> {
			sb.append(" ");
			sb.append(name);
			sb.append("=");
			sb.append(value);
			sb.append(";");
		});
		
		if (getChildren().isEmpty()) {
			return;
		}
		
		sb.append(" {");
		sb.breakLine();
		getChildren().forEach(child -> {
			sb.indent();
			child.dump(sb);
			sb.unindent();
			sb.breakLine();
		});
		
		sb.append("} " + getClass().getSimpleName());
	}
	
	protected void getDumpCharacteristics(Map<String, String> map) {
		map.put("bounds", getX() + ":" + getY() + ">" + getWidth() + ":" + getHeight());
		map.put("valid", String.valueOf(isValid()));
		map.put("layout", String.valueOf(getLayout()));
		map.put("hint", String.valueOf(getLayoutHint()));
		map.put("focusable", String.valueOf(isFocusable()));
		if (isFocusable()) map.put("focused", String.valueOf(isFocused()));
		map.put("hovered", String.valueOf(isHovered()));
	}
	
	/**
	 * Returns a component that displays this component in its center.
	 * @return a {@link Aligner} initialized to center this component
	 */
	public Component center() {
		return new Aligner(this);
	}
	
	/**
	 * Returns a component that aligns this component.
	 * @return a {@link Aligner} initialized with this component
	 */
	public Component align(double x, double y) {
		return new Aligner(this, x, y);
	}
	
	/**
	 * Returns a component that allows scrolling this component
	 * @return a {@link Scroller} initialized with this component
	 */
	public Component scroller() {
		return new Scroller(this);
	}

}
