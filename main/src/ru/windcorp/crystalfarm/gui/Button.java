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

import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.fonts.GString;
import ru.windcorp.crystalfarm.gui.layout.LayoutCenter;
import ru.windcorp.crystalfarm.gui.listener.ComponentKeyInputListener;
import ru.windcorp.crystalfarm.gui.listener.ComponentMouseButtonInputListener;
import ru.windcorp.crystalfarm.input.KeyInput;

import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;

public class Button extends Component implements Consumer<Object> {
	
	public static final Color FILL_COLOR = new Color(0xBF_8D_6E_FF);
	public static final Color FILL_COLOR_HOVERED = FILL_COLOR.clone().multiply(1.5);
	public static final Color BORDER_COLOR = new Color(0x7F_5D_49_FF);
	public static final Color BORDER_COLOR_FOCUSED = BORDER_COLOR.clone().multiply(0.5);
	public static final int BORDER_SIZE = 5;

	private Label label = new Label("Label", null);
	
	private final Collection<Consumer<? super Button>> actions = Collections.synchronizedCollection(new ArrayList<>());
	
	public Button(String name, GString label, Consumer<? super Button> action) {
		super(name);
		setFocusable(true);
		setLayout(new LayoutCenter(BORDER_SIZE));
		
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

	public void setText(GString text) {
		getLabel().setText(text);
	}
	
	public void addAction(Consumer<? super Button> action) {
		if (action != null) getActions().add(action);
	}
	
	public void removeAction(Consumer<? super Button> action) {
		getActions().remove(action);
	}
	
	public Collection<Consumer<? super Button>> getActions() {
		return actions;
	}
	
	/**
	 * Activates this button and dispatches event to action listeners.
	 * @param t igonred
	 */
	@Override
	public void accept(Object t) {
		getActions().forEach(consumer -> consumer.accept(this));
	}

	@Override
	protected void renderSelf() {
		fillRectangle(
				getX(),
				getY(),
				getWidth(),
				getHeight(),
				isFocused() ? BORDER_COLOR_FOCUSED : BORDER_COLOR);
		
		fillRectangle(
				getX() + BORDER_SIZE,
				getY() + BORDER_SIZE,
				getWidth() - BORDER_SIZE * 2,
				getHeight() - BORDER_SIZE * 2,
				isHovered() ? FILL_COLOR_HOVERED : FILL_COLOR);
	}

}
