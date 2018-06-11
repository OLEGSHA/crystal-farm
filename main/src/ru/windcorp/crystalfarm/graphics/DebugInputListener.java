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

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.input.Input;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.tge2.util.debug.Log;

public class DebugInputListener implements InputListener {
	
	private boolean debugEnabled;

	@Override
	public void onInput(Input input) {
		if (input instanceof KeyInput) {
			
			KeyInput i = (KeyInput) input;
			
			if (!i.isPressed() || !i.hasAlt()) {
				return;
			}
			
			if (i.getKey() == GLFW.GLFW_KEY_APOSTROPHE) {
				// TODO: add visual notifier
				debugEnabled = !debugEnabled;
				Log.info("Debug keyboard shortcuts " + (debugEnabled ? "enabled" : "disabled"));
				i.consume();
				return;
			}
			
			if (!debugEnabled) {
				return;
			}
			
			switch (i.getKey()) {
			case GLFW.GLFW_KEY_L:
				Log.info("Dumping layers");
				Log.report("\n" + GraphicsInterface.dumpLayers());
				Log.info("Dump complete");
			}
			
		}
	}

}
