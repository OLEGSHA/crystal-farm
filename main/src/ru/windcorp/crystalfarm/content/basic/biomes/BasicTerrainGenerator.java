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

import ru.windcorp.crystalfarm.content.basic.ground.GroundLevel;
import ru.windcorp.crystalfarm.content.basic.ground.GroundTile;
import ru.windcorp.crystalfarm.logic.Biome;
import ru.windcorp.crystalfarm.logic.BiomeProcessor;
import ru.windcorp.crystalfarm.logic.Island;
import ru.windcorp.crystalfarm.logic.TileGenerator;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.util.RangedList;
import ru.windcorp.crystalfarm.util.noise.OctaveGenerator;
import ru.windcorp.crystalfarm.util.noise.SimplexOctaveGenerator;

public class BasicTerrainGenerator extends BiomeProcessor {
	
	private final int OCTAVES = 8;

	private final RangedList<TileGenerator<? extends GroundTile>> tiles;
	private double frequency, noiseAmplitude, scale, amplitude;

	public BasicTerrainGenerator(
			Mod mod,
			String name,
			RangedList<TileGenerator<? extends GroundTile>> tiles,
			double frequency,
			double noiseAmplitude,
			double scale,
			double amplitude) {
		
		super(mod, name);
		this.tiles = tiles;
		this.frequency = frequency;
		this.noiseAmplitude = noiseAmplitude;
		this.scale = scale;
		this.amplitude = amplitude;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public double getNoiseAmplitude() {
		return noiseAmplitude;
	}

	public void setNoiseAmplitude(double noiseAmplitude) {
		this.noiseAmplitude = noiseAmplitude;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public double getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}

	public RangedList<TileGenerator<? extends GroundTile>> getTiles() {
		return tiles;
	}

	@Override
	public void process(Island island, Biome biome, long seed) {
		GroundLevel level = island.getLevel("Inbuilt:GroundLevel", GroundLevel.class);
		
		OctaveGenerator noise = new SimplexOctaveGenerator(seed, OCTAVES);
		double z;
		double size = island.getSize();
		
		for (int x = 0; x < level.getSize(); ++x) {
			for (int y = 0; y < level.getSize(); ++y) {
				z = getZ(
						noise,
						x,
						y,
						frequency,
						noiseAmplitude,
						scale,
						amplitude,
						size);
				level.setTile(tiles.get(z).generate(x, y, z), x, y);
			}
		}
	}

	protected double getZ(
			OctaveGenerator noise,
			double x,
			double y,
			double frequency,
			double noiseAmplitude,
			double scale,
			double amplitude,
			double size) {
		
		double result = Math.min(noise.noise(x * scale / size, y * scale / size, frequency, noiseAmplitude, true) + 0.15, 1);
		result -= pow4((x / size - 0.5) * 2) + pow4((y / size - 0.5) * 2);
		
		return result * amplitude;
	}
	
	private static double pow4(double x) {
		return x * x * x * x;
	}

}
