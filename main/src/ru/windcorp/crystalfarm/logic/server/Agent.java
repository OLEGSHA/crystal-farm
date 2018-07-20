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

import java.lang.ref.WeakReference;

import ru.windcorp.crystalfarm.logic.Island;

public abstract class Agent {
	
	private Island island = null;
	private WeakReference<Server> server = null;

	public Island getIsland() {
		return island;
	}
	
	public Server getServer() {
		if (server == null) {
			return null;
		}
		return server.get();
	}
	
	public void setServer(Server server) {
		this.server = new WeakReference<Server>(server);
	}

	public void setIsland(Island island) {
		this.island = island;
	}
	
	public void setTickLength(long tickLength) {
		// To be overridden
	}
	
	public void onServerShutdown() {
		// To be overridden
	}
	
}
