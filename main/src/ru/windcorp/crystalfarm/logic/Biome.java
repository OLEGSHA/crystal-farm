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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.struct.mod.ModNameable;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class Biome extends ModNameable {
	
	private final List<BiomeProcessor> processors = Collections.synchronizedList(new ArrayList<>());

	public Biome(Mod mod, String name, BiomeProcessor... processors) {
		super(mod, name);
		for (BiomeProcessor processor : processors) {
			addProcessor(processor);
		}
	}
	
	public List<BiomeProcessor> getProcessors() {
		return processors;
	}

	public void addProcessor(BiomeProcessor e) {
		getProcessors().add(e);
	}

	public void generate(Island island) {
		island.getMeta().setBiome(this);
		getProcessors().forEach(processor -> {
			try {
				processor.process(island, this);
			} catch (Exception e) {
				ExecutionReport.reportError(e, null,
						"Biome processor %s for biome %s failed to execute", processor, this);
			}
		});
	}

}
