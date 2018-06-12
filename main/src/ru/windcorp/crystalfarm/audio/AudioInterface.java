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

import static org.lwjgl.openal.AL10.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public class AudioInterface {
	
	/** Buffers hold sound data. */
	private final static IntBuffer BUFFERS = BufferUtils.createIntBuffer(1);
	/** Sources are points emitting sound. */
	private final static IntBuffer SOURCES = BufferUtils.createIntBuffer(1);
	
	private static long device;
	
	public static void play(Sound sound, float volume, float pitch) {
		/** Position of the source sound. */
		FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
		sourcePos.rewind();
		
		/** Velocity of the source sound. */
		FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
		sourceVel.rewind();
		 
		AL10.alSourcei(getSources().get(0), AL10.AL_BUFFER, getBuffers().get(0));
		AL10.alSourcef(getSources().get(0), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(getSources().get(0), AL10.AL_GAIN, 1.0f);
		AL10.alSourcefv(getSources().get(0), AL10.AL_POSITION, sourcePos);
		AL10.alSourcefv(getSources().get(0), AL10.AL_VELOCITY, sourceVel);
		alSourcePlay(getSources().get(0));
	}

	public static IntBuffer getSources() {
		return SOURCES;
	}

	public static IntBuffer getBuffers() {
		return BUFFERS;
	}
	
	public static int getBuffer(int bufferId) {
		return getBuffers().get(bufferId);
	}

	public static long getDevice() {
		return device;
	}

	static void setDevice(long device) {
		AudioInterface.device = device;
	}
	
}
