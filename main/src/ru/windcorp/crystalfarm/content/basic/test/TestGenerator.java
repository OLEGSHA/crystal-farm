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

import ru.windcorp.crystalfarm.logic.Biome;
import ru.windcorp.crystalfarm.logic.BiomeProcessor;
import ru.windcorp.crystalfarm.logic.GridTileLevel;
import ru.windcorp.crystalfarm.logic.Island;

public class TestGenerator implements BiomeProcessor {

	@Override
	public void process(Island island, Biome biome) {
		@SuppressWarnings("unchecked")
		GridTileLevel<TestTile> level = island.getLevel("TestLevel", GridTileLevel.class);
		
		for (int x = 0; x < level.getSize(); ++x) {
			for (int y = 0; y < level.getSize(); ++y) {
				level.setTile(new TestTile(), x, y);
			}
		}
	}

}
