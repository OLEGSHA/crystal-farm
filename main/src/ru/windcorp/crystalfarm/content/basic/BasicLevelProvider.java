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

import java.util.function.Consumer;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.content.basic.entity.EntityLevel;
import ru.windcorp.crystalfarm.content.basic.floor.FloorLevel;
import ru.windcorp.crystalfarm.content.basic.ground.GroundLevel;
import ru.windcorp.crystalfarm.content.basic.object.ObjectLevel;
import ru.windcorp.crystalfarm.logic.IslandFactory.IslandLevelProvider;
import ru.windcorp.crystalfarm.logic.Level;

public class BasicLevelProvider extends IslandLevelProvider {

	public BasicLevelProvider() {
		super(InbuiltMod.INST, "BasicLevelProvider");
	}

	@Override
	public void provideLevels(Consumer<Level> output, String name, int size) {
		output.accept(new GroundLevel(size));
		output.accept(new FloorLevel(size));
		output.accept(new ObjectLevel(size));
		output.accept(new EntityLevel());
	}

}
