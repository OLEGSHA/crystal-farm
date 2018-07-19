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
package ru.windcorp.crystalfarm.content.basic.test;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.content.basic.Units;
import ru.windcorp.crystalfarm.logic.GameManager;
import ru.windcorp.crystalfarm.logic.FullGridTile;
import ru.windcorp.crystalfarm.logic.Island;
import ru.windcorp.crystalfarm.logic.Level;
import ru.windcorp.crystalfarm.logic.server.World;
import ru.windcorp.crystalfarm.translation.TString;

public class TestTile extends FullGridTile {
	
	private static final TString NAME = TString.wrap("Test Tile Name");

	public TestTile() {
		super(InbuiltMod.INST, "testTile", NAME,
				4, 400 * Units.MILLISECONDS,
				3, 300 * Units.MILLISECONDS);
	}
	
	@Override
	protected FullGridTile clone() {
		FullGridTile t = super.clone();

		if (GameManager.GENERIC_RANDOM.nextBoolean()) {
			t.setTickable(true);
		}
		
		return t;
	}
	
	@Override
	public void tick(World world, Island island, Level level, long length, long time) {
		getTexture().setState(time % (10 * Units.SECONDS) > (5 * Units.SECONDS) ? 0 : 1);
	}

}
