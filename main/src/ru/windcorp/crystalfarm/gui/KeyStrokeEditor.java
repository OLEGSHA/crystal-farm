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

import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.gdGetBorderColor;
import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.gdGetBorderFocusedColor;
import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.gdGetForegroundAltColor;
import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.gdGetForegroundColor;
import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.gdGetLine;
import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.fillRectangle;

import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.graphics.GraphicsInterface;
import ru.windcorp.crystalfarm.graphics.OneTimeKeyStrokeConsumer;
import ru.windcorp.crystalfarm.input.KeyStroke;
import ru.windcorp.crystalfarm.translation.TString;

public class KeyStrokeEditor extends Button implements OneTimeKeyStrokeConsumer {
	
	private static final TString EDITING = TString.wrap("> ... <");

	private KeyStroke value;
	private boolean isEditing = false;

	public KeyStrokeEditor(String name, KeyStroke value, Consumer<?> action) {
		super(name, TString.wrap(value.toString()).toFont(), action);
		this.value = value;
	}

	public KeyStroke getValue() {
		return value;
	}

	public boolean isEditing() {
		return isEditing;
	}
	
	protected void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
		if (isEditing) {
			getText().setText(EDITING);
		}
	}

	public void setValueSilently(KeyStroke value) {
		this.value = value;
		getText().setText(TString.wrap(value.toString()));
	}
	
	public void setValue(KeyStroke value) {
		setValueSilently(value);
		super.accept(null);
	}
	
	@Override
	public void accept(Object t) {
		setEditing(true);
		GraphicsInterface.onNextKeyStroke(this);
	}
	
	@Override
	protected void renderSelf() {
		fillRectangle(
				getX(),
				getY(),
				getWidth(),
				getHeight(),
				(isHovered() || isEditing()) ? gdGetForegroundAltColor() : gdGetForegroundColor(),
				isFocused() ? gdGetBorderFocusedColor() : gdGetBorderColor(),
				gdGetLine());
	}

	@Override
	public void onKeyStroke(KeyStroke keyStroke) {
		setEditing(false);
		
		if (keyStroke.getKey() != GLFW.GLFW_KEY_ESCAPE) {
			setValue(keyStroke);
		} else {
			getText().setText(TString.wrap(value.toString()));
		}
	}

}
