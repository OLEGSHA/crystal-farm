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
package ru.windcorp.crystalfarm.logic.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

import ru.windcorp.crystalfarm.logic.Data;
import ru.windcorp.tge2.util.grh.Resource;

public class WorldFactory {
	
	private static final Collection<Consumer<World>> PROCESSORS = Collections.synchronizedCollection(new ArrayList<>());
	private static final Collection<Consumer<Collection<Data>>> DATA_PROVIDERS = Collections.synchronizedCollection(new ArrayList<>());
	
	public static World createWorld(Resource resource) {
		ArrayList<Data> data = new ArrayList<>();
		DATA_PROVIDERS.forEach(provider -> provider.accept(data));
		
		World world = new World(resource);
		world.addData(data);
		PROCESSORS.forEach(processor -> processor.accept(world));
		
		return world;
	}
	
	public static void registerWorldProcessor(Consumer<World> processor) {
		PROCESSORS.add(processor);
	}
	
	public static void registerWorldDataProvider(Consumer<Collection<Data>> provider) {
		DATA_PROVIDERS.add(provider);
	}

}
