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
import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.texture.SimpleTexture;
import ru.windcorp.crystalfarm.gui.Button;
import ru.windcorp.crystalfarm.gui.Centerer;
import ru.windcorp.crystalfarm.gui.Component;
import ru.windcorp.crystalfarm.gui.GuiLayer;
import ru.windcorp.crystalfarm.gui.GuiSettingEditors;
import ru.windcorp.crystalfarm.gui.Image;
import ru.windcorp.crystalfarm.gui.Label;
import ru.windcorp.crystalfarm.gui.Size;
import ru.windcorp.crystalfarm.gui.layout.LayoutVertical;
import ru.windcorp.crystalfarm.translation.ModuleTranslation;
import ru.windcorp.crystalfarm.translation.TString;
import ru.windcorp.tge2.util.StringUtil;
import ru.windcorp.tge2.util.debug.Log;

public class MainMenu extends GuiLayer {

	public MainMenu() {
		super("MainMenu");
		
		Component root = new Component("MainMenuBg");
		root.setLayout(null);
		
		Component centered = new Component("MainMenuCentered");
		
			centered.addChild(new Image("MainMenu.logo", SimpleTexture.get("title")));
			
			Component menu = new Menu("MainMenu", TString.translated("menu.MainMenu.title").toFont());
	
				Button button1 = new Button("1.3", TString.translated("TMP_1.3").toFont(), null);
				button1.addAction(button -> {
					menu.removeChild(button1);
				});
				
				menu.addChild(new Button("1.1", TString.translated("TMP_1.1").toFont(), button -> Log.info(button + " activated")).takeFocus());
				menu.addChild(new Button("1.2", TString.translated("TMP_1.2").toFont(), button -> Log.info(button + " activated")));
				menu.addChild(button1);
				menu.addChild(new Button("1.5", TString.translated("TMP_1.5").toFont(), button -> new TestMenu().show()));
				menu.addChild(new Button("1.4", TString.translated("TMP_1.4").toFont(), button -> CrystalFarm.exit("user request", 0)));
			
			centered.addChild(menu);
			centered.setLayout(new LayoutVertical());
		
		root.addChild(new Centerer(centered) {
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
		
		root.addChild(new Centerer(GuiSettingEditors.createLimitedChoiceEditor(ModuleTranslation.LANGUAGE, ModuleTranslation.getAvailableLanguages())) {
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
				.toFont().setColor(Color.DARK_GRAY)) {
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
