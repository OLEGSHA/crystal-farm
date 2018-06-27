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

import java.util.Arrays;

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.audio.ModuleAudioInterface;
import ru.windcorp.crystalfarm.graphics.GraphicsDesign;
import ru.windcorp.crystalfarm.graphics.ModuleGraphicsInterface;
import ru.windcorp.crystalfarm.graphics.notifier.Notifier;
import ru.windcorp.crystalfarm.gui.*;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.crystalfarm.input.KeyStroke;
import ru.windcorp.crystalfarm.translation.TString;

public class TestMenu extends MenuLayer {

	public TestMenu() {
		super("TestMenu", true);
		
		Table table = new Table(getName() + ".table", 3);
		
		Button button = new Button("ExampleButton", TString.translated("menu.test.button").toFont(),
				x -> {
					
					Notifier.positive(TString.translated("menu.test.button").append("!"));
				});
		ChoiceButton<String> choiceButton = new ChoiceButton<String>("ExampleChoiceButton",
				x -> Notifier.positive(TString.translated("menu.test.choiceButton").append("!")),
				0, Arrays.asList("1", "2", "3"));
		
		SliderInt sliderInt = new SliderInt("ExampleSliderInt",
				0, 20, 4, 16,
				x -> Notifier.positive(TString.translated("menu.test.sliderInt").append("!")));
		
		TextField textField = new TextField("ExampleTestField",
				"Foobar", 20,
				x -> {
					for (int i = 0; i < x.getLength(); ++i) {
						if (Character.isWhitespace(x.getBuffer()[i])) {
							return false;
						}
					}
					return true;
				},
				x -> Notifier.positive(TString.translated("menu.test.textField").append("!")));
		
		KeyStrokeEditor keyStrokeEditor = new KeyStrokeEditor("ExampleKeyStrokeEditor",
				new KeyStroke(GLFW.GLFW_KEY_W, GLFW.GLFW_MOD_ALT, KeyInput.PRESSED),
				x -> Notifier.positive(TString.translated("menu.test.control").append("!")));
		
		table.addRow("menu.test.button", button);
		table.addRow("menu.test.choiceButton", choiceButton);
		table.addRow("menu.test.sliderInt", sliderInt);
		table.addRow("menu.test.textField", textField);
		table.addRow("menu.test.control", keyStrokeEditor);
		
		table.addRow("menu.test.gain",
				GuiSettingEditors.createEditor(ModuleAudioInterface.GAIN),
				GuiSettingEditors.createResetter(ModuleAudioInterface.GAIN));
		table.addRow("menu.test.fullscreen",
				GuiSettingEditors.createEditor(ModuleGraphicsInterface.WINDOW_FULLSCREEN),
				GuiSettingEditors.createResetter(ModuleGraphicsInterface.WINDOW_FULLSCREEN));
		table.addRow("menu.test.foreground",
				GuiSettingEditors.createEditor(GraphicsDesign.SETTING_FOREGROUND_COLOR),
				GuiSettingEditors.createResetter(GraphicsDesign.SETTING_FOREGROUND_COLOR));
		
		add(table);
		button.takeFocus();
	}

}
