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

import org.lwjgl.openal.AL10;

import ru.windcorp.tge2.util.Nameable;

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
	void setBufferId(int bufferId) {
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
				* AL10.alGetBufferi(getBufferName(), AL10.AL_SIZE)
				/ AL10.alGetBufferi(getBufferName(), AL10.AL_BITS)
				/ AL10.alGetBufferi(getBufferName(), AL10.AL_FREQUENCY);
	}
	
}
