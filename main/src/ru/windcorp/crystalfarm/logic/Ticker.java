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

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import ru.windcorp.crystalfarm.logic.server.World;
import ru.windcorp.crystalfarm.logic.server.WorldMeta;

public class Ticker extends TimerTask {

	private final WeakReference<World> world;
	private final Timer timer;
	private boolean isPaused = false;
	
	public Ticker(World world) {
		this.world = new WeakReference<>(world);
		this.timer = new Timer("Ticker for World " + world.getMeta().getDisplayName());
	}
	
	public World getWorld() {
		return world.get();
	}
	
	public Timer getTimer() {
		return timer;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

	public static void tick(World world, long length, long time) {
		synchronized (world.getIslands()) {
			for (Island island : world.getIslands()) {
				for (Level level : island.getLevels()) {
					try {
						level.tick(world, island, length, time);
					} catch (Exception e) {
						GameManager.shutdownLocalServer(e, "tickFailed",
								"Level %s of island %s failed to tick", level, island);
					}
				}
			}
		}
	}
	
	public void run() {
		if (isPaused()) {
			return;
		}
		
		World world = getWorld();
		
		if (world == null) {
			stop();
		} else {
			WorldMeta meta = world.getMeta();
			tick(world, meta.getTickLength(), meta.getTime());
			
			meta.setTime(meta.getTime() + meta.getTickLength());
		}
	}
	
	public void start() {
		getTimer().scheduleAtFixedRate(this, 0, getWorld().getMeta().getTickLength());
	}
	
	public void stop() {
		getTimer().cancel();
	}

	public void schedule(TimerTask task, long delay) {
		getTimer().schedule(task, delay);
	}

	public void schedule(TimerTask task, long delay, long period) {
		getTimer().scheduleAtFixedRate(task, delay, period);
	}
	
}
