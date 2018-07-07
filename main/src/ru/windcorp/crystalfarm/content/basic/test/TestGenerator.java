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
import ru.windcorp.crystalfarm.logic.Biome;
import ru.windcorp.crystalfarm.logic.BiomeProcessor;
import ru.windcorp.crystalfarm.logic.DynamicTile;
import ru.windcorp.crystalfarm.logic.DynamicTileLevel;
import ru.windcorp.crystalfarm.logic.FullGridTileLevel;
import ru.windcorp.crystalfarm.logic.GameManager;
import ru.windcorp.crystalfarm.logic.GridTile;
import ru.windcorp.crystalfarm.logic.GridTileLevel;
import ru.windcorp.crystalfarm.logic.Island;

public class TestGenerator extends BiomeProcessor {

	public TestGenerator() {
		super(InbuiltMod.INST, "TestGenerator");
	}

	@Override
	@SuppressWarnings("unchecked")
	public void process(Island island, Biome biome) {
		FullGridTileLevel<TestTile> testLevel = island.getLevel("Inbuilt:TestLevel", FullGridTileLevel.class);
		for (int x = 0; x < testLevel.getSize(); ++x) {
			for (int y = 0; y < testLevel.getSize(); ++y) {
				testLevel.setTile(new TestTile(), x, y);
			}
		}
		
		DynamicTileLevel<DynamicTile> testDynLevel = island.getLevel("Inbuilt:TestDynLevel", DynamicTileLevel.class);
		testDynLevel.addTile(new TestCharTile());
		
		GridTileLevel<GridTile> treeLevel = island.getLevel("Inbuilt:TreeLevel", GridTileLevel.class);
		
		for (int i = 0; i < treeLevel.getSize(); ++i) {
			treeLevel.addTile(
					treeLevel.getTileRegistry().createNew("Inbuilt:testTreeTile"),
					GameManager.GENERIC_RANDOM.nextInt(treeLevel.getSize()),
					GameManager.GENERIC_RANDOM.nextInt(treeLevel.getSize()));
		}
	}

}
