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

import ru.windcorp.crystalfarm.gui.Component;
import ru.windcorp.crystalfarm.gui.GuiLayer;
import ru.windcorp.crystalfarm.gui.Layout;
import ru.windcorp.crystalfarm.gui.layout.LayoutCenter;
import ru.windcorp.crystalfarm.gui.layout.LayoutVertical;
import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;

public class TMP_JobTestGUI extends ModuleJob {
	
	static class ColorfulComponent extends Component {
		
		public static final Color FOCUSED_COLOR = new Color(1, 1, 0, 0);
		
		private final Color color;
		
		public ColorfulComponent(String name, Color color, Layout layout) {
			super(name);
			this.color = color;
			setLayout(layout);
		}
		
		@Override
		protected void renderSelf() {
			GraphicsInterface.applyColor(isFocused() ? FOCUSED_COLOR : color);
			GraphicsInterface.fillRectangle(getX(), getY(), getWidth(), getHeight());
			
			if (isHovered()) {
				GraphicsInterface.applyColor(Color.WHITE);
				GraphicsInterface.fillRectangle(getX() + 1, getY() + 1, getWidth() - 2, getHeight() - 2);
			}
		}
		
		@Override
		public boolean isFocusable() {
			return color == Color.BLACK;
		}
	}

	public TMP_JobTestGUI(Module module) {
		super("TMP_JobTestGUI", "Debug job to fiddle with GUI", module);
		
		addDependency("Inbuilt:GraphicsInterface:GraphicsInterfaceInit");
	}

	@Override
	protected void runImpl() {
		
		GuiLayer layer = new GuiLayer("GUI test");
		
		ColorfulComponent root = new ColorfulComponent("Root", Color.BLACK, new LayoutVertical());
		
		ColorfulComponent sub1 = new ColorfulComponent("1", Color.RED, new LayoutVertical());
		sub1.addChild(new ColorfulComponent("1.1", Color.BLACK, new LayoutVertical()));
		sub1.addChild(new ColorfulComponent("1.2", Color.BLACK, new LayoutVertical()));
		sub1.addChild(new ColorfulComponent("1.3", Color.BLACK, new LayoutVertical()));
		sub1.addChild(new ColorfulComponent("1.4", Color.BLACK, new LayoutVertical()));
		
		ColorfulComponent sub2 = new ColorfulComponent("2", Color.GREEN, new LayoutVertical());
		sub2.addChild(new ColorfulComponent("2.1", Color.BLACK, new LayoutVertical()));
		sub2.addChild(new ColorfulComponent("2.2", Color.BLACK, new LayoutVertical()));
		
		root.addChild(sub1);
		root.addChild(sub2);
		root.addChild(new ColorfulComponent("3", Color.BLUE, new LayoutVertical()));
		
		ColorfulComponent bg = new ColorfulComponent("BG", Color.BLUE, new LayoutCenter());
		bg.addChild(root);
		
		layer.setRoot(bg);
		
		GraphicsInterface.addLayer(layer);
		
	}

}
