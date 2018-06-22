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

import java.util.List;

import ru.windcorp.crystalfarm.cfg.*;
import ru.windcorp.crystalfarm.translation.TString;

public class GuiSettingEditors {
	
	public static Component createEditor(ConfigurationNode setting) {
		if (setting instanceof SettingInt) {
			return createIntEditor((SettingInt) setting);
		}
		if (setting instanceof SettingFloat) {
			return createFloatEditor((SettingFloat) setting);
		}
		if (setting instanceof SettingBoolean) {
			return createBooleanEditor((SettingBoolean) setting);
		}
		
		return null;
	}

	private static Component createIntEditor(SettingInt setting) {
		SliderInt slider = new SliderInt(
				setting.getName() + ".editor",
				setting.getMin(),
				setting.getMax(),
				setting.getStep(),
				setting.get(),
				null);
		
		slider.addAction(x -> setting.set(slider.getValue()));
		setting.addListener(x -> slider.setValueSilently(setting.get()));
		return slider;
	}
	
	private static Component createFloatEditor(SettingFloat setting) {
		SliderFloat slider = new SliderFloat(
				setting.getName() + ".editor",
				setting.getMin(),
				setting.getMax(),
				setting.getStep(),
				setting.get(),
				null);
		
		slider.addAction(x -> setting.set(slider.getValue()));
		setting.addListener(x -> slider.setValueSilently(setting.get()));
		return slider;
	}
	
	private static Component createBooleanEditor(SettingBoolean setting) {
		SwitchButton button = new SwitchButton(
				setting.getName() + ".editor",
				TString.translated("misc.gen.enabled"),
				TString.translated("misc.gen.disabled"),
				setting.get(),
				null);
		
		button.addAction(x -> setting.set(button.getState()));
		setting.addListener(x -> button.setStateSilently(setting.get()));
		return button;
	}
	
	public static Button createResetter(ConfigurationNode setting) {
		if (setting instanceof SettingInt) {
			return createIntResetter((SettingInt) setting);
		}
		if (setting instanceof SettingFloat) {
			return createFloatResetter((SettingFloat) setting);
		}
		if (setting instanceof SettingBoolean) {
			return createBooleanResetter((SettingBoolean) setting);
		}
		
		return null;
	}

	private static Button createIntResetter(SettingInt setting) {
		return new Button(
				setting.getName() + ".resetter",
				TString.translated("misc.gen.reset").toFont(),
				x -> setting.set(setting.getDefaultValue()));
	}

	private static Button createFloatResetter(SettingFloat setting) {
		return new Button(
				setting.getName() + ".resetter",
				TString.translated("misc.gen.reset").toFont(),
				x -> setting.set(setting.getDefaultValue()));
	}

	private static Button createBooleanResetter(SettingBoolean setting) {
		return new Button(
				setting.getName() + ".resetter",
				TString.translated("misc.gen.reset").toFont(),
				x -> setting.set(setting.getDefaultValue()));
	}

	public static <T> Component createLimitedChoiceEditor(Setting<T> setting, List<T> choices) {
		ChoiceButton<T> button = new ChoiceButton<T>(
				setting.getName() + ".editor",
				null,
				choices.indexOf(setting.get()),
				choices);
		
		button.addAction(x -> setting.set(button.getSelection()));
		setting.addListener(x -> button.selectSilently(choices.indexOf(setting.get())));
		
		return button;
	}

}
