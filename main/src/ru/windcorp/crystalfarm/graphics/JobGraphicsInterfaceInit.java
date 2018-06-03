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

import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.debug.Log;

public class JobGraphicsInterfaceInit extends ModuleJob {

	public JobGraphicsInterfaceInit(Module module) {
		super("GraphicsInterfaceInit", "Initializes OpenGL and GLFW", module);
		
		addDependency("Inbuilt:Configuration:LoadConfig");
	}

	@Override
	protected void runImpl() {
		Log.debug("Launching render thread");
		new Thread(new WindowHandler()).start();
	}

}
