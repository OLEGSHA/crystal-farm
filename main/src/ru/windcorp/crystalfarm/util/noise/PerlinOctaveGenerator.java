/**
 * org.bukkit.util.noise package
 * Adapted from Bukkit API
 * as ru.windcorp.crystalfarm.util.noise
 * for Crystal Farm the game
 * 
 * Copyright (C) 2011-2013  The Bukkit Project
 * Adaptation Copyright (C) 2018  Crystal Farm Development Team
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
package ru.windcorp.crystalfarm.util.noise;

import java.util.Random;

/**
 * Creates perlin noise through unbiased octaves
 */
public class PerlinOctaveGenerator extends OctaveGenerator {

    /**
     * Creates a perlin octave generator for the given world
     *
     * @param seed Seed to construct this generator for
     * @param octaves Amount of octaves to create
     */
    public PerlinOctaveGenerator(long seed, int octaves) {
        this(new Random(seed), octaves);
    }

    /**
     * Creates a perlin octave generator for the given {@link Random}
     *
     * @param rand Random object to construct this generator for
     * @param octaves Amount of octaves to create
     */
    public PerlinOctaveGenerator(Random rand, int octaves) {
        super(createOctaves(rand, octaves));
    }

    private static NoiseGenerator[] createOctaves(Random rand, int octaves) {
        NoiseGenerator[] result = new NoiseGenerator[octaves];

        for (int i = 0; i < octaves; i++) {
            result[i] = new PerlinNoiseGenerator(rand);
        }

        return result;
    }
}
