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

import java.util.function.Consumer;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.logic.DynamicTile;
import ru.windcorp.crystalfarm.logic.DynamicTileLevel;
import ru.windcorp.crystalfarm.logic.FullGridTileLevel;
import ru.windcorp.crystalfarm.logic.GridTile;
import ru.windcorp.crystalfarm.logic.GridTileLevel;
import ru.windcorp.crystalfarm.logic.Level;
import ru.windcorp.crystalfarm.logic.IslandFactory.IslandLevelProvider;

public class TestLevelProvider extends IslandLevelProvider {

	public TestLevelProvider() {
		super(InbuiltMod.INST, "TestLevelProvider");
	}

	@Override
	public void provideLevels(Consumer<Level> output, String name, int size) {
		FullGridTileLevel<TestTile> testLevel = new FullGridTileLevel<>(InbuiltMod.INST, "TestLevel", TestTile.class, size);
		testLevel.getTileRegistry().register(new TestTile());
		output.accept(testLevel);
		
		DynamicTileLevel<DynamicTile> testDynLevel = new DynamicTileLevel<>(InbuiltMod.INST, "TestDynLevel", DynamicTile.class, 10);
		testDynLevel.getTileRegistry().register(new TestCharTile());
		output.accept(testDynLevel);
		
		GridTileLevel<GridTile> treeLevel = new GridTileLevel<>(InbuiltMod.INST, "TreeLevel", GridTile.class, size);
		treeLevel.getTileRegistry().register(new TestTreeTile());
		output.accept(treeLevel);
	}

}
