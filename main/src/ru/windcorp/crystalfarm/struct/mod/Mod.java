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

public abstract class Mod extends Nameable {
	
	private final ModMeta metadata;
	
	public Mod(ModMeta metadata) {
		super(metadata.name);
		this.metadata = metadata;
	}

	public ModMeta getMetadata() {
		return metadata;
	}
	
	public String getUserFriendlyName() {
		return getMetadata().userFriendlyName;
	}

	public abstract void registerModules();
	
	@Override
	public String toString() {
		return getUserFriendlyName() + " (" + getName() + ":" + getMetadata().version + ")";
	}

}
