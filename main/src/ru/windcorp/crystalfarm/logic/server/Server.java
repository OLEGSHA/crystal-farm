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

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
	
	private static Server current;
	
	public static Server getCurrent() {
		return current;
	}
	
	private final World world;
	
	private final Collection<Agent> agents = Collections.synchronizedCollection(new CopyOnWriteArrayList<>());
	
	public Server(World world) {
		this.world = world;
		
		current = this;
	}

	public World getWorld() {
		return world;
	}

	public Collection<Agent> getAgents() {
		return agents;
	}

}
