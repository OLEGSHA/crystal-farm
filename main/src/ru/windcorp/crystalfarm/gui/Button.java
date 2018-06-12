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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.graphics.GraphicsDesign;
import ru.windcorp.crystalfarm.graphics.fonts.GString;
import ru.windcorp.crystalfarm.gui.layout.LayoutCenter;
import ru.windcorp.crystalfarm.gui.listener.ComponentKeyInputListener;
import ru.windcorp.crystalfarm.gui.listener.ComponentMouseButtonInputListener;
import ru.windcorp.crystalfarm.input.KeyInput;

import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;

public class Button extends Component implements Consumer<Object>, GraphicsDesign {

	private Label label = null;
	
	private final Collection<Consumer<?>> actions = Collections.synchronizedCollection(new ArrayList<>());
	
	public Button(String name, GString label, Consumer<?> action) {
		super(name);
		setFocusable(true);
		setLayout(new LayoutCenter());
		
		this.label = new Label(name + ".label", label);
		
		addInputListener((ComponentKeyInputListener) (comp, input) -> {
			if (input.is(GLFW.GLFW_KEY_ENTER, KeyInput.RELEASED)) {
				accept(null);
				input.consume();
			}
		});
		addInputListener((ComponentMouseButtonInputListener) (comp, input) -> {
			if (input.isLeftButton() && input.isPressed()) {
				accept(null);
				input.consume();
			}
		});
		
		setText(label);
		addChild(getLabel());
		addAction(action);
	}
	
	public Label getLabel() {
		return label;
	}

	public GString getText() {
		return getLabel().getText();
	}

	public Button setText(GString text) {
		getLabel().setText(text);
		return this;
	}
	
	public Button addAction(Consumer<?> action) {
		if (action != null) getActions().add(action);
		return this;
	}
	
	public Button removeAction(Consumer<?> action) {
		getActions().remove(action);
		return this;
	}
	
	public Collection<Consumer<?>> getActions() {
		return actions;
	}
	
	/**
	 * Activates this button and dispatches event to action listeners.
	 * @param t igonred
	 */
	@Override
	public void accept(Object t) {
		getActions().forEach(consumer -> consumer.accept(null));
	}

	@Override
	protected void renderSelf() {
		fillRectangle(
				getX(),
				getY(),
				getWidth(),
				getHeight(),
				isHovered() ? FOREGROUND_COLOR_LIGHTER : FOREGROUND_COLOR,
				isFocused() ? BORDER_COLOR_DARKER : BORDER_COLOR,
				LINE_THICKNESS);
	}

}
