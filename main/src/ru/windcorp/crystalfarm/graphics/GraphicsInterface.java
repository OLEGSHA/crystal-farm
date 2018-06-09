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

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GLCapabilities;
import ru.windcorp.crystalfarm.graphics.texture.Texture;
import ru.windcorp.crystalfarm.input.CursorMoveInput;
import ru.windcorp.crystalfarm.input.Input;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.crystalfarm.input.MouseButtonInput;
import ru.windcorp.crystalfarm.util.Direction;
import ru.windcorp.tge2.util.collections.ReverseListView;

public class GraphicsInterface {
	
	private static long window = 0;
	private static Thread renderThread = null;
	
	private static boolean graphicsReady = false;
	private static GLCapabilities glCapabilities = null;
	
	private static int fullscreenWidth = ModuleGraphicsInterface.WINDOW_WIDTH.get();
	private static int fullscreenHeight = ModuleGraphicsInterface.WINDOW_HEIGHT.get();
	
	private static final List<Layer> LAYERS = Collections.synchronizedList(new ArrayList<>());
	private static final List<Layer> LAYERS_REVERSE = new ReverseListView<>(LAYERS);
	
	private static final Collection<WindowResizeListener> LISTENERS_WINDOW_RESIZE = Collections.synchronizedCollection(new ArrayList<>());
	static Stream<WindowResizeListener> getWindowResizeListeners() {
		return Stream.concat(
				LAYERS.stream().filter(layer -> layer instanceof WindowResizeListener),
				LISTENERS_WINDOW_RESIZE.stream());
	}
	
	private static final Collection<InputListener> LISTENERS_INPUT = Collections.synchronizedCollection(new ArrayList<>());
	static Stream<InputListener> getInputListeners() {
		return Stream.concat(
				LAYERS_REVERSE.stream().filter(layer -> layer instanceof InputListener),
				LISTENERS_INPUT.stream());
	}
	
	private static int cursorX = 0, cursorY = 0;
	
	/*
	 * Layers
	 */
	
	public static void addLayer(Layer layer) {
		getLayers().add(layer);
	}
	
	public static void removeLayer(Layer layer) {
		getLayers().remove(layer);
	}
	
	public static void removeAllLayers() {
		getLayers().clear();
	}
	
	public static List<Layer> getLayers() {
		return LAYERS;
	}
	
	/*
	 * GLFW event handling
	 */
	
	static void handleKeyInput(long window, int key, int scancode, int action, int mods) {
		if (window != getWindow()) {
			return;
		}
		
		if (key == GLFW_KEY_F11 && action == GLFW_PRESS) {
			setFullscreen(!isFullscreen());
			return;
		}
		
		dispatchInput(new KeyInput(key, action, mods));
	}
	
	static void dispatchInput(Input input) {
		getInputListeners().forEach(l -> l.onInput(input));
	}
	
	static void handleWindowResize(long window, int width, int height) {
		if (window != getWindow()) {
			return;
		}
		
		if (!isFullscreen()) {
			ModuleGraphicsInterface.WINDOW_WIDTH.set(width);
			ModuleGraphicsInterface.WINDOW_HEIGHT.set(height);
		}
		
		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		ModuleGraphicsInterface.WINDOW_MAXIMIZED.set(glfwGetWindowAttrib(getWindow(), GLFW_MAXIMIZED) == GLFW_TRUE);
		
		getWindowResizeListeners().forEach(l -> l.onWindowResize(width, height));
	}
	
	static void handleCursorMove(long window, double x, double y) {
		if (window != getWindow()) {
			return;
		}
		
		GraphicsInterface.cursorX = (int) x;
		GraphicsInterface.cursorY = (int) y;
		
		dispatchInput(new CursorMoveInput());
	}
	
	static void handleMouseButton(long window, int button, int action, int mods) {
		if (window != getWindow()) {
			return;
		}
		
		dispatchInput(new MouseButtonInput(button, action, mods));
	}
	
	/*
	 * GLFW window operations
	 */
	
