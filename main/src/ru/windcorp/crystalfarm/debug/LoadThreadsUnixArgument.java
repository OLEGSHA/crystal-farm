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
package ru.windcorp.crystalfarm.debug;

import java.lang.reflect.InvocationTargetException;

import ru.windcorp.crystalfarm.CrystalFarmLauncher;
import ru.windcorp.tge2.util.unixarg.UnixArgument;
import ru.windcorp.tge2.util.unixarg.UnixArgumentInvalidSyntaxException;

public class LoadThreadsUnixArgument extends UnixArgument<Integer> {

	public LoadThreadsUnixArgument() {
		super("loadThreads", null,
				"Specifies amount of load threads. Use -1 for automatic detection",
				Integer.class,
				false, true, false);
	}

	@Override
	protected boolean runImpl(Integer arg) throws UnixArgumentInvalidSyntaxException, InvocationTargetException {
		if (arg < 1) {
			CrystalFarmLauncher.setLoadThreads(Runtime.getRuntime().availableProcessors());
		} else {
			CrystalFarmLauncher.setLoadThreads(arg);
		}
		return false;
	}

}
