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
import ru.windcorp.crystalfarm.CrystalFarmResourceManagers;
import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.texture.SimpleTexture;
import ru.windcorp.crystalfarm.gui.Aligner;
import ru.windcorp.crystalfarm.gui.Button;
import ru.windcorp.crystalfarm.gui.Component;
import ru.windcorp.crystalfarm.gui.GuiLayer;
import ru.windcorp.crystalfarm.gui.GuiSettingEditors;
import ru.windcorp.crystalfarm.gui.Image;
import ru.windcorp.crystalfarm.gui.Label;
import ru.windcorp.crystalfarm.gui.Size;
import ru.windcorp.crystalfarm.gui.layout.LayoutVertical;
import ru.windcorp.crystalfarm.logic.GameManager;
import ru.windcorp.crystalfarm.translation.ModuleTranslation;
import ru.windcorp.crystalfarm.translation.TString;
import ru.windcorp.tge2.util.StringUtil;

public class MainMenu extends GuiLayer {

	public MainMenu() {
		super("MainMenu");
		
		Component root = new Component("MainMenuBg");
		root.setLayout(null);
		
		Component centered = new Component("MainMenuCentered");
		
			centered.addChild(new Image("MainMenu.logo", SimpleTexture.get("title")));
			
			Component menu = new Menu("MainMenu", TString.translated("menu.MainMenu.title").toFont());
				
				menu.addChild(
						new Button(
								"MainMenu.play",
								TString.translated("menu.MainMenu.play").toFont(),
								button -> GameManager.startLocalGame(CrystalFarmResourceManagers.RM_FILE_WD.getResource("testWorld.earthpony.cfsave"))
						).takeFocus());
				
				menu.addChild(
						new Button(
								"MainMenu.settings",
								TString.translated("menu.MainMenu.settings").toFont(),
								button -> new SettingsMenu().show()
						));

				menu.addChild(
						new Button(
								"MainMenu.about",
								TString.translated("menu.MainMenu.about").toFont(),
								button -> new AboutMenu().show()
						));
				
				menu.addChild(
						new Button(
								"MainMenu.guiTest",
								TString.translated("TMP_1.5").toFont(),
								button -> new TestMenu().show()
						));
				
				menu.addChild(
						new Button(
								"MainMenu.exit",
								TString.translated("menu.MainMenu.exit").toFont(),
								button -> CrystalFarm.exit("User request", 0)
						));
			
			centered.addChild(menu);
			centered.setLayout(new LayoutVertical(3));
		
		root.addChild(new Aligner(centered) {
			@Override
			protected synchronized void layoutSelf() {
				setBounds(
						root.getX(),
						root.getY(),
						root.getWidth(),
						root.getHeight());
				
				super.layoutSelf();
			}
		});
		
		root.addChild(new Aligner(GuiSettingEditors.createLimitedChoiceEditor(ModuleTranslation.LANGUAGE, ModuleTranslation.getAvailableLanguages())) {
			@Override
			protected synchronized void layoutSelf() {
				Size preferred = getPreferredSize();
				setBounds(
						root.getX() + root.getWidth() - preferred.width,
						root.getY() + root.getHeight() - preferred.height,
						preferred.width,
						preferred.height);
				
				super.layoutSelf();
			}
		});
		
		root.addChild(new Label("MainMenu.notice", TString.translated("menu.MainMenu.notice").toFormatted(
				CrystalFarm.FULL_NAME,
				CrystalFarm.VERSION_CODENAME + "/" + CrystalFarm.VERSION,
				StringUtil.iteratorToString(CrystalFarm.DEVELOPERS.iterator(), ", "),
				CrystalFarm.YEARS,
				CrystalFarm.LICENSE)
				.toFont().setColor(Color.GRAY)) {
			@Override
			protected synchronized void layoutSelf() {
				Size preferred = getPreferredSize();
				setBounds(
						root.getX(),
						root.getY() + root.getHeight() - preferred.height,
						preferred.width,
						preferred.height);
				
				super.layoutSelf();
			}
		});
		
		setRoot(root);
		
	}

}
