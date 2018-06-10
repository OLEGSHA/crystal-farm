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
package ru.windcorp.crystalfarm.graphics;

import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.synch.SynchUtil;

public class TMP_JobDelayLoad extends ModuleJob {

	public TMP_JobDelayLoad(int i, Module module) {
		super("TMP_JobDelayLoad_" + i, "Debug job to simulate lengthy load", module);
		
		if (i == 0) {
			addDependency("Inbuilt:GraphicsInterface:TMP_JobTestGUI");
		} else {
			addDependency("Inbuilt:GraphicsInterface:TMP_JobDelayLoad_" + (i - 1));
		}
	}

	@Override
	protected void runImpl() {
		SynchUtil.pause(1000);
	}

}
