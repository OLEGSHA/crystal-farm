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
package ru.windcorp.crystalfarm.content.basic;

import ru.windcorp.crystalfarm.logic.Collideable;
import ru.windcorp.crystalfarm.logic.FullGridTile;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.translation.TString;

public abstract class GroundTile extends FullGridTile implements Collideable {
	
	private boolean canCollide = false;

	public GroundTile(Mod mod, String id, TString name, int... textureData) {
		super(mod, id, name, textureData);
	}

	@Override
	public boolean canCollide() {
		return canCollide;
	}
	
	public void setCanCollide(boolean canCollide) {
		if (canCollide == this.canCollide) {
			return;
		}
		
		this.canCollide = canCollide;
		
		GroundLevel level = getLevel();
		if (level != null) {
			if (canCollide) {
				level.getCollideables().add(this);
			} else {
				level.getCollideables().remove(null);
			}
		}
	}
	
	@Override
	public GroundLevel getLevel() {
		return (GroundLevel) super.getLevel();
	}

}
