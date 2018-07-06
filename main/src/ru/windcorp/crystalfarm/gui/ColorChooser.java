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

import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.RandomColor;
import ru.windcorp.crystalfarm.graphics.fonts.Font;
import ru.windcorp.crystalfarm.graphics.fonts.FontManager;
import ru.windcorp.crystalfarm.graphics.fonts.FontStyle;
import ru.windcorp.crystalfarm.graphics.texture.SimpleTexture;
import ru.windcorp.crystalfarm.graphics.texture.Texture;
import ru.windcorp.crystalfarm.gui.listener.ComponentKeyInputListener;
import ru.windcorp.crystalfarm.gui.listener.ComponentMouseButtonInputListener;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.crystalfarm.translation.TString;
import ru.windcorp.tge2.util.NumberUtil;

public class ColorChooser extends ActivatableComponent {
	
	static Texture texture = SimpleTexture.get("ui/colorChooser/alphaBg");
	
	private static final char[] SAMPLE = NumberUtil.toFullHex(0xDDDDDDDD);
	private static final TString TEXT_RANDOM = TString.translated("gui.colorChooser.random");
	
	private Color color;
	private char[] text;
	
	private final boolean allowAlpha;
	private final boolean allowRandom;
	
	private Font font = FontManager.getDefaultFont();
	
	public ColorChooser(String name, Color color, boolean allowAlpha, boolean allowRandom, Consumer<?> action) {
		super(name, action);
		setColorSilently(color);
		this.allowAlpha = allowAlpha;
		this.allowRandom = allowRandom;
		
		setFocusable(true);
		
		Size size = getFont().getSize(SAMPLE, false);
		size.width += 5*gdGetLine() + size.height;
		size.height += 2*gdGetLine();
		setPreferredSize(size);
		
		addInputListener((ComponentKeyInputListener) (comp, input) -> {
			if (input.is(GLFW.GLFW_KEY_ENTER, KeyInput.PRESSED)) {
				new ColorChooserLayer(this).show(false);
				input.consume();
			}
		});
		addInputListener((ComponentMouseButtonInputListener) (comp, input) -> {
			if (input.isLeftButton() && input.isPressed()) {
				new ColorChooserLayer(this).show(false);
				input.consume();
			}
		});
	}

	public Color getColor() {
		return color;
	}
	
	public synchronized void setColorSilently(Color color) {
		this.color = color;
		if (color instanceof RandomColor) {
			this.text = TEXT_RANDOM.get().toCharArray();
		} else {
			this.text = NumberUtil.toFullHex(color.getRGBA());
		}
	}
	
	public void setColor(Color color) {
		setColorSilently(color);
		accept(null);
	}

	public boolean isAllowAlpha() {
		return allowAlpha;
	}

	public boolean isAllowRandom() {
		return allowRandom;
	}
	
	public Font getFont() {
		return font;
	}
	
	@Override
	protected synchronized void renderSelf() {
		fillRectangle(
				getX(),
				getY(),
				getWidth(),
				getHeight(),
				isHovered() ? gdGetForegroundAltColor() : gdGetForegroundColor(),
				isFocused() ? gdGetBorderFocusedColor() : gdGetBorderColor(),
				gdGetLine());
		texture.render(
				getX() + gdGetLine(),
				getY() + gdGetLine(),
				getHeight() - 2*gdGetLine(),
				getHeight() - 2*gdGetLine());
		fillRectangle(
				getX() + gdGetLine(),
				getY() + gdGetLine(),
				getHeight() - 2*gdGetLine(),
				getHeight() - 2*gdGetLine(),
				getColor());
		fillRectangle(
				getX() + getHeight() - gdGetLine(),
				getY() + gdGetLine(),
				gdGetLine(),
				getHeight() - 2*gdGetLine(),
				isFocused() ? gdGetBorderFocusedColor() : gdGetBorderColor());
		getFont().render(
				text,
				getX() + 1*gdGetLine() + getHeight() + (getWidth() - 3*gdGetLine() - getHeight() - getFont().getLength(text, false)) / 2,
				getY() + gdGetLine() + (getHeight() - 2*gdGetLine() - getFont().getHeight()) / 2,
				false,
				FontStyle.PLAIN,
				gdGetFontColor());
	}

}
