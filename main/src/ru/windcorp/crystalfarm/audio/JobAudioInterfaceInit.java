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

import static ru.windcorp.crystalfarm.audio.AudioInterface.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC.*;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.ALCCapabilities;

import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class JobAudioInterfaceInit extends ModuleJob {

	public JobAudioInterfaceInit(Module module) {
		super("AudioInterfaceInit", "Initializes OpenAL", module);
		
		addDependency("Inbuilt:Configuration:LoadConfig");
	}


	protected void runImpl() {
		Log.info("OpenAL initialization");
		
		setDevice(alcOpenDevice((ByteBuffer) null));
		ALCCapabilities deviceCaps = createCapabilities(getDevice());
		ModuleAudioInterface.isALInitialized = true;

		long context = alcCreateContext(getDevice(), (IntBuffer) null);
		alcMakeContextCurrent(context);
		createCapabilities(deviceCaps);
		
		// Bind the buffer with the source.
		IntBuffer sources = BufferUtils.createIntBuffer(ModuleAudioInterface.SOURCES.get());
		alGenSources(sources);
		setSources(sources);
		
		SoundManager.processQueueAndSetAudioReady();
		
		if (alGetError() != AL_NO_ERROR) {
			ExecutionReport.reportCriticalError(null, null,
			"Could not initialize Audio Interface due to OpenAL error (code %d)", alGetError());
		}
		
		Log.debug("OpenAL initialized successfully");
	}
	
}
