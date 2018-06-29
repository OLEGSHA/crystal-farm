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

import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;
import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.*;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.audio.Sound;
import ru.windcorp.crystalfarm.audio.SoundManager;
import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.fonts.Font;
import ru.windcorp.crystalfarm.graphics.fonts.FontManager;
import ru.windcorp.crystalfarm.graphics.fonts.FontStyle;
import ru.windcorp.crystalfarm.input.CharInput;
import ru.windcorp.crystalfarm.input.Input;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.crystalfarm.input.MouseButtonInput;

// TOADD: caret position, selections, clipboard
public class TextField extends ActivatableComponent {
	
	private static Sound sound = SoundManager.get("ui/error");
	
	private char[] buffer;
	private int length;
	
	private boolean isChanged;
	private boolean isError;
	
	private final Predicate<TextField> filter;
	private final Font font = FontManager.getDefaultFont();
	
	public TextField(String name, String value, int length, Predicate<TextField> filter, Consumer<?> action) {
		super(name, action);
		setValueSilently(value);
		this.filter = filter;
		
		setFocusable(true);
		
		char[] sample = new char[length];
		Arrays.fill(sample, '_');
		Size size = getFont().getSize(sample, false);
		size.width += 2*gdGetLine();
		size.height += 2*gdGetLine();
		setPreferredSize(size);
	}
	
	public char[] getBuffer() {
		return buffer;
	}
	
	public String get() {
		return new String(getBuffer(), 0, getLength());
	}
	
	public synchronized void setValueSilently(String value) {
		this.buffer = value.toCharArray();
		this.length = this.buffer.length;
		this.isChanged = false;
		check();
	}
	
	public void setValue(String value) {
		setValueSilently(value);
		accept(null);
	}

	public int getLength() {
		return length;
	}
	
	public Font getFont() {
		return font;
	}
	
	public boolean isChanged() {
		return isChanged;
	}
	
	public boolean isError() {
		return isError;
	}
	
	protected void onChange() {
		isChanged = true;
		check();
	}
	
	private void check() {
		isError = getFilter() == null ? false : !getFilter().test(this);
	}
	
	public Predicate<TextField> getFilter() {
		return filter;
	}
	
	@Override
	protected boolean dispatchInput(Input input) {
		if (input instanceof MouseButtonInput) {
			takeFocus();
			input.consume();
			return false;
		} else if (input instanceof KeyInput) {
			KeyInput ki = (KeyInput) input;
			
			if (ki.is(GLFW.GLFW_KEY_ENTER, KeyInput.PRESSED)) {
				synchronized (this) {
					if (isChanged()) {
						if (isError()) {
							sound.play();
						} else {
							isChanged = false;
							accept(null);
						}
					}
				}

				input.consume();
				return false;
			} else if (!ki.isReleased() && ki.getKey() == GLFW.GLFW_KEY_BACKSPACE) {
				synchronized (this) {
					if (getLength() == 0) {
						sound.play();
					} else {
						this.length--;
						onChange();
					}
				}

				input.consume();
				return false;
			}
		} else if (input instanceof CharInput) {
			append((CharInput) input);

			input.consume();
			return false;
		}
		
		return super.dispatchInput(input);
	}

	private synchronized void append(CharInput input) {
		if (buffer.length == length) {
			buffer = Arrays.copyOf(buffer, buffer.length + 10);
		}
		
		buffer[length++] = (char) input.getCharacter();
		
		onChange();
	}

	@Override
	protected void renderSelf() {
		fillRectangle(
				getX(),
				getY(),
				getWidth(),
				getHeight(),
				getBackgroundColor(),
				isFocused() ? gdGetBorderFocusedColor() : gdGetBorderColor(),
				gdGetLine());

		setMask(
				getX() + gdGetLine(),
				getY() + gdGetLine(),
				getWidth() - 2*gdGetLine(),
				getHeight() - 2*gdGetLine());
		
		synchronized (this) {
			getFont().render(
					getBuffer(),
					0, getLength(),
					
					getX() + gdGetLine() + Math.min(
							gdGetLine(),
							getWidth() - 3*gdGetLine() - getFont().getLength(getBuffer(), 0, getLength(), false)),
					getY() + gdGetLine() + (getHeight() - 2*gdGetLine() - getFont().getHeight()) / 2,
					
					false,
					FontStyle.PLAIN,
					Color.BLACK
					);
		}
		
		resetMask();
	}

	private Color getBackgroundColor() {
		if (isError()) {
			return isHovered() ? Color.SOFT_RED : Color.RED;
		} else {
			if (isChanged()) {
				return isHovered() ? Color.BRIGHT_YELLOW : Color.YELLOW;
			} else {
				return isHovered() ? Color.LIGHTEST_GRAY : Color.LIGHT_GRAY;
			}
		}
	}

}
