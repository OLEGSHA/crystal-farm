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
package ru.windcorp.crystalfarm.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;

import java.lang.reflect.InvocationTargetException;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.cfg.SettingBoolean;
import ru.windcorp.crystalfarm.cfg.SettingInt;
import ru.windcorp.crystalfarm.graphics.texture.TextureManager;
import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.jobs.JobManager;
import ru.windcorp.tge2.util.unixarg.UnixArgument;
import ru.windcorp.tge2.util.unixarg.UnixArgumentInvalidSyntaxException;

public class ModuleGraphicsInterface extends Module {
	
	public static final int MIN_WIDTH = 800;
	public static final int MIN_HEIGHT = 600;
	
	static final SettingInt WINDOW_WIDTH = new SettingInt(
			"WindowWidth", "Width of the game window at startup. Has no effect in fullscreen mode",
			MIN_WIDTH, 16000, MIN_WIDTH, 1);
	static final SettingInt WINDOW_HEIGHT = new SettingInt("WindowHeight", "Height of the game window at startup. Has no effect in fullscreen mode",
			MIN_HEIGHT, 16000, MIN_HEIGHT, 1);
	public static final SettingBoolean WINDOW_FULLSCREEN = new SettingBoolean("WindowFullscreen", "True when window is in fullscreen. Does not affect WindowMaximized", false);
	static final SettingBoolean WINDOW_MAXIMIZED = new SettingBoolean("WindowMaximized", "True when window is maximized. Does not affect WindowFullscreen", true);
	public static final SettingBoolean SHOW_FPS = new SettingBoolean("ShowFPS", "When true, current FPS is displayed", false);
	public static final SettingBoolean VSYNC = new SettingBoolean("Vsync", "When true, vertical synchronization is enabled", true);
	
	public ModuleGraphicsInterface() {
		super("GraphicsInterface", InbuiltMod.INST);
		
		WINDOW_FULLSCREEN.addListener(x -> {
			run(() -> {
				setFullscreen(isFullscreen());
				return null;
			});
		});
		
		WINDOW_FULLSCREEN.addListener(x -> {
			run(() -> {
				glfwSwapInterval(VSYNC.get() ? 1 : 0);
				return null;
			});
		});
		
		addConfig(WINDOW_WIDTH);
		addConfig(WINDOW_HEIGHT);
		addConfig(WINDOW_FULLSCREEN);
		addConfig(WINDOW_MAXIMIZED);
		addConfig(SHOW_FPS);
		addConfig(VSYNC);
		
		//addConfig(GraphicsDesign.registerConfig());
		
		addArgument(new UnixArgument<Void>(
				"debugTextureLoading", null,
				"Enables extra debug info for texture loading", null,
				false, false, true) {

			@Override
			protected boolean runImpl(Void arg) throws UnixArgumentInvalidSyntaxException, InvocationTargetException {
				TextureManager.setEnableDebug(true);
				return false;
			}
			
		});
		
		GraphicsInterface.addInputListener(new DebugInputListener());
	}

	@Override
	public void registerJobs(JobManager<ModuleJob> manager) {
		manager.addJob(new JobGraphicsInterfaceInit(this));
		manager.addJob(new TMP_JobTestGUI(this));
		manager.addJob(new JobShowGameLoadLayer(this));
		manager.addJob(new JobLoadDefaultFonts(this));
		manager.addJob(new JobOpenMainMenu(this));
		
//		for (int i = 0; i < 10; ++i) manager.addJob(new TMP_JobDelayLoad(i, this));
		
		Runtime.getRuntime().addShutdownHook(new Thread(() ->  {
			if (getWindow() != 0) {
				// Free the window callbacks and destroy the window
				glfwFreeCallbacks(getWindow());
				glfwDestroyWindow(getWindow());
				
				// Terminate GLFW and free the error callback
				glfwTerminate();
				glfwSetErrorCallback(null).free();
			}
		}));
	}

}
