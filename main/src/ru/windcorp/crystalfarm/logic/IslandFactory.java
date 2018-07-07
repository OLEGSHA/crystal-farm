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
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.struct.mod.ModNameable;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class IslandFactory {
	
	public static abstract class IslandProcessor extends ModNameable {

		public IslandProcessor(Mod mod, String name) {
			super(mod, name);
		}
		
		public abstract void process(Island island);
		
	}
	
	public static abstract class IslandDataProvider extends ModNameable {

		public IslandDataProvider(Mod mod, String name) {
			super(mod, name);
		}
		
		public abstract void provideData(Consumer<Data> output, String name, int size);
		
	}
	
	public static abstract class IslandLevelProvider extends ModNameable {
		
		public IslandLevelProvider(Mod mod, String name) {
			super(mod, name);
		}
		
		public abstract void provideLevels(Consumer<Level> output, String name, int size);
		
	}
	
	private static final Collection<IslandProcessor> PROCESSORS = Collections.synchronizedCollection(new ArrayList<>());
	private static final Collection<IslandDataProvider> DATA_PROVIDERS = Collections.synchronizedCollection(new ArrayList<>());
	private static final Collection<IslandLevelProvider> LEVEL_PROVIDERS = Collections.synchronizedCollection(new ArrayList<>());
	
	public static Island createIsland(String name, int size) {
		ArrayList<Data> data = new ArrayList<>();
		DATA_PROVIDERS.forEach(provider -> {
			try {
				provider.provideData(provided -> data.add(provided), name, size);
			} catch (Exception e) {
				ExecutionReport.reportError(e, null,
						"Island Data provider %s failed to execute", provider);
			}
		});
		
		ArrayList<Level> levels = new ArrayList<>();
		LEVEL_PROVIDERS.forEach(provider -> {
			try {
				provider.provideLevels(provided -> levels.add(provided), name, size);
			} catch (Exception e) {
				ExecutionReport.reportError(e, null,
						"Island Island provider %s failed to execute", provider);
			}
		});
		
		Island island = new Island(name, size, levels.toArray(new Level[levels.size()]), data.toArray(new Data[data.size()]));
		PROCESSORS.forEach(processor -> {
			try {
				processor.process(island);
			} catch (Exception e) {
				ExecutionReport.reportError(e, null,
						"Island processor %s failed to execute", processor);
			}
		});
		
		return island;
	}
	
	public static void registerIslandProcessor(IslandProcessor processor) {
		PROCESSORS.add(processor);
	}
	
	public static void registerIslandDataProvider(IslandDataProvider provider) {
		DATA_PROVIDERS.add(provider);
	}
	
	public static void registerIslandLevelProvider(IslandLevelProvider provider) {
		LEVEL_PROVIDERS.add(provider);
	}

}
