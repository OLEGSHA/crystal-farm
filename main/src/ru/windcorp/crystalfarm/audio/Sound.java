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

import ru.windcorp.tge2.util.Nameable;
import ru.windcorp.tge2.util.synch.SynchUtil;

/**
 * A Sound represents a loaded sound file. Sounds are associated with a unique OpenAL buffer.
 * New Sounds can be created with {@link SoundManager#get(String)}.
 * 
 * @author OLEGSHA
 */
public class Sound extends Nameable {

	private int bufferName;
	private long length = -1;

	/**
	 * Creates a new Sound that is not associated to an OpenLA buffer with the specified name.
	 * @param name the unique name of the sound
	 * @see {@link SoundManager#get(String)}
	 */
	protected Sound(String name) {
		super(name);
	}

	/**
	 * Gets the associated OpenAL buffer name.
	 * @return the buffer name that can be used by OpenAL functions
	 * @throws IllegalStateException if no buffer has been set
	 */
	public int getBufferName() {
		if (bufferName == 0) {
			throw new IllegalStateException("Sound not initialized yet");
		}
		return bufferName;
	}
	
	/**
	 * Sets the OpenAL buffer name for this Sound.
	 * @param bufferId the name of the buffer
	 */
	void setBufferName(int bufferId) {
		this.bufferName = bufferId;
	}
	
	/**
	 * Gets this Sound's length in milliseconds.
	 * @param the duration of this Sound expressed in milliseconds
	 * @throws IllegalStateException if no buffer has been set
	 */
	public long getLength() {
		if (length == -1) {
			calculateLength();
		}
		
		return length;
	}

	private void calculateLength() {
		if (getBufferName() == 0) {
			throw new IllegalStateException("Sound not initialized yet");
		}
		
		this.length = 1000l * Byte.SIZE
				* alGetBufferi(getBufferName(), AL_SIZE)
				/ alGetBufferi(getBufferName(), AL_BITS)
				/ alGetBufferi(getBufferName(), AL_FREQUENCY)
				/ alGetBufferi(getBufferName(), AL_CHANNELS);
	}
	
	/**
	 * Plays this sound.
	 * @param gain - sound volume
	 * @param xPosition - sound position at X axis
	 * @param yPosition - sound position at Y axis
	 * @param pitch - pitch change
	 */
	public void play(float gain, float xPosition, float yPosition, float pitch) {
		AudioInterface.play(this, gain, xPosition, yPosition, pitch);
	}
	
	/**
	 * Plays this sound. Sound is centered relative to the listener
	 * and is not pitch-shifted.
	 * @param gain - sound volume
	 */
	public void play(float gain) {
		play(gain, 0, 0, 1);
	}
	
	/**
	 * Plays this sound. Gain is 1.0. Sound is centered relative to
	 * the listener and is not pitch-shifted.
	 */
	public void play() {
		play(1, 0, 0, 1);
	}
	
	/**
	 * Plays this sound and waits for it to complete.
	 * @param gain - sound volume
	 * @param xPosition - sound position at X axis
	 * @param yPosition - sound position at Y axis
	 * @param pitch - pitch change
	 */
	public void playCompletely(float gain, float xPosition, float yPosition, float pitch) {
		play(gain, xPosition, yPosition, pitch);
		SynchUtil.pause(getLength());
	}
	
	/**
	 * Plays this sound and waits for it to complete.
	 * Sound is centered relative to the listener and is not pitch-shifted.
	 * @param gain - sound volume
	 */
	public void playCompletely(float gain) {
		playCompletely(gain, 0, 0, 1);
	}
	
	/**
	 * Plays this sound  and waits for it to complete. Gain is 1.0.
	 * Sound is centered relative to the listener and is not pitch-shifted.
	 */
	public void playCompletely() {
		playCompletely(1, 0, 0, 1);
	}
	
	/**
	 * Stops this sound if it is playing, fails silently otherwise.
	 */
	public void stop() {
		AudioInterface.stop(this);
	}
	
	@Override
	public String toString() {
		return "Sound " + getName() +
				" (OpenAL buffer name: " + getBufferName() + "; length: " +
				(getLength() / 60000) + ":" + (getLength() % 60000 / 1000.0) + ")";
	}
	
}
