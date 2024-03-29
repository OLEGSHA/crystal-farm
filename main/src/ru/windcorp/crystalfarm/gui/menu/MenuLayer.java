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
package ru.windcorp.crystalfarm.gui.menu;

import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;
import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.*;

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.graphics.fonts.FontString;
import ru.windcorp.crystalfarm.gui.Button;
import ru.windcorp.crystalfarm.gui.Component;
import ru.windcorp.crystalfarm.gui.GuiLayer;
import ru.windcorp.crystalfarm.gui.layout.LayoutBorderVertical;
import ru.windcorp.crystalfarm.input.Input;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.crystalfarm.translation.TString;

public class MenuLayer extends GuiLayer {
	
	private final Menu contents;
	
	public MenuLayer(String name, boolean addCloseButton) {
		this(name, TString.translated("menu." + name + ".title").toFont(), addCloseButton);
	}

	public MenuLayer(String name, FontString title, boolean addCloseButton) {
		super(name);
		
		Component root = new Component(name + "Bg");
		root.setLayout(new LayoutBorderVertical());
		
		this.contents = new Menu(name, title);
		
		root.addChild(contents.center().setLayoutHint(LayoutBorderVertical.CENTER));
		if (addCloseButton) {
			root.addChild(
					new Button(name + ".close", TString.translated("menu.generic.close").toFont(), button -> close())
						.center().setLayoutHint(LayoutBorderVertical.DOWN)
			);
		}
		
		setRoot(root);
		root.takeFocus();
	}
	
	@Override
	public void onInput(Input input) {
		super.onInput(input);
		
		if (!input.isConsumed() &&
				input instanceof KeyInput &&
				((KeyInput) input).is(GLFW.GLFW_KEY_ESCAPE, KeyInput.PRESSED)) {
			close();
		}
		
		input.consume();
	}
	
	@Override
	public void renderImpl() {
		fillRectangle(0, 0, getWindowWidth(), getWindowHeight(), gdGetCoverColor());
		super.renderImpl();
	}
	
	public Menu getContents() {
		return contents;
	}
	
	public MenuLayer add(Component... entries) {
		for (Component c : entries) {
			getContents().addChild(c);
		}
		
		return this;
	}

}
