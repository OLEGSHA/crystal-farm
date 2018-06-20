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

public class IslandFactory {
	
	private static final Collection<Consumer<Island>> PROCESSORS = Collections.synchronizedCollection(new ArrayList<>());
	private static final Collection<Consumer<Collection<Data>>> DATA_PROVIDERS = Collections.synchronizedCollection(new ArrayList<>());
	private static final Collection<Consumer<Collection<Level>>> LEVEL_PROVIDERS = Collections.synchronizedCollection(new ArrayList<>());
	
	public static Island createIsland(String name) {
		ArrayList<Data> data = new ArrayList<>();
		DATA_PROVIDERS.forEach(provider -> provider.accept(data));
		
		ArrayList<Level> level = new ArrayList<>();
		LEVEL_PROVIDERS.forEach(provider -> provider.accept(level));
		
		Island island = new Island(name, level.toArray(new Level[level.size()]), data.toArray(new Data[data.size()]));
		PROCESSORS.forEach(processor -> processor.accept(island));
		
		return island;
	}
	
	public static void registerIslandProcessor(Consumer<Island> processor) {
		PROCESSORS.add(processor);
	}
	
	public static void registerIslandDataProvider(Consumer<Collection<Data>> provider) {
		DATA_PROVIDERS.add(provider);
	}
	
	public static void registerIslandLevelProvider(Consumer<Collection<Level>> provider) {
		LEVEL_PROVIDERS.add(provider);
	}

}
