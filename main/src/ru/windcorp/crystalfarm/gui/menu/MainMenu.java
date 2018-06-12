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

import ru.windcorp.crystalfarm.CrystalFarm;
import ru.windcorp.crystalfarm.graphics.fonts.GString;
import ru.windcorp.crystalfarm.graphics.texture.SimpleTexture;
import ru.windcorp.crystalfarm.gui.Button;
import ru.windcorp.crystalfarm.gui.Image;
import ru.windcorp.crystalfarm.gui.layout.LayoutBorder;
import ru.windcorp.tge2.util.debug.Log;

public class MainMenu extends MenuLayer {

	public MainMenu() {
		super("MainMenu", false);
		
		getRoot().addChild(new Image("MainMenu.logo", SimpleTexture.get("logo")).setLayoutHint(LayoutBorder.UP));

		Button button1 = new Button("1.3", new GString("TMP_1.3"), null);
		button1.addAction(button -> {
			getContents().removeChild(button1);
		});
		
		add(new Button("1.1", new GString("TMP_1.1"), button -> Log.info(button + " activated")),
				new Button("1.2", new GString("TMP_1.2"), button -> Log.info(button + " activated")),
				button1,
				new Button("1.5", new GString("TMP_1.5"), button -> new TestMenu().show()),
				new Button("1.4", new GString("TMP_1.4"), button -> CrystalFarm.exit("user request", 0)));
		
	}
	
	@Override
	public void close() {
		// Do nothing
	}

}
