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

public class AudioInterface {
	
	/** Buffers hold sound data. */
	private final static IntBuffer BUFFERS = BufferUtils.createIntBuffer(1);
	/** Sources are points emitting sound. */
	private final static IntBuffer SOURCES = BufferUtils.createIntBuffer(1);
	
	public static void play(Sound sound, float volume, float pitch) {
		alSourcePlay(sound.getBufferId());
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
	
}
