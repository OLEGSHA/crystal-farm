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
import ru.windcorp.crystalfarm.graphics.ModuleGraphicsInterface;
import ru.windcorp.crystalfarm.graphics.notifier.Notifier;
import ru.windcorp.crystalfarm.gui.*;
import ru.windcorp.crystalfarm.translation.TString;

public class TestMenu extends MenuLayer {

	public TestMenu() {
		super("TestMenu", true);
		
		Button button = new Button("ExampleButton", TString.translated("menu.test.button").toFont(),
				x -> Notifier.positive(TString.translated("menu.test.button").append("!")));
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
		
		add(button, choiceButton, switchButton, sliderInt, sliderFloat,
				GuiSettingEditors.createEditor(ModuleAudioInterface.GAIN),
				GuiSettingEditors.createEditor(ModuleGraphicsInterface.WINDOW_FULLSCREEN));
		
		button.takeFocus();
	}

}
