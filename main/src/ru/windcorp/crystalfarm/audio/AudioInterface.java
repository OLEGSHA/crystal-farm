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
import static ru.windcorp.crystalfarm.audio.ModuleAudioInterface.*;

import java.nio.IntBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import ru.windcorp.tge2.util.synch.SynchUtil;

public class AudioInterface {
	
	private static IntBuffer sources = null;
	private static final Queue<Integer> SOURCE_QUEUE = new LinkedList<>();
	private static final Map<Integer, Sound> SOURCE_SOUNDS = Collections.synchronizedMap(new HashMap<>());
	
	private static long device;
	private static boolean isAudioReady = false;
	
	/**
	 * Plays the given sound.
	 * @param sound - the sound to play
	 * @param gain - sound volume
	 * @param xPosition - sound position at X axis
	 * @param yPosition - sound position at Y axis
	 * @param pitch - pitch change
	 */
	public static void play(Sound sound, float gain, float xPosition, float yPosition, float pitch) {
		int source = nextSource();
		
		if (isSourceActive(source)) {
			alSourceStop(source);
		}
		
		alSourcei(source,	AL_BUFFER,		sound.getBufferName());
		alSourcef(source,	AL_PITCH,		pitch);
		alSourcef(source,	AL_GAIN,		gain * GAIN.get());
		alSource3f(source,	AL_POSITION,	xPosition, yPosition, 0);
		
		alSourcePlay(source);
		SOURCE_SOUNDS.put(source, sound);
	}
	
	/**
	 * Plays the given sound. Pitch is preserved, sound is played at (0; 0).
	 * @param sound - the sound to play
	 * @param gain - sound volume
	 */
	public static void play(Sound sound, float gain) {
		play(sound, gain, 0, 0, 1.0f);
	}
	
	/**
	 * Plays the given sound and waits for it to stop.
	 * @param sound - the sound to play
	 * @param gain - sound volume
	 * @param xPosition - sound position at X axis
	 * @param yPosition - sound position at Y axis
	 * @param pitch - pitch change
	 */
	public static void playCompletely(Sound sound, float gain, float xPosition, float yPosition, float pitch) {
		play(sound, gain, xPosition, yPosition, pitch);
		SynchUtil.pause(sound.getLength());
	}
	
	/**
	 * Plays the given sound and waits for it to stop. Pitch is preserved, sound is played at (0; 0).
	 * @param sound - the sound to play
	 * @param gain - sound volume
	 */
	public static void playCompletely(Sound sound, float gain) {
		playCompletely(sound, gain, 0, 0, 1.0f);
	}
	
	/**
	 * Stops the given sound if it is playing, fails silently otherwise.
	 * @param sound the sound to stop
	 */
	public static void stop(Sound sound) {
		Integer source = getSourceBySound(sound);
		if (source == null) return;
		alSourceStop(source);
	}
	
	/**
	 * Sets the sources to use.
	 * @param buffer a buffer that contains source names
	 */
	static void setSources(IntBuffer buffer) {
		sources = buffer;
		
		sources.rewind();
		while (sources.remaining() != 0) {
			SOURCE_QUEUE.add(sources.get());
		}
		sources.rewind();
	}
	
	/**
	 * Gets all source names.
	 * @return a buffer containing all source names
	 */
	static IntBuffer getSources() {
		return sources;
	}
	
	/**
	 * Returns a source that can be used to play the next sound with.
	 * This method first attempts to pick a source that is not playing any sound.
	 * If all sources are occupied then it picks the source that has started first.
	 * @return a source name
	 */
	private static Integer nextSource() {
		synchronized (SOURCE_QUEUE) {
			Integer result = null;
			
			for (Integer source : SOURCE_QUEUE) {
				if (!isSourceActive(source)) {
					result = source;
				}
			}
			
			if (result == null) result = SOURCE_QUEUE.poll();
			SOURCE_QUEUE.add(result);
			return result;
		}
	}
	
	/**
	 * Finds a source that is currently playing the given sound.
	 * If there are multiple sources playing it, returns one
	 * arbitrarily.
	 * @param sound the sound to look up
	 * @return a source name or null if none found
	 */
	private static Integer getSourceBySound(Sound sound) {
		for (Map.Entry<Integer, Sound> entry : SOURCE_SOUNDS.entrySet()) {
			if (isSourceActive(entry.getKey()) && entry.getValue() == sound) {
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	/**
	 * Checks whether a source is considered active by OpenAL.
	 * @param source the name of the source to check
	 * @return true when source is
	 * {@link org.lwjgl.openal.AL10#AL_PLAYING AL_PLAYING} or 
	 * {@link org.lwjgl.openal.AL10#AL_PAUSED AL_PAUSED}.
	 */
	private static boolean isSourceActive(int source) {
		int state = alGetSourcei(source, AL_SOURCE_STATE);
		return state == AL_PLAYING || state == AL_PAUSED;
	}

	/**
	 * Creates a complete list of OpenAL buffers.
	 * @return a buffer with names of all OpenAL buffers
	 */
	static IntBuffer getBuffers() {
		return SoundManager.getBuffers();
	}

	/**
	 * Gets the OpenAL device handle.
	 * @return the device handle
	 */
	static long getDevice() {
		return device;
	}

	/**
	 * Sets the OpenAL device handle.
	 * @param device the device handle
	 */
	static void setDevice(long device) {
		AudioInterface.device = device;
	}

	/**
	 * Checks whether {@link AudioInterface} is ready to be used.
	 * @return true when {@link AudioInterface} is ready to be used.
	 */
	public static boolean isAudioReady() {
		return isAudioReady;
	}

	/**
	 * Marks {@link AudioInterface} as ready for use.
	 */
	static void setAudioReady() {
		AudioInterface.isAudioReady = true;
	}
	
}
