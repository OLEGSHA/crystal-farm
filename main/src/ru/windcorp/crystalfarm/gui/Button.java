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

import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.graphics.fonts.FontString;
import ru.windcorp.crystalfarm.gui.layout.LayoutAlign;
import ru.windcorp.crystalfarm.gui.listener.ComponentKeyInputListener;
import ru.windcorp.crystalfarm.gui.listener.ComponentMouseButtonInputListener;
import ru.windcorp.crystalfarm.input.KeyInput;

import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;
import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.*;

public class Button extends ActivatableComponent {
	
	private final Label label;
	
	public Button(String name, FontString label, Consumer<?> action) {
		super(name, action);
		setFocusable(true);
		setLayout(new LayoutAlign());
		
		this.label = new Label(name + ".label", label);
		
		addInputListener((ComponentKeyInputListener) (comp, input) -> {
			if (input.is(GLFW.GLFW_KEY_ENTER, KeyInput.PRESSED)) {
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
	}
	
	public Label getLabel() {
		return label;
	}

	public FontString getText() {
		return getLabel().getText();
	}

	public Button setText(FontString text) {
		getLabel().setText(text);
		return this;
	}

	@Override
	protected void renderSelf() {
		fillRectangle(
				getX(),
				getY(),
				getWidth(),
				getHeight(),
				isHovered() ? gdGetForegroundAltColor() : gdGetForegroundColor(),
				isFocused() ? gdGetBorderFocusedColor() : gdGetBorderColor(),
				gdGetLine());
	}

}
