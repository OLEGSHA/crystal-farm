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
package ru.windcorp.crystalfarm.content.basic.biomes;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.content.basic.ground.*;
import ru.windcorp.crystalfarm.logic.Biome;
import ru.windcorp.crystalfarm.logic.TileGenerator;
import ru.windcorp.crystalfarm.util.RangedList;
import ru.windcorp.crystalfarm.util.noise.SimplexNoiseGenerator;

public class TemperateForestBiome extends Biome {
	
	private static final RangedList<TileGenerator<? extends GroundTile>> TILES = new RangedList<>();
	
	static {
		TILES.add((x, y, z) -> new DeepWaterGround(), Double.NEGATIVE_INFINITY, -0.1);
		TILES.add((x, y, z) -> new ShallowWaterGround(), -0.1, 0.0);
		TILES.add((x, y, z) -> new SandBeachGround(), 0.0, 0.15);
		TILES.add((x, y, z) -> new ShortGrassGround(), 0.15, Double.POSITIVE_INFINITY);
	}

	public TemperateForestBiome() {
		super(InbuiltMod.INST, "TemperateForest",
				new BasicTerrainGenerator(
						InbuiltMod.INST,
						"TemperateForestGenerator",
						TILES,
						1.0125,
						1,
						5,
						2));
	}

}
