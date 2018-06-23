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
package ru.windcorp.crystalfarm.logic;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public class IslandMeta extends Data {

	private String displayName;
	private Biome biome;
	
	public IslandMeta() {
		super(InbuiltMod.INST, "IslandMeta");
	}

	@Override
	public void read(DataInput input, int change) throws IOException, SyntaxException {
		displayName		= input.readUTF();
		biome			= readBiome(input);
	}

	private Biome readBiome(DataInput input) throws IOException, SyntaxException {
		String name = input.readUTF();
		Biome result = BiomeRegistry.get(name);
		
		if (result == null) {
			ExecutionReport.reportWarning(null, null, "Biome with name %s has not been registered",
					name);
			
			result = BiomeRegistry.getFallback();
			
			if (result == null) {
				throw new SyntaxException("Biome with name " + name + " has not been registered"
						+ " and no fallback tile specified");
			}
		}
		
		return result;
	}

	@Override
	public void write(DataOutput output, int change) throws IOException {
		output.writeUTF(displayName);
		output.writeUTF(biome.getName());
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		setChangeAll();
	}

	public Biome getBiome() {
		return biome;
	}

	public void setBiome(Biome biome) {
		this.biome = biome;
	}

}
