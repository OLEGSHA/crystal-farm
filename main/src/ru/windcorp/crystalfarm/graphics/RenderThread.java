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

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import ru.windcorp.crystalfarm.CrystalFarm;
import ru.windcorp.crystalfarm.graphics.texture.TextureManager;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.synch.Lock;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class RenderThread implements Runnable {
	
	private final Lock lock = new Lock();

	@Override
	public void run() {
		
		Thread.currentThread().setName("Render");
		setRenderThread(Thread.currentThread());
		
		Log.topic("Graphics Init");
		Log.info("Initializing GLFW (window toolkit)");
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		initializeGlfw();
		createWindow();
		centerWindow();
		showWindow();
		initializeOpenGL();
		
		Log.debug("Entering render loop");
		Log.end("Graphics Init");
		
		lock.unlock();
		
		while (!glfwWindowShouldClose(getWindow())) {
			TextureManager.processLoadQueue();
			
			glClear(GL_COLOR_BUFFER_BIT);

			doRender();

			glfwSwapBuffers(getWindow());
			glfwPollEvents();
		}
		
	}

	private void doRender() {
		
		synchronized (GraphicsInterface.getLayers()) {
			for (Layer l : GraphicsInterface.getLayers()) {
				l.render();
			}
		}
		
	}
	
	private void initializeGlfw() {
		if (!glfwInit()) {
			ExecutionReport.reportCriticalError(null, ExecutionReport.rscCorrupt("GLFW", "GLFW failed to initialize"), null);
		}
	}

	private void createWindow() {
		Log.info("Creating window");
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		
		setWindow(
				glfwCreateWindow(640, 480,
						CrystalFarm.FULL_NAME + " " + CrystalFarm.VERSION_CODENAME + "/" + CrystalFarm.VERSION,
						NULL, NULL)
				);
		
		if (getWindow() == NULL) {
			ExecutionReport.reportCriticalError(null, ExecutionReport.rscCorrupt("GLFW", "GLFW failed to initialize window"), null);
		}
		
		glfwSetKeyCallback(getWindow(), GraphicsInterface::handleKeyInput);
		glfwSetCursorPosCallback(getWindow(), GraphicsInterface::handleCursorMove);
		glfwSetMouseButtonCallback(getWindow(), GraphicsInterface::handleMouseButton);
		glfwSetWindowSizeCallback(getWindow(), GraphicsInterface::handleWindowResize);
	}

	private void showWindow() {
		glfwMakeContextCurrent(getWindow());
		glfwSwapInterval(1);
		glfwShowWindow(getWindow());
	}

	private void centerWindow() {
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			glfwGetWindowSize(getWindow(), pWidth, pHeight);

			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			glfwSetWindowPos(
				getWindow(),
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}
	}

	private void initializeOpenGL() {
		Log.info("Initializing OpenGL");
		GL.createCapabilities();
		
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity(); // Resets any previous projection matrices
		glOrtho(0, 640, 480, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}
	
	public void waitForInit() {
		lock.raiseFlag();
		lock.lock();
	}

}
