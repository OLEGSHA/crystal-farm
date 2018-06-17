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
import ru.windcorp.crystalfarm.gui.layout.LayoutBorderHorizontal;
import ru.windcorp.crystalfarm.gui.listener.ComponentKeyInputListener;
import ru.windcorp.crystalfarm.gui.listener.ComponentMouseButtonInputListener;
import ru.windcorp.crystalfarm.translation.TString;

public abstract class ArrowedComponent extends ActivatableComponent {
	
	private static final FontString ARROW_LEFT = TString.wrap("<").toFont().setBold(true);
	private static final FontString ARROW_RIGHT = TString.wrap(">").toFont().setBold(true);
	
	private class Arrow extends Label {

		public Arrow(String name, FontString text) {
			super(name, text);
		}
		
		@Override
		protected void renderSelf() {
			if (ArrowedComponent.this.isFocused() || ArrowedComponent.this.isHovered()) super.renderSelf();
		}
		
	}
	
	private final Label display;
	private final Label left;
	private final Label right;

	public ArrowedComponent(String name, Consumer<?> action, FontString text) {
		super(name, action);
		
		setLayout(new LayoutBorderHorizontal(0));
		setFocusable(true);
		
		this.display = new Label(name + ".display", text);
		addChild(this.display.center().setLayoutHint(LayoutBorderHorizontal.CENTER));
		
		this.left = new Arrow(name + ".leftArrow", ARROW_LEFT);
		addChild(this.left.center().setLayoutHint(LayoutBorderHorizontal.LEFT));
		
		this.right = new Arrow(name + ".rightArrow", ARROW_RIGHT);
		addChild(this.right.center().setLayoutHint(LayoutBorderHorizontal.RIGHT));
		
		addInputListener((ComponentMouseButtonInputListener) (comp, input) -> {
			if (!input.isPressed()) {
				return;
			}
			
			if (input.isLeftButton()) {
				selectNext();
				input.consume();
			} else if (input.isRightButton()) {
				selectPrevious();
				input.consume();
			}
		});
		
		addInputListener((ComponentKeyInputListener) (comp, input) -> {
			if (input.isReleased()) {
				return;
			}
			
			switch (input.getKey()) {
			case GLFW.GLFW_KEY_DOWN:
			case GLFW.GLFW_KEY_RIGHT:
			case GLFW.GLFW_KEY_ENTER:
				selectNext();
				input.consume();
				break;
				
			case GLFW.GLFW_KEY_UP:
			case GLFW.GLFW_KEY_LEFT:
				selectPrevious();
				input.consume();
				break;
			}
		});
	}
	
	public abstract void selectNext();
	public abstract void selectPrevious();

	public Label getDisplay() {
		return display;
	}
	
}