	public static long getWindow() {
		return window;
	}
	
	static void setWindow(long window) {
		GraphicsInterface.window = window;
	}
	
	public static int getWindowWidth() {
		return isFullscreen() ? fullscreenWidth : ModuleGraphicsInterface.WINDOW_WIDTH.get();
	}
	
	public static int getWindowHeight() {
		return isFullscreen() ? fullscreenHeight : ModuleGraphicsInterface.WINDOW_HEIGHT.get();
	}
	
	public static int getCursorX() {
		return cursorX;
	}
	
	public static int getCursorY() {
		return cursorY;
	}
	
	public static boolean isFullscreen() {
		return ModuleGraphicsInterface.WINDOW_FULLSCREEN.get();
	}
	
	public static void setFullscreen(boolean fullscreen) {
		ModuleGraphicsInterface.WINDOW_FULLSCREEN.set(fullscreen);
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		if (fullscreen) {
			glfwSetWindowMonitor(
					getWindow(),
					glfwGetPrimaryMonitor(),
					0,
					0,
					fullscreenWidth = vidmode.width(),
					fullscreenHeight = vidmode.height(),
					0);
		} else {
			glfwSetWindowMonitor(
					getWindow(),
					0,
					(vidmode.width() - getWindowWidth()) / 2,
					(vidmode.height() - getWindowHeight()) / 2,
					getWindowWidth(),
					getWindowHeight(),
					0);
		}
	}
	
	/*
	 * Drawing
	 */
	
	private static void applyColor(Color color) {
		glColor4d(color.r, color.g, color.b, color.a);
	}
	
	public static void fillRectangle(int x, int y, int width, int height, Color color) {
		width += x;
		height += y;
		
		glBegin(GL_QUADS);
		
			applyColor(color);
			
			glVertex2i(x, y);
			glVertex2i(x, height);
			glVertex2i(width, height);
			glVertex2i(width, y);
		
		glEnd();
	}
	
	private static final int UL_X = 0, UL_Y = 1, LR_X = 2, LR_Y = 3;
	
	public static void drawTexture(int x, int y, Texture texture, int tileX, int tileY, Color filter, Direction direction) {
		double[] coords = new double[4];
		int endX = texture.getWidth() + x,
				endY = texture.getHeight() + y;
		
		if (texture.getTileSize() == 0) {
			coords[UL_X] = 0;
			coords[UL_Y] = 0;
			coords[LR_X] = texture.getWidth() / (float) texture.getTextureWidth();
			coords[LR_Y] = texture.getHeight() / (float) texture.getTextureHeight();
		} else {
			// TODO
		}
		
		direction.turnCoordiates(coords);

		glBindTexture(GL_TEXTURE_2D, texture.getTextureId());
		
		glBegin(GL_QUADS);
		
			if (filter == null) {
				glColor3d(1, 1, 1);
			} else {
				applyColor(filter);
			}
			
			glTexCoord2d(coords[UL_X], coords[UL_Y]); glVertex2i(x, y);
			glTexCoord2d(coords[UL_X], coords[LR_Y]); glVertex2i(x, endY);
			glTexCoord2d(coords[LR_X], coords[LR_Y]); glVertex2i(endX, endY);
			glTexCoord2d(coords[LR_X], coords[UL_Y]); glVertex2i(endX, y);
			
		glEnd();
		
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	/*
	 * Misc
	 */
	
	public static boolean isRenderThread() {
		return Thread.currentThread() == renderThread;
	}
	
	static void setRenderThread(Thread thread) {
		GraphicsInterface.renderThread = thread;
	}

	public static boolean isGraphicsReady() {
		return graphicsReady;
	}

	static void setGraphicsReady() {
		GraphicsInterface.graphicsReady = true;
	}

	public static GLCapabilities getGlCapabilities() {
		return glCapabilities;
	}

	static void setGlCapabilities(GLCapabilities glCapabilities) {
		GraphicsInterface.glCapabilities = glCapabilities;
	}

}
