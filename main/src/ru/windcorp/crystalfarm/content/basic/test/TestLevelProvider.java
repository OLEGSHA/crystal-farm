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

import java.util.Collection;
import java.util.function.Consumer;

import ru.windcorp.crystalfarm.logic.GridTileLevel;
import ru.windcorp.crystalfarm.logic.Level;

public class TestLevelProvider implements Consumer<Collection<Level>> {

	@Override
	public void accept(Collection<Level> arg0) {
		GridTileLevel<TestTile> testLevel = new GridTileLevel<>("TestLevel", TestTile.class, 50);
		testLevel.getTileRegistry().register(new TestTile());
		
		arg0.add(testLevel);
	}

}
