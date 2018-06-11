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
package ru.windcorp.crystalfarm.audio;

import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
 
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

public class JobAudioInterfaceInit extends ModuleJob {
	
	public JobAudioInterfaceInit(String name, String description, Module module) {
		super(name, description, module);
		System.err.println("Called auto-generated constructor for type JobAudioInterfaceInit");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void runImpl() {
		
	}
		 
}
