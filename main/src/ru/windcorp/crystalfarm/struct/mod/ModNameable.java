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
package ru.windcorp.crystalfarm.struct.mod;

import ru.windcorp.tge2.util.Nameable;

public abstract class ModNameable extends Nameable {
	
	private final Mod mod;

	public ModNameable(Mod mod, String name) {
		super(mod.getName() + ":" + name);
		this.mod = mod;
	}

	public Mod getMod() {
		return mod;
	}
	
	public String getRawName() {
		return getName().substring(getMod().getName().length() + 1);
	}
	
	/*
	 * Names are unique per mod, no need to override equals() and hashCode()
	 */

}
