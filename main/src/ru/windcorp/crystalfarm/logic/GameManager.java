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

import java.io.IOException;
import java.util.Random;

import ru.windcorp.crystalfarm.client.*;
import ru.windcorp.crystalfarm.graphics.GraphicsInterface;
import ru.windcorp.crystalfarm.graphics.LayerFailure;
import ru.windcorp.crystalfarm.gui.menu.MainMenu;
import ru.windcorp.crystalfarm.logic.exception.*;
import ru.windcorp.crystalfarm.logic.server.*;
import ru.windcorp.tge2.util.NumberUtil;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.grh.Resource;

public class GameManager {
	
	private static Server localServer = null;
	private static Proxy localClient = null;
	
	public static final Random GENERIC_RANDOM = new Random();
	
	public static Server getLocalServer() {
		return localServer;
	}

	public static Proxy getLocalClient() {
		return localClient;
	}
	
	public static boolean isServer() {
		return localServer != null;
	}
	
	public static boolean isClient() {
		return localClient != null;
	}
	
	public static long getRandomSeed() {
		return System.currentTimeMillis() ^ System.nanoTime();
	}

	public static boolean generateNewWorld(Resource resource, long seed) {
		Log.topic("Worldgen");
		
		try {
			Log.info("Generating island Test");
			Island island = IslandFactory.createIsland("TestIsland", 100);
			island.getMeta().setDisplayName("Test Island");
			BiomeRegistry.get("Inbuilt:TemperateForest").generate(island, seed);
			
			Log.info("Saving world");
			World world = WorldFactory.createWorld(resource);
			world.getMeta().setDisplayName("Test World");
			
			world.spawnPlayer(island, world.addPlayer("Frozenfield"));
			
			world.addIsland(island);
			
			world.save();
			return false;
		} catch (IOException e) {
			failToMainMenu(e, "gm.gen.ioException",
					"Could not write %s due to an IO issue",
					resource.toString());
		} catch (Exception e) {
			failToMainMenu(e, "gm.gen.exception",
					"Could not write %s due to a runtime exception: %s",
					resource.toString(),
					e.toString());
		} finally {
			Log.end("Worldgen");
		}
		
		return true;
	}

	public static void startLocalGame(Resource resource) {
		if (startLocalServer(resource)) return;
		joinLocalServer();
	}

	public static boolean startLocalServer(Resource resource) {
		Log.topic("Server Init");
		
		Log.info("Initializing server");
		World world = WorldFactory.createWorld(resource);
		localServer = new Server(world);
		
		Log.info("Loading world");
		try {
			world.load();
			
			Log.info("Starting server");
			getLocalServer().start();
			Log.info("Server started");
			return false;
			
		} catch (IOException e) {
			failToMainMenu(e, "gm.ioException",
					"Could not read %s due to an IO issue",
					resource.toString());
			
		} catch (WrongMagicValueException e) {
			failToMainMenu(e, "gm.wrongMagicValue",
					"%s is probably not a world save: wrong magic value (expected %s, got %s)",
					resource.toString(),
					NumberUtil.toFullHex(e.getCorrectMagicValue()),
					NumberUtil.toFullHex(e.getWrongMagicValue()));
			
		} catch (UnknownWorldDataException e) {
			failToMainMenu(e, "gm.unknownWorldData",
					"%s contains an unknown World Data entry: %s",
					resource.toString(),
					e.getName());
			
		} catch (UnknownIslandDataException e) {
			failToMainMenu(e, "gm.unknownIslandData",
					"%s contains an unknown Island Data entry: %s in island %s",
					resource.toString(),
					e.getName(),
					e.getIsland().getName());
			
		} catch (UnknownLevelException e) {
			failToMainMenu(e, "gm.unknownIslandLevel",
					"%s contains an unknown Island Level entry: %s in island %s",
					resource.toString(),
					e.getName(),
					e.getIsland().getName());
			
		} catch (UnknownVersionException e) {
			failToMainMenu(e, "gm.unknownVersion",
					"%s uses an unknown version %s",
					resource.toString(),
					NumberUtil.toFullHex(e.getUnknownVersion()));
			
		} catch (SyntaxException e) {
			failToMainMenu(e, "gm.other",
					"Could not load %s: %s",
					resource.toString(),
					e.toString());
			
		} catch (Exception e) {
			failToMainMenu(e, "gm.exception",
					"Could not read %s due to a runtime exception: %s",
					resource.toString(),
					e.toString());
			
		} finally {
			Log.end("Server Init");
		}
		
		return true;
	}

	public static void failToMainMenu(Exception e, String code, String format, Object... args) {
		GraphicsInterface.removeAllNormalLayers();
		new MainMenu().show(true);
		LayerFailure.displayFailure(e, code, format, args);
	}

	public static void joinLocalServer() {
		LocalClientAgent agent = new LocalClientAgent(getLocalServer());
		localClient = new LocalProxy(agent);
		
		getLocalServer().addAgent(agent);
		GameLayer layer = new GameLayer(getLocalClient());
		GraphicsInterface.removeAllNormalLayers();
		layer.show(true);
	}
	
	public static void shutdownLocalServer() {
		shutdownLocalServer(null, null, null, (Object[]) null);
	}
	
	public static void shutdownLocalServer(Exception e, String format, Object... args) {
		if (getLocalServer() == null) {
			throw new IllegalStateException("No local server present");
		}
		
		Log.topic("Server Shutdown");
		
		try {
			Server server = getLocalServer();
			localServer = null;
			
			if (e != null) {
				ExecutionReport.reportError(e, null, format, args);
			}
			
			server.shutdown();
		} catch (Exception e1) {
			ExecutionReport.reportError(e1, null,
					"Failed to shut down the local server due to a runtime exception: %s",
					e1.toString());
		} finally {
			Log.end("Server Shutdown");			
		}
	}
	
	public static void shutdownClient() {
		shutdownClient(null, null, null, (Object[]) null);
	}

	public static void shutdownClient(Exception e, String code, String format, Object... args) {
		if (getLocalClient() == null) {
			throw new IllegalStateException("No client present");
		}
		
		Proxy client = getLocalClient();
		localClient = null;
		
		if (e != null) {
			failToMainMenu(e, code, format, args);
		}
		
		if (client instanceof LocalProxy
				&& getLocalServer() != null) {
			shutdownLocalServer();
		}

		GraphicsInterface.removeAllNormalLayers();
		new MainMenu().show(true);
	}
	
}
