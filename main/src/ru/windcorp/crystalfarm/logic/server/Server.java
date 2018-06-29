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

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

import ru.windcorp.crystalfarm.logic.GameManager;
import ru.windcorp.crystalfarm.logic.Ticker;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class Server {
	
	public class ServerShutdownHook extends Thread {
		public ServerShutdownHook() {
			super("Server shutdown hook");
		}
		
		@Override
		public void run() {
			GameManager.shutdownLocalServer();
		}
	}

	private final World world;
	private final Ticker ticker;
	
	private final Thread shutdownHook = new ServerShutdownHook();
	
	private final Collection<Agent> agents = Collections.synchronizedCollection(new CopyOnWriteArrayList<>());
	
	public Server(World world) {
		this.world = world;
		this.ticker = new Ticker(world);
	}

	public World getWorld() {
		return world;
	}
	
	public Ticker getTicker() {
		return ticker;
	}

	public Collection<Agent> getAgents() {
		return agents;
	}
	
	public void start() {
		getTicker().start();
		Log.info("Started server ticker");
		
		Runtime.getRuntime().addShutdownHook(shutdownHook);
		Log.debug("Shutdown hook added");
	}
	
	public void shutdown() {
		Log.info("Stopping server ticker");
		getTicker().stop();
		getAgents().forEach(agent -> agent.onServerShutdown());
		save();
		
		if (Thread.currentThread() != shutdownHook) {
			Runtime.getRuntime().removeShutdownHook(shutdownHook);
			Log.debug("Shutdown hook removed");
		}
	}
	
	public boolean save() {
		try {
			Log.info("Saving world...");
			getWorld().save();
			Log.info("World saved!");
			return true;
		} catch (IOException e) {
			ExecutionReport.reportError(e, null,
					"Could not write world to disk");
		}
		
		return false;
	}
	
	public void pause() {
		getTicker().setPaused(true);
	}
	
	public void unpause() {
		getTicker().setPaused(false);
	}
	
	public void addAgent(Agent agent) {
		agent.setServer(this);
		// TODO: find island for new agent
		agent.setIsland(getWorld().getIslands().iterator().next());
		getAgents().add(agent);
	}
	
	public void removeAgent(Agent agent) {
		getAgents().remove(agent);
		// TODO: save info about agent
	}

}
