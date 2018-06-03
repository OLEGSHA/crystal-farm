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

import static ru.windcorp.crystalfarm.graphics.ModuleGraphicsInterface.*;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import ru.windcorp.crystalfarm.CrystalFarm;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class WindowHandler implements Runnable {

	@Override
	public void run() {
		
		Thread.currentThread().setName("Window Handler");
		
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
		
		while (!glfwWindowShouldClose(getGLWFWindow())) {
			glClear(GL_COLOR_BUFFER_BIT);
			glBegin(GL_TRIANGLES);

			doRender();

			glEnd();
			glfwSwapBuffers(getGLWFWindow());
			glfwPollEvents();
		}
		
	}
	
	private Color color = new Color(1, 1, 1, 1);

	private void doRender() {
		
		GraphicsInterface.applyColor(color);
		GraphicsInterface.fillRectangle(10, 10, 100, 200);
		
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
		
		ModuleGraphicsInterface.setGLFWWindow(
				glfwCreateWindow(640, 480,
						CrystalFarm.FULL_NAME + " " + CrystalFarm.VERSION_CODENAME + "/" + CrystalFarm.VERSION,
						NULL, NULL));
		
		if (ModuleGraphicsInterface.getGLWFWindow() == NULL) {
			ExecutionReport.reportCriticalError(null, ExecutionReport.rscCorrupt("GLFW", "GLFW failed to initialize window"), null);
		}
	}

	private void showWindow() {
		glfwMakeContextCurrent(ModuleGraphicsInterface.getGLWFWindow());
		glfwSwapInterval(1);
		glfwShowWindow(ModuleGraphicsInterface.getGLWFWindow());
	}

	private void centerWindow() {
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			glfwGetWindowSize(getGLWFWindow(), pWidth, pHeight);

			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			glfwSetWindowPos(
				getGLWFWindow(),
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}
	}

	private void initializeOpenGL() {
		Log.info("Initializing OpenGL");
		GL.createCapabilities();

		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity(); // Resets any previous projection matrices
		glOrtho(0, 640, 480, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

}
