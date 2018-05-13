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
package ru.windcorp.crystalfarm;

import java.util.ArrayList;
import java.util.List;

import ru.windcorp.tge2.util.Version;

public class CrystalFarm {

	public static final String INTERNAL_NAME = "crystal-farm";
	public static final String FULL_NAME = "Crystal Farm";
	
	public static final String LICENSE = "GNU General Public License version 3 or any later version";
	public static final List<String> DEVELOPERS = new ArrayList<>();
	public static final String YEARS = "2018";
	
	public static final Version VERSION = new Version(0, 0, 0);
	public static final String VERSION_CODENAME = "pre-alpha";
	
	static {
		DEVELOPERS.add("OLEGSHA");
		DEVELOPERS.add("Neiroc");
	}
	
}
