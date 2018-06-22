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

//import java.io.IOException;

import ru.windcorp.crystalfarm.graphics.GraphicsInterface;
import ru.windcorp.crystalfarm.graphics.LayerFailure;
import ru.windcorp.crystalfarm.gui.menu.MainMenu;
//import ru.windcorp.crystalfarm.logic.exception.UnknownWorldDataException;
//import ru.windcorp.crystalfarm.logic.exception.WrongMagicValueException;
//import ru.windcorp.crystalfarm.logic.server.Server;
//import ru.windcorp.crystalfarm.logic.server.World;
//import ru.windcorp.crystalfarm.logic.server.WorldFactory;
//import ru.windcorp.tge2.util.debug.Log;
//import ru.windcorp.tge2.util.debug.er.ExecutionReport;
//import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.grh.Resource;

public class GameManager {

	public static void startLocalGame(Resource resource) {
		
		startLocalServer(resource);
		joinLocalServer();
		
	}

	public static void startLocalServer(Resource resource) {
		
		failWorldLoad(new IllegalStateException(), "test", "Could not load world from %s because I haven't written the code yet", resource.toString());
		return;
//		Log.topic("Server Init");
//		
//		Log.info("Initializing server");
//		World world = WorldFactory.createWorld(resource);
//		Server server = new Server(world);
//		
//		Log.info("Loading world");
//		try {
//			world.load();
//			
//			return;
//		} catch (IOException e) {
//			//failWorldLoad();
//			ExecutionReport.reportError(e,
//					ExecutionReport.rscCorrupt(resource.toString(),
//							"Could not read world save due to an IO issue"),
//					null);
//		} catch (WrongMagicValueException e) {
//			ExecutionReport.reportWarning(e,
//					ExecutionReport.rscCorrupt(resource.toString(),
//							"The given file is either not a world save or is corrupted"),
//					null);
//		} catch (UnknownWorldDataException e) {
//			
//		} catch (SyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.err.println("Called auto-generated catch block in method GameManager.startLocalServer for SyntaxException");
//		}
	}

	private static void failWorldLoad(Exception e, String code, String format, Object... args) {
		GraphicsInterface.removeAllNormalLayers();
		GraphicsInterface.addLayer(new MainMenu());
		LayerFailure.displayFailure(e, code, format, args);
		
	}

	public static void joinLocalServer() {
		// TODO Auto-generated method stub
		System.err.println("Called auto-generated method GameManager.GameManager");
		
	}
	
}
