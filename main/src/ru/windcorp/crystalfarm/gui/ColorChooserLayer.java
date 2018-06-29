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

import static org.lwjgl.opengl.GL11.*;
import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.*;
import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;

import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.RandomColor;
import ru.windcorp.crystalfarm.gui.layout.LayoutHorizontal;
import ru.windcorp.crystalfarm.gui.menu.MenuLayer;
import ru.windcorp.crystalfarm.translation.TString;

public class ColorChooserLayer extends MenuLayer {
	
	private static final double[] ZERO = new double[] { 0, 0, 0, 1 };
	
	private class ChannelSlider extends SliderInt {
		
		final double[] extreme;

		ChannelSlider(String name, int value, double[] extreme, Consumer<?> action) {
			super(ColorChooserLayer.this.getName() + "." + name, 0, 255, 1, value, action);
			this.extreme = extreme;
			setPreferredSize(256 + 2*gdGetLine(), 0);
		}
		
		@Override
		protected void renderSelf() {
			fillRectangle(
					getX(),
					getY(),
					getWidth(),
					getHeight(),
					isFocused() ? gdGetBorderFocusedColor() : gdGetBorderColor());
			
			ColorChooser.texture.render(
					getX() + gdGetLine(),
					getY() + gdGetLine(), 
					getWidth() - 2*gdGetLine(),
					getHeight() - 2*gdGetLine());
			
			glBegin(GL_TRIANGLES);
				glColor4dv(extreme);
				glVertex2i(getX() + gdGetLine(), getY() + gdGetLine());
				
				glColor4dv(ZERO);
				glVertex2i(getX() + getWidth() - gdGetLine(), getY() + getHeight() - gdGetLine());
				glVertex2i(getX() + getWidth() - gdGetLine(), getY() + gdGetLine());
				
				glColor4dv(extreme);
				glVertex2i(getX() + gdGetLine(), getY() + gdGetLine());
				glVertex2i(getX() + gdGetLine(), getY() + getHeight() - gdGetLine());
				
				glColor4dv(ZERO);
				glVertex2i(getX() + getWidth() - gdGetLine(), getY() + getHeight() - gdGetLine());
			glEnd();
			
			int pos = getX() + gdGetLine() + (int) ((getWidth() - 2*gdGetLine()) * ((getValue() - getMin()) / (double) (getMax() - getMin()))) - gdGetLine()/2;
			
			fillRectangle(
					pos,
					getY() + gdGetLine(),
					gdGetLine(),
					getHeight() - 2*gdGetLine(),
					isFocused() ? Color.WHITE : Color.LIGHT_GRAY);
		}
		
	}
	
	private Color dummy;
	
	public ColorChooserLayer(ColorChooser chooser) {
		super(chooser.getName() + ".layer", TString.translated("gui.colorChooser.title").toFont(), false);
		this.dummy = new Color(chooser.getColor());
		
		Table table = new Table(getName() + ".table", 1);
		
		ChannelSlider 
				sliderR = new ChannelSlider(
						"r",
						(int) Math.round(chooser.getColor().r * 0xFF),
						new double[] {1, 0, 0, 1}, null),
				sliderG = new ChannelSlider(
						"g",
						(int) Math.round(chooser.getColor().g * 0xFF),
						new double[] {0, 1, 0, 1}, null),
				sliderB = new ChannelSlider(
						"b",
						(int) Math.round(chooser.getColor().b * 0xFF),
						new double[] {0, 0, 1, 1}, null);
		
		sliderR.addAction(x -> dummy.r = sliderR.getValue() / 255.0);
		sliderG.addAction(x -> dummy.g = sliderG.getValue() / 255.0);
		sliderB.addAction(x -> dummy.b = sliderB.getValue() / 255.0);
		
		table.addRow("gui.colorChooser.channel.r", sliderR);
		table.addRow("gui.colorChooser.channel.g", sliderG);
		table.addRow("gui.colorChooser.channel.b", sliderB);
		
		if (chooser.isAllowAlpha()) {
			ChannelSlider sliderA = new ChannelSlider(
							"a",
							(int) Math.round(chooser.getColor().a * 0xFF),
							new double[] {0, 0, 0, 0}, null);
			
			sliderA.addAction(x -> dummy.a = sliderA.getValue() / 255.0);
			
			table.addRow("gui.colorChooser.channel.a", sliderA);
		}
		
		TextField textField = new TextField(
				getName() + ".hex",
				dummy.toRGBAString(),
				10,
				field -> {
					String text = field.get();
					if (text.startsWith("0x")) {
						text = text.substring("0x".length());
					} else if (text.startsWith("#")) {
						text = text.substring("#".length());
					}
					
					try {
						Integer.parseUnsignedInt(text, 0x10);
						return true;
					} catch (NumberFormatException e) {
						return false;
					}
				}, null);
		
		textField.addAction(
				x -> {
					String text = textField.get();
					if (text.startsWith("0x")) {
						text = text.substring("0x".length());
					} else if (text.startsWith("#")) {
						text = text.substring("#".length());
					}
					
					try {
						close();
						chooser.setColor(new Color(Integer.parseUnsignedInt(text, 0x10)));
					} catch (NumberFormatException e) {
						return;
					}
		});
		
		table.addRow("gui.colorChooser.hex", textField);
		
		add(table);
		
		add(new Component(getName() + ".preview") {
			@Override
			public Size getPreferredSize() {
				return new Size(0, 10*gdGetLine());
			}
			@Override
			protected void renderSelf() {
				fillRectangle(
						getX(),
						getY(),
						getWidth(),
						getHeight(),
						isFocused() ? gdGetBorderFocusedColor() : gdGetBorderColor());
				ColorChooser.texture.render(
						getX() + gdGetLine(),
						getY() + gdGetLine(),
						getWidth() - 2*gdGetLine(),
						getHeight() - 2*gdGetLine());
				fillRectangle(
						getX() + gdGetLine(),
						getY() + gdGetLine(),
						getWidth() - 2*gdGetLine(),
						getHeight() - 2*gdGetLine(),
						dummy);
			}
		});
		
		Component buttons = new Component(getName() + ".buttons");
		buttons.setLayout(new LayoutHorizontal(0, 1));
		buttons.addChild(new Button(getName() + ".discard", TString.translated("gui.colorChooser.discard").toFont(),
				x -> close()).takeFocus());
		buttons.addChild(new Button(getName() + ".accept", TString.translated("gui.colorChooser.accept").toFont(),
				x -> {
					close();
					chooser.setColor(dummy);
				}));
		if (chooser.isAllowRandom()) {
			buttons.addChild(new Button(getName() + ".random", TString.translated("gui.colorChooser.random").toFont(),
					x -> {
						close();
						chooser.setColor(new RandomColor());
					}));
		}
		
		add(buttons.center());
	}

}
