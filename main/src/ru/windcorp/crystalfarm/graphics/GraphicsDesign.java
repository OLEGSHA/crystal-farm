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
package ru.windcorp.crystalfarm.graphics;

import ru.windcorp.crystalfarm.cfg.Section;
import ru.windcorp.crystalfarm.cfg.Setting;
import ru.windcorp.crystalfarm.cfg.SettingInt;

public class GraphicsDesign {
	
	public static final Setting<Color> SETTING_BACKGROUND_COLOR = new Setting<Color>(
			"Background", "Background color", Color.class,
			Color.WHITE);
	
	public static final Setting<Color> SETTING_BACKGROUND_ALT_COLOR = new Setting<Color>(
			"BackgroundAlt", "Alternative background color", Color.class,
			gdDarker(SETTING_BACKGROUND_COLOR.getDefaultValue()));
	
	public static final Setting<Color> SETTING_FOREGROUND_COLOR = new Setting<Color>(
			"Foreground", "Foreground color", Color.class,
			new RandomColor());
	
	public static final Setting<Color> SETTING_FOREGROUND_ALT_COLOR = new Setting<Color>(
			"ForegroundAlt", "Alternative foreground color", Color.class,
			gdLighter(SETTING_FOREGROUND_COLOR.getDefaultValue()));
	
	public static final Setting<Color> SETTING_FONT_COLOR = new Setting<Color>(
			"Font", "Font color", Color.class,
			Color.WHITE);
	
	public static final Setting<Color> SETTING_FONT_ALT_COLOR = new Setting<Color>(
			"FontAlt", "Alternative font color", Color.class,
			Color.BLACK);
	
	public static final Setting<Color> SETTING_COVER_COLOR = new Setting<Color>(
			"Cover", "Cover color", Color.class,
			new Color(0xFF_FF_FF_88));
	
	public static final Setting<Color> SETTING_BORDER_COLOR = new Setting<Color>(
			"Border", "Border color", Color.class,
			gdDarker(SETTING_FOREGROUND_COLOR.getDefaultValue()));
	
	public static final Setting<Color> SETTING_BORDER_FOCUSED_COLOR = new Setting<Color>(
			"BorderFocused", "Focused border color", Color.class,
			SETTING_BORDER_COLOR.getDefaultValue().clone().multiply(0.5));
	
	public static final Setting<Color> SETTING_BORDER_HOVERED_COLOR = new Setting<Color>(
			"BorderHovered", "Hovered border color", Color.class,
			gdLighter(SETTING_BORDER_COLOR.getDefaultValue()));
	
	public static final SettingInt SETTING_LINE = new SettingInt(
			"Line", "Line thickness, in pixels",
			5, 20, 1, 1);
	
	static {
		SETTING_LINE.addListener(listener -> GraphicsInterface.invalidateEverything());
	}
	
	static final double DARKER_MULTIPLIER = 0.75;
	static final double LIGHTER_MULTIPLIER = 1.25;
	
	static Section registerConfig() {
		Section section = new Section("Design", "Graphics design configuration");
		
		section.add(
				SETTING_BACKGROUND_COLOR,
				SETTING_BACKGROUND_ALT_COLOR,
				SETTING_FOREGROUND_COLOR,
				SETTING_FOREGROUND_ALT_COLOR,
				SETTING_BORDER_COLOR,
				SETTING_BORDER_FOCUSED_COLOR,
				SETTING_BORDER_HOVERED_COLOR,
				SETTING_FONT_COLOR,
				SETTING_FONT_ALT_COLOR,
				SETTING_COVER_COLOR,
				SETTING_LINE);
		
		return section;
	}
	
	public static Color gdGetBackgroundColor() {
		return SETTING_BACKGROUND_COLOR.get();
	}
	
	public static Color gdGetBackgroundAltColor() {
		return SETTING_BACKGROUND_ALT_COLOR.get();
	}
	
	public static Color gdGetForegroundColor() {
		return SETTING_FOREGROUND_COLOR.get();
	}
	
	public static Color gdGetForegroundAltColor() {
		return SETTING_FOREGROUND_ALT_COLOR.get();
	}
	
	public static Color gdGetFontColor() {
		return SETTING_FONT_COLOR.get();
	}
	
	public static Color gdGetFontAltColor() {
		return SETTING_FONT_ALT_COLOR.get();
	}
	
	public static Color gdGetCoverColor() {
		return SETTING_COVER_COLOR.get();
	}
	
	public static Color gdGetBorderColor() {
		return SETTING_BORDER_COLOR.get();
	}
	
	public static Color gdGetBorderFocusedColor() {
		return SETTING_BORDER_FOCUSED_COLOR.get();
	}
	
	public static Color gdGetBorderHoveredColor() {
		return SETTING_BORDER_HOVERED_COLOR.get();
	}
	
	public static int gdGetLine() {
		return SETTING_LINE.get();
	}
	
	public static Color gdDarker(Color src) {
		return src.clone().multiply(DARKER_MULTIPLIER);
	}
	
	public static Color gdLighter(Color src) {
		return src.clone().multiply(LIGHTER_MULTIPLIER);
	}
	
}
