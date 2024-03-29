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

import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import ru.windcorp.crystalfarm.CrystalFarm;
import ru.windcorp.crystalfarm.debug.CrystalFarmGLFWErrorHandler;
import ru.windcorp.crystalfarm.graphics.fonts.Font;
import ru.windcorp.crystalfarm.graphics.fonts.FontManager;
import ru.windcorp.crystalfarm.graphics.fonts.FontStyle;
import ru.windcorp.crystalfarm.graphics.texture.TextureManager;
import ru.windcorp.crystalfarm.graphics.texture.TexturePrimitive;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.synch.Lock;
import ru.windcorp.tge2.util.vectors.Vector2;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class RenderThread implements Runnable {
	
	private final Lock lock = new Lock();

	@Override
	public void run() {
		
		Thread.currentThread().setName("Render");
		setRenderThread(Thread.currentThread());
		
		Log.topic("Graphics Init");
		Log.info("Initializing GLFW (window toolkit)");
		
		initializeGlfw();
		createWindow();
		positionWindow();
		createWindowIcons();
		initializeOpenGL();
		setupWindowCallbacks();
		
		Log.debug("Entering render loop");
		Log.end("Graphics Init");
		
		setGraphicsReady();
		lock.unlock();
		
		double lastFrame = glfwGetTime();
		try {
			while (!glfwWindowShouldClose(getWindow())) {
				TextureManager.processLoadQueue();
				processRunQueue();
				
				glClear(GL_COLOR_BUFFER_BIT);

				render();
				drawFps();
	
				glfwSwapBuffers(getWindow());
				glfwPollEvents();
				
				frameLength = (-lastFrame + (lastFrame = glfwGetTime())) * 1000;
				
				checkOpenGLErrors();
			}
		} catch (OpenGLException e) {
			ExecutionReport.reportCriticalError(e, null,
					"OpenGL error detected: %s",
					getOpenGLErrorDescription(e.getErrorCode()));
		} catch (Exception e) {
			ExecutionReport.reportCriticalError(e, null,
					"Unhandled error in master render loop: %s",
					e.toString());
		}
		
	}

	private void drawFps() {
		Font font = FontManager.getDefaultFont();
		if (ModuleGraphicsInterface.SHOW_FPS.get() && font != null) {
			char[] text = String.valueOf((int) getFps()).toCharArray();
			int length = font.getLength(text, false);
			fillRectangle(
					0,
					getWindowHeight() - font.getHeight(),
					length,
					font.getHeight(),
					Color.WHITE);
			font.render(
					text,
					0,
					getWindowHeight() - font.getHeight(),
					false,
					FontStyle.PLAIN,
					Color.BLACK);
		}
	}

	private void initializeGlfw() {
		Log.debug("About to initialize GLFW version " + glfwGetVersionString());
		
		glfwSetErrorCallback(new CrystalFarmGLFWErrorHandler());
		
		if (!glfwInit()) {
			ExecutionReport.reportCriticalError(null, ExecutionReport.rscCorrupt("GLFW", "GLFW failed to initialize"), null);
		}
	}

	private void createWindow() {
		Log.info("Creating window");
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_FOCUSED, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, ModuleGraphicsInterface.WINDOW_MAXIMIZED.get() ? GLFW_TRUE : GLFW_FALSE);
		
		setWindow(
				glfwCreateWindow(GraphicsInterface.getWindowWidth(), GraphicsInterface.getWindowHeight(),
						CrystalFarm.FULL_NAME + " " + CrystalFarm.VERSION_CODENAME + "/" + CrystalFarm.VERSION,
						NULL, NULL)
				);
		
		if (getWindow() == NULL) {
			ExecutionReport.reportCriticalError(null, ExecutionReport.rscCorrupt("GLFW", "GLFW failed to initialize window"), null);
		}
		
		glfwSetWindowSizeLimits(getWindow(),
				ModuleGraphicsInterface.MIN_WIDTH,
				ModuleGraphicsInterface.MIN_HEIGHT,
				GLFW_DONT_CARE, GLFW_DONT_CARE);
		
		glfwMakeContextCurrent(getWindow());
		glfwSwapInterval(1);
	}

	private void positionWindow() {
		//setFullscreen(isFullscreen());
		syncFullscreen();
	}

	private void createWindowIcons() {
		Vector2<TexturePrimitive, ByteBuffer> icon16 = TextureManager.loadToByteBuffer("window/icon16", false);
		Vector2<TexturePrimitive, ByteBuffer> icon32 = TextureManager.loadToByteBuffer("window/icon32", false);
		
		try (GLFWImage.Buffer buffer = GLFWImage.malloc(2)) {
			buffer
				.position(0)
				.width(icon16.a.getWidth())
				.height(icon16.a.getHeight())
				.pixels(icon16.b);
			
			buffer
				.position(1)
				.width(icon32.a.getWidth())
				.height(icon32.a.getHeight())
				.pixels(icon32.b);
			
			glfwSetWindowIcon(getWindow(), buffer);
		}
	}

	private void setupWindowCallbacks() {
		Log.info("Registering window callbacks");
		glfwSetKeyCallback(getWindow(), GraphicsInterface::handleKeyInput);
		glfwSetCharCallback(getWindow(), GraphicsInterface::handleCharInput);
		glfwSetCursorPosCallback(getWindow(), GraphicsInterface::handleCursorMove);
		glfwSetMouseButtonCallback(getWindow(), GraphicsInterface::handleMouseButton);
		glfwSetScrollCallback(getWindow(), GraphicsInterface::handleScroll);
		glfwSetWindowSizeCallback(getWindow(), GraphicsInterface::handleWindowResize);
		glfwSetWindowCloseCallback(getWindow(), GraphicsInterface::handleWindowClose);
	}
	
	private void initializeOpenGL() {
		Log.info("Initializing OpenGL");
		setGlCapabilities(GL.createCapabilities());
		
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		
		checkOpenGLErrors();
		handleWindowResize(getWindow(), getWindowWidth(), getWindowHeight());
	}
	
	public void waitForInit() {
		lock.raiseFlag();
		lock.lock();
	}

}
