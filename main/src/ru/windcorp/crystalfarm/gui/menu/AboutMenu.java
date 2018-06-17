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

import java.io.IOException;
import java.nio.charset.Charset;

import ru.windcorp.crystalfarm.CrystalFarm;
import ru.windcorp.crystalfarm.CrystalFarmResourceManagers;
import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.texture.SimpleTexture;
import ru.windcorp.crystalfarm.gui.Image;
import ru.windcorp.crystalfarm.gui.Label;
import ru.windcorp.crystalfarm.translation.TString;
import ru.windcorp.tge2.util.StringUtil;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class AboutMenu extends MenuLayer {

	public AboutMenu() {
		super("AboutMenu", true);
		
		add(new Image(getName() + ".title", SimpleTexture.get("title")).center());
		
		add(new Label(getName() + ".main", TString.translated("menu.MainMenu.notice").toFormatted(
				CrystalFarm.FULL_NAME,
				CrystalFarm.VERSION_CODENAME + "/" + CrystalFarm.VERSION,
				StringUtil.iteratorToString(CrystalFarm.DEVELOPERS.iterator(), ", "),
				CrystalFarm.YEARS,
				CrystalFarm.LICENSE)
				.toFont().setColor(Color.BLACK).setBold(true)));
		
		try {
			add(new Label(getName() + ".mcleod",
					TString.wrap(
							StringUtil.readToString(
									CrystalFarmResourceManagers.RM_ASSETS.getInputStream("audio/music/mcleodcredit.txt"),
									Charset.forName("UTF-8"),
									1024)
							)
					.toFont().setColor(Color.BLACK)));
		} catch (IOException e) {
			ExecutionReport.reportError(e,
					ExecutionReport.rscCorrupt(CrystalFarmResourceManagers.RM_ASSETS.getCanonicalPath("audio/music/mcleodcredit.txt"),
							"Could not read McLeod credit"),
					null);
		}
	}
	
}
