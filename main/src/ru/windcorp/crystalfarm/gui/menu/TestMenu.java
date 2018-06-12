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

import ru.windcorp.crystalfarm.graphics.fonts.GString;
import ru.windcorp.crystalfarm.gui.Button;
import ru.windcorp.crystalfarm.struct.modules.Module;

public class TestMenu extends MenuLayer {

	public TestMenu(Module module) {
		super("TestMenu", true, module);
		
		Button button = new Button("ExampleButton", new GString(module, "menu.test.button"), null);
		
		add(button);
	}

}
