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

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public class AudioInterface {
	
	/** Sources are points emitting sound. */
	private final static IntBuffer SOURCES = BufferUtils.createIntBuffer(1);
	
	private static long device;
	private static boolean isAudioReady = false;
	
	private static final float[] FLOAT_ZERO3 = new float[] {0, 0, 0};
	
	public static void play(Sound sound, float volume, float pitch) {
		AL10.alSourcei(getSources().get(0), AL10.AL_BUFFER, sound.getBufferId());
		AL10.alSourcef(getSources().get(0), AL10.AL_PITCH, pitch);
		AL10.alSourcef(getSources().get(0), AL10.AL_GAIN, volume);
		AL10.alSourcefv(getSources().get(0), AL10.AL_POSITION, FLOAT_ZERO3);
		AL10.alSourcefv(getSources().get(0), AL10.AL_VELOCITY, FLOAT_ZERO3);
		alSourcePlay(getSources().get(0));
	}

	public static IntBuffer getSources() {
		return SOURCES;
	}

	public static IntBuffer getBuffers() {
		return SoundManager.getBuffers();
	}

	public static long getDevice() {
		return device;
	}

	static void setDevice(long device) {
		AudioInterface.device = device;
	}

	public static boolean isAudioReady() {
		return isAudioReady;
	}

	static void setAudioReady(boolean isAudioReady) {
		AudioInterface.isAudioReady = isAudioReady;
	}
	
}
