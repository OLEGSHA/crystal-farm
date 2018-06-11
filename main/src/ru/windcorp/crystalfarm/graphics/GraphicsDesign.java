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

public interface GraphicsDesign {

	public static final Color BACKGROUND_COLOR = Color.WHITE;
	public static final Color BACKGROUND_COLOR_DARKER = Color.LIGHT_GRAY;
	public static final Color FONT_COLOR = Color.WHITE;
	public static final Color FOREGROUND_COLOR = new Color(0xBF_8D_6E_FF);
	public static final Color FOREGROUND_COLOR_LIGHTER = FOREGROUND_COLOR.clone().multiply(1.5);
	public static final Color BORDER_COLOR = new Color(0x7F_5D_49_FF);
	public static final Color BORDER_COLOR_DARKER = BORDER_COLOR.clone().multiply(0.5);
	public static final Color COVER_COLOR = new Color(0xFF_FF_FF_88);
	
	public static final int LINE_THICKNESS = 5;
	
}
