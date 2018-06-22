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

import ru.windcorp.crystalfarm.graphics.texture.SimpleTexture;
import ru.windcorp.crystalfarm.gui.Button;
import ru.windcorp.crystalfarm.gui.Component;
import ru.windcorp.crystalfarm.gui.GuiLayer;
import ru.windcorp.crystalfarm.gui.Image;
import ru.windcorp.crystalfarm.gui.Label;
import ru.windcorp.crystalfarm.gui.layout.LayoutBorderVertical;
import ru.windcorp.crystalfarm.gui.layout.LayoutVertical;
import ru.windcorp.crystalfarm.input.Input;
import ru.windcorp.crystalfarm.translation.TString;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class LayerFailure extends GuiLayer {

	public LayerFailure(Exception e, String code, String format, Object... args) {
		super("Failure");
		
		ExecutionReport.reportError(e, null, format, args);
		
		Component root = new Component(getName()) {
			@Override
			protected void renderSelf() {
				GraphicsInterface.fillRectangle(
						getX(),
						getY(),
						getWidth(),
						getHeight(),
						Color.RED);
			}
		};
		root.setLayout(new LayoutBorderVertical());
		
			Component centered = new Component(root.getName() + ".centered");
			centered.setLayout(new LayoutVertical());
			
			centered.addChild(new Image(centered.getName() + ".icon", SimpleTexture.get("mascot/broken")).center());
			centered.addChild(new Label(centered.getName() + ".title", TString.translated("failure.title").toFont().setBold(true)).center());
			centered.addChild(new Label(centered.getName() + ".subtitle", TString.translated("failure.subtitle." + code).toFont().setBold(true)).center());
			centered.addChild(new Label(centered.getName() + ".text", TString.translated("failure.text." + code).toFormatted(args).toFont()).center());
		
		root.addChild(centered.center().setLayoutHint(LayoutBorderVertical.CENTER));
		root.addChild(new Button(getName() + ".close", TString.translated("menu.generic.close").toFont(),
				x -> GraphicsInterface.removeLayer(this)).center().setLayoutHint(LayoutBorderVertical.DOWN));
		
		setRoot(root);
	}
	
	@Override
	public void onInput(Input input) {
		super.onInput(input);
		input.consume();
	}
	
	public static void displayFailure(Exception e, String code, String format, Object... args) {
		LayerFailure layer = new LayerFailure(e, code, format, args);
		GraphicsInterface.addLayer(layer);
	}

}
