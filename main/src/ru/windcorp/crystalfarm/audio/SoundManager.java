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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import ru.windcorp.crystalfarm.CrystalFarmResourceManagers;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.grh.Resource;

public class SoundManager {
	
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
		return CrystalFarmResourceManagers.RM_ASSETS.getResource("sound/" + name + ".ogg");
	}

	private static Sound load(String name) {
		Resource resource = getResource(name);
		
		String problem = resource.canRead();
		if (problem != null) {
			ExecutionReport.reportCriticalError(null,
					ExecutionReport.rscMissing(resource.toString(), problem), null);
			return null;
		}
		
		AudioInputStream inputStream = null;
		try {
			AudioSystem.getAudioInputStream(resource.getInputStream());
		} catch (UnsupportedAudioFileException e) {
			ExecutionReport.reportCriticalError(null,
					ExecutionReport.rscNotSupp(resource.toString(), "Audio format not supported"), null);
			return null;
		} catch (IOException e) {
			ExecutionReport.reportCriticalError(null,
					ExecutionReport.rscCorrupt(resource.toString(), "Could not read or parse sound file"), null);
			return null;
		}
		
		WaveData waveData = WaveData.create(inputStream);
		Sound sound = createSound(name, waveData);
		
		SOUNDS.put(name, sound);
		
		return sound;
	}

	private static Sound createSound(String name, WaveData data) {
		
		// TODO create sound object and return it
		
		System.err.println("Called auto-generated method SoundManager.SoundManager");
		return null;
	}

}
