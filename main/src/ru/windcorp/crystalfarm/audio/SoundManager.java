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

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_close;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_get_info;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_get_samples_short_interleaved;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_open_memory;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_stream_length_in_samples;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbisInfo;

import ru.windcorp.crystalfarm.CrystalFarmResourceManagers;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.grh.Resource;

public class SoundManager {
	
	private static class DelayedLoad {
		final Sound sound;
		final ShortBuffer pcm;
		final int format;
		final int sampleRate;
		
		DelayedLoad(Sound sound, ShortBuffer pcm, int format, int sampleRate) {
			this.sound = sound;
			this.pcm = pcm;
			this.format = format;
			this.sampleRate = sampleRate;
		}
	}
	
	private static final Queue<DelayedLoad> DELAYED_LOAD_QUEUE = new ConcurrentLinkedQueue<>();
	
	private static final Map<String, Sound> SOUNDS = Collections.synchronizedMap(new HashMap<>());
	
	public static Sound get(String name) {
		synchronized (SOUNDS) {
			Sound sound = SOUNDS.get(name);
			if (sound == null) {
				sound = load(name);
			}
			return sound;
		}
	}
	
	private static Resource getResource(String name) {
		return CrystalFarmResourceManagers.RM_ASSETS.getResource("audio/" + name + ".ogg");
	}

	private synchronized static Sound load(String name) {
		Resource resource = getResource(name);
		
		String problem = resource.canRead();
		if (problem != null) {
			ExecutionReport.reportCriticalError(null,
					ExecutionReport.rscMissing(resource.toString(), problem), null);
			return null;
		}
		
		ShortBuffer pcm;
		int format, sampleRate;
		
		try {
			try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
	            pcm = readVorbis(resource, 32 * 1024, info);
	            format = info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16;
	            sampleRate = info.sample_rate();
	        }
		} catch (IOException e) {
			ExecutionReport.reportCriticalError(e,
					ExecutionReport.rscCorrupt(resource.toString(), "Could not read or parse sound file"), null);
			return null;
		}
		
		Sound sound = new Sound(name);
		SOUNDS.put(name, sound);
		
		if (AudioInterface.isAudioReady()) {
			loadInAL(sound, pcm, format, sampleRate);
		} else {
			DELAYED_LOAD_QUEUE.add(new DelayedLoad(sound, pcm, format, sampleRate));
		}
		
		return sound;
	}
	
	private static void loadInAL(Sound sound, ShortBuffer pcm, int format, int sampleRate) {
		Log.critical("Loading " + sound.getName());
		int bufferId = AL10.alGenBuffers();
		alBufferData(bufferId, format, pcm, sampleRate);
		sound.setBufferId(bufferId);
	}
	
	static synchronized void processQueueAndSetAudioReady() {
		DELAYED_LOAD_QUEUE.forEach(delayed -> loadInAL(delayed.sound, delayed.pcm, delayed.format, delayed.sampleRate));
		AudioInterface.setAudioReady(true);
	}

	/*
	 * Method readVorbis() is imported from LWJGL OpenAL Demo and modified
	 */
    private static ShortBuffer readVorbis(Resource resource, int bufferSize, STBVorbisInfo info) throws IOException {
        ByteBuffer vorbis  = ioResourceToByteBuffer(resource, bufferSize);
        IntBuffer  error   = BufferUtils.createIntBuffer(1);
        long       decoder = stb_vorbis_open_memory(vorbis, error, null);
        if (decoder == NULL) {
            throw new IOException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
        }
        
        stb_vorbis_get_info(decoder, info);

        int channels = info.channels();

        int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

        ShortBuffer pcm = BufferUtils.createShortBuffer(lengthSamples);

        pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
        stb_vorbis_close(decoder);

        return pcm;
    }

	private static ByteBuffer ioResourceToByteBuffer(Resource resource, int bufferSize) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bufferSize);
		InputStream in = resource.getInputStream();
		
		// Sync'ing once to avoid excessive monitor checks in baos.write()
		synchronized (baos) {
			byte[] buffer = new byte[bufferSize];
			int read;
			do {
				read = in.read(buffer);
				baos.write(buffer);
			} while (read == bufferSize);

			ByteBuffer result = ByteBuffer.allocateDirect(baos.size());
			result.order(ByteOrder.nativeOrder());
			result.put(baos.toByteArray());
			result.flip();
			return result;
		}
	}

	static IntBuffer getBuffers() {
		synchronized (SOUNDS) {
			IntBuffer buffers = IntBuffer.allocate(SOUNDS.size());
			
			SOUNDS.forEach((name, sound) -> {
				buffers.put(sound.getBufferId());
			});
			
			return buffers;
		}
	}

}
