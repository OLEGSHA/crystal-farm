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

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;

import ru.windcorp.crystalfarm.gui.Size;
import ru.windcorp.crystalfarm.input.CursorMoveInput;
import ru.windcorp.crystalfarm.input.Input;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.crystalfarm.input.MouseButtonInput;
import ru.windcorp.tge2.util.collections.ReverseListView;

public class GraphicsInterface {
	
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
	
	private static final Color CURRENT_COLOR = new Color(0, 0, 0, 0);
	
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
		if (window != ModuleGraphicsInterface.getGLWFWindow()) {
			return;
		}
		
		dispatchInput(new KeyInput(key, action, mods));
	}
	
	static void dispatchInput(Input input) {
		getInputListeners().forEach(l -> l.onInput(input));
	}
	
	static void handleWindowResize(long window, int width, int height) {
		if (window != ModuleGraphicsInterface.getGLWFWindow()) {
			return;
		}
		
		getWindowResizeListeners().forEach(l -> l.onWindowResize(width, height));
	}
	
	static void handleCursorMove(long window, double x, double y) {
		if (window != ModuleGraphicsInterface.getGLWFWindow()) {
			return;
		}
		
		GraphicsInterface.cursorX = (int) x;
		GraphicsInterface.cursorY = (int) y;
		
		dispatchInput(new CursorMoveInput());
	}
	
	static void handleMouseButton(long window, int button, int action, int mods) {
		if (window != ModuleGraphicsInterface.getGLWFWindow()) {
			return;
		}
		
		dispatchInput(new MouseButtonInput(button, action, mods));
	}
	
	/*
	 * GLFW window operations
	 */
	
	public static Size getWindowSize() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer width = stack.mallocInt(1),
					height = stack.mallocInt(1);
			GLFW.glfwGetWindowSize(ModuleGraphicsInterface.getGLWFWindow(),
					width, height);
			
			return new Size(width.get(), height.get());
		}
	}
	
	public static int getCursorX() {
		return cursorX;
	}
	
	public static int getCursorY() {
		return cursorY;
	}
	
	/*
	 * Drawing
	 */
	
	public static void applyColor(Color color) {
		CURRENT_COLOR.copyFrom(color);
		glColor4d(color.r, color.g, color.b, color.a);
	}
	
	public static Color getColor() {
		return CURRENT_COLOR;
	}
	
	public static void fillRectangle(int x, int y, int width, int height) {
		width += x;
		height += y;
		
		glVertex2i(x, y);
		glVertex2i(width, height);
		glVertex2i(width, y);
		
		glVertex2i(x, y);
		glVertex2i(x, height);
		glVertex2i(width, height);
	}

}
