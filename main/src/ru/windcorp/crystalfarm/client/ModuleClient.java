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
package ru.windcorp.crystalfarm.client;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.cfg.SettingBoolean;
import ru.windcorp.crystalfarm.logic.action.ActionRegistry;
import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.jobs.JobManager;

public class ModuleClient extends Module {
	
	public static final SettingBoolean DRAW_COLLISION_BOUNDS = new SettingBoolean(
			"DrawCollisionBounds", "When true, all collideable tiles draw their collision bounds",
			false);

	public ModuleClient() {
		super("Client", InbuiltMod.INST);
		
		addConfig(ActionRegistry.ROOT_SECTION);
		addConfig(DRAW_COLLISION_BOUNDS);
	}
	
	@Override
	public void registerJobs(JobManager<ModuleJob> manager) {
		// Do nothing
	}
	
}
