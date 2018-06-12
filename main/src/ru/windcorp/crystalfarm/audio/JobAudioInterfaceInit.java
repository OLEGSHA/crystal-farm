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
import java.nio.FloatBuffer;
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

		long context = alcCreateContext(getDevice(), (IntBuffer)null);
		alcMakeContextCurrent(context);
		
		createCapabilities(deviceCaps);
		
		/** Position of the listener. */
		FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
		listenerPos.rewind();
		
		/** Velocity of the listener. */
		FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
		listenerVel.rewind();
		
		/** Orientation of the listener. (first 3 elements are "at", second 3 are "up") */
		FloatBuffer listenerOri =
		    BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f });
		listenerOri.rewind();
		
		// Bind the buffer with the source.
		alGenSources(getSources());
		alGenBuffers(getBuffers());
		
		//TODO You can delete it, I guess
		if (alGetError() != AL_NO_ERROR) {
			ExecutionReport.reportCriticalError(null, null,
			"Could not initialize Audio Interface due to OpenAL error (code %d)", alGetError());
		}
		
		alListenerfv(AL_POSITION,		listenerPos);
		alListenerfv(AL_VELOCITY,		listenerVel);
		alListenerfv(AL_ORIENTATION,	listenerOri);
		
		if (alGetError() != AL_NO_ERROR) {
			ExecutionReport.reportCriticalError(null, null,
			"Could not initialize Audio Interface due to OpenAL error (code %d)", alGetError());
		}
		
		Log.debug("OpenAL initialized successfully");
	}
	
}
