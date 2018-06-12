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

import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;

public class TMP_JobTestAudio extends ModuleJob {

	public TMP_JobTestAudio(Module module) {
		super("TMP_JobTestAudio", "Debug job to fiddle with audio", module);

		addDependency("Inbuilt:AudioInterface:AudioInterfaceInit");
		addDependency("Inbuilt:GraphicsInterface:TMP_JobTestGUI");
	}

	@Override
	protected void runImpl() {
		Sound sound = SoundManager.get("rat");
		AudioInterface.play(sound, 0, 0);
		
	}

}
