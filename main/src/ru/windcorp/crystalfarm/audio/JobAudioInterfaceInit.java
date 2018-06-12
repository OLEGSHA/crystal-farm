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

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
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
		
		//ALC.create();
		
		long device = ALC10.alcOpenDevice((ByteBuffer) null);
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		ModuleAudioInterface.isALInitialized = true;

		long context = ALC10.alcCreateContext(device, (IntBuffer)null);
		ALC10.alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCaps);
		
		
		/** Position of the source sound. */
		FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
		sourcePos.rewind();
		
		/** Velocity of the source sound. */
		FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
		sourceVel.rewind();
		
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
		AL10.alGenSources(getSources()); 
		
		//TODO You can delete it, I guess
		if (AL10.alGetError() != AL10.AL_NO_ERROR) {
			ExecutionReport.reportCriticalError(null, null,
			"Could not initialize Audio Interface due to OpenAL error (code %d)", AL10.alGetError());
		}
		 
		AL10.alSourcei(getSources().get(0), AL10.AL_BUFFER, getBuffers().get(0));
		AL10.alSourcef(getSources().get(0), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(getSources().get(0), AL10.AL_GAIN, 1.0f);
		AL10.alSourcefv(getSources().get(0), AL10.AL_POSITION, sourcePos);
		AL10.alSourcefv(getSources().get(0), AL10.AL_VELOCITY, sourceVel);
		
		AL10.alListenerfv(AL10.AL_POSITION,		listenerPos);
		AL10.alListenerfv(AL10.AL_VELOCITY,		listenerVel);
		AL10.alListenerfv(AL10.AL_ORIENTATION,	listenerOri);
		
		if (AL10.alGetError() != AL10.AL_NO_ERROR) {
			ExecutionReport.reportCriticalError(null, null,
			"Could not initialize Audio Interface due to OpenAL error (code %d)", AL10.alGetError());
		}
		
		
		
		Log.debug("OpenAL initialized successfully");
	}
	
}
