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
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.cfg.SettingFloat;
import ru.windcorp.crystalfarm.cfg.SettingInt;
import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.jobs.JobManager;

public class ModuleAudioInterface extends Module {

	public static final SettingFloat GAIN = new SettingFloat("Gain", "Sets master audio volume [0.0; 1.0]", 1, 1, 0, 0.1f);
	static final SettingInt SOURCES = new SettingInt("AudioSources", "Sets the amount of audio sources (sounds that can be played simultaneously)", 64, 1024, 4, 4);
	public static final SettingFloat GAIN_MUSIC = new SettingFloat("MusicGain", "Sets music volume [0.0; 1.0]", 0.4f, 1, 0, 0.1f);

	static boolean isALInitialized = false;

	public ModuleAudioInterface() {
		super("AudioInterface", InbuiltMod.INST);
		
		addConfig(GAIN);
		addConfig(SOURCES);
		addConfig(GAIN_MUSIC);
	}

	@Override
	public void registerJobs(JobManager<ModuleJob> manager) {
		manager.addJob(new JobAudioInterfaceInit(this));
		manager.addJob(new JobMusicInit(this));
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (!isALInitialized) {
				return;
			}
			
			AL10.alDeleteSources(AudioInterface.getSources());
			AL10.alDeleteBuffers(AudioInterface.getBuffers());
			ALC10.alcCloseDevice(AudioInterface.getDevice());
			ALC.destroy();
		}));
	}

}
