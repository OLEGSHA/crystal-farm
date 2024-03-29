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
package ru.windcorp.crystalfarm.cfg.rw;

import ru.windcorp.crystalfarm.cfg.ConfigurationSyntaxException;
import ru.windcorp.crystalfarm.input.KeyStroke;

public class SettingRWKeyStroke extends SettingRW<KeyStroke> {

	public SettingRWKeyStroke() {
		super("Inbuilt-KeyStroke",
				KeyStroke.class,
				
				declar -> {
					try{
						return KeyStroke.fromString(declar);
					} catch (IllegalArgumentException e) {
						throw new ConfigurationSyntaxException("\"" + declar + "\" does not denote a correct key stroke", e);
					}
				},
				
				keyStroke -> keyStroke.toString());
	}

}
