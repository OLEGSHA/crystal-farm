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

import ru.windcorp.crystalfarm.audio.ModuleAudioInterface;
import ru.windcorp.crystalfarm.graphics.GraphicsDesign;
import ru.windcorp.crystalfarm.graphics.ModuleGraphicsInterface;
import ru.windcorp.crystalfarm.graphics.notifier.Notifier;
import ru.windcorp.crystalfarm.gui.*;
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
		
		TString base = TString.translated("menu.test.switchButton.text");
		SwitchButton switchButton = new SwitchButton("ExampleSwitchButton",
				base.append(TString.translated("misc.gen.enabled")), base.append(TString.translated("misc.gen.disabled")), false,
				x -> Notifier.positive(TString.translated("menu.test.switchButton").append("!")));
		
		SliderInt sliderInt = new SliderInt("ExampleSliderInt",
				0, 20, 4, 16,
				x -> Notifier.positive(TString.translated("menu.test.sliderInt").append("!")));
		
		SliderFloat sliderFloat = new SliderFloat("ExampleSliderFloat",
				0.0005f, 0.001f, 0.00001f, 0.0008f,
				x -> Notifier.positive(TString.translated("menu.test.sliderFloat").append("!")));
		
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
		
		ColorChooser colorChooser = new ColorChooser("ExampleColorChooser",
				GraphicsDesign.gdGetBorderHoveredColor(),
				true, true,
				x -> Notifier.positive(TString.translated("menu.test.colorChooser").append("!")));
		
		table.addRow("menu.test.button", button);
		table.addRow("menu.test.choiceButton", choiceButton);
		table.addRow("menu.test.switchButton.text", switchButton);
		table.addRow("menu.test.sliderInt", sliderInt);
		table.addRow("menu.test.sliderFloat", sliderFloat);
		table.addRow("menu.test.textField", textField);
		table.addRow("menu.test.colorChooser", colorChooser);
		
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
