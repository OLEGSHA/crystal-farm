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
package ru.windcorp.crystalfarm.graphics.notifier;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.cfg.SettingFloat;
import ru.windcorp.crystalfarm.cfg.SettingInt;
import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.jobs.JobManager;

public class ModuleNotifier extends Module {
	
	public static final SettingInt SETTING_TIMEOUT =
			new SettingInt("Timeout", "Time, in seconds, that a non-modal notification will persist on the screen", 3);
	public static final SettingInt SETTING_SHAKE_INTERVAL =
			new SettingInt("ShakeInterval", "Time, in seconds, between small shakes for modal notifications", 5);
	public static final SettingFloat SETTING_ALERT_GAIN =
			new SettingFloat("AlertGain", "The volume of audio alerts [0.0; 1.0]", 1);

	public ModuleNotifier() {
		super("Notifier", InbuiltMod.INST);
		
		addConfig(SETTING_TIMEOUT);
		addConfig(SETTING_SHAKE_INTERVAL);
		addConfig(SETTING_ALERT_GAIN);
	}

	@Override
	public void registerJobs(JobManager<ModuleJob> manager) {
		manager.addJob(new JobNotifierInit(this));
	}

}
