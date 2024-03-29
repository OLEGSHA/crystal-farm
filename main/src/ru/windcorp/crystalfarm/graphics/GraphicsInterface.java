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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GLCapabilities;

import ru.windcorp.crystalfarm.CrystalFarm;
import ru.windcorp.crystalfarm.graphics.texture.Texture;
import ru.windcorp.crystalfarm.gui.Component;
import ru.windcorp.crystalfarm.gui.GuiLayer;
import ru.windcorp.crystalfarm.input.CharInput;
import ru.windcorp.crystalfarm.input.CursorMoveInput;
import ru.windcorp.crystalfarm.input.Input;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.crystalfarm.input.KeyStroke;
import ru.windcorp.crystalfarm.input.MouseButtonInput;
import ru.windcorp.crystalfarm.input.ScrollInput;
import ru.windcorp.crystalfarm.util.Direction;
import ru.windcorp.tge2.util.IndentedStringBuilder;
import ru.windcorp.tge2.util.NumberUtil;
import ru.windcorp.tge2.util.collections.ReverseListView;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

/**
 * An interface to access a limited set of OpenGL and the graphics structure specific to CrystalFarm
 */
public class GraphicsInterface {
	
	private static long window = 0;
	private static Thread renderThread = null;
	
	private static boolean graphicsReady = false;
	private static GLCapabilities glCapabilities = null;
	
	private static int unsavedWidth = ModuleGraphicsInterface.WINDOW_WIDTH.get();
	private static int unsavedHeight = ModuleGraphicsInterface.WINDOW_HEIGHT.get();
	
	private static final List<Layer> LAYERS = Collections.synchronizedList(new CopyOnWriteArrayList<>());
	private static final List<Layer> LAYERS_REVERSE = new ReverseListView<>(LAYERS);
	private static int firstStickyLayer = 0;
	
	private static final Collection<WindowResizeListener> LISTENERS_WINDOW_RESIZE = Collections.synchronizedCollection(new CopyOnWriteArrayList<>());
	
	@SuppressWarnings("unchecked")
	static Stream<WindowResizeListener> getWindowResizeListeners() {
		return Stream.concat(
				(Stream<? extends WindowResizeListener>) LAYERS.stream().filter(layer -> layer instanceof WindowResizeListener),
				LISTENERS_WINDOW_RESIZE.stream());
	}
	
	private static final Collection<InputListener> LISTENERS_INPUT = Collections.synchronizedCollection(new CopyOnWriteArrayList<>());
	
	@SuppressWarnings("unchecked")
	static Stream<InputListener> getInputListeners() {
		return Stream.concat(
				LISTENERS_INPUT.stream(),
				(Stream<? extends InputListener>) LAYERS_REVERSE.stream().filter(layer -> layer instanceof InputListener));
	}
	
	private static int cursorX = 0, cursorY = 0;
	
	/*
	 * Layers
	 */
	
	/**
	 * Adds a normal layer to the layer stack.
	 * @param layer the layer to add
	 * @see {@link #getLayers()}
	 */
	static void addLayer(Layer layer) {
		getLayers().add(firstStickyLayer++, layer);
	}
	
	/**
	 * Adds a sticky layer to the layer stack.
	 * @param layer the layer to add
	 * @see {@link #getLayers()}
	 */
	static void addStickyLayer(Layer layer) {
		getLayers().add(layer);
	}
	
	/**
	 * Adds a normal layer to the bottom of the layer stack.
	 * @param layer the layer to add
	 * @see {@link #getLayers()}
	 */
	static void addLayerToBottom(Layer layer) {
		getLayers().add(0, layer);
		firstStickyLayer++;
	}
	
	/**
	 * Removes the given layer from the layer stack if it is present.
	 * @param layer the layer to remove
	 * @see {@link #getLayers()}
	 */
	static void removeLayer(Layer layer) {
		synchronized (LAYERS) {
			int index = getLayers().indexOf(layer);
			if (index != -1) {
				if (index < firstStickyLayer) {
					firstStickyLayer--;
				}
				getLayers().remove(index);
			}
		}
	}
	
	/**
	 * Removes all normal layers from the layer stack.
	 * @see {@link #getLayers()}
	 */
	public static void removeAllNormalLayers() {
		synchronized (LAYERS) {
			int layers = firstStickyLayer;
			
			for (int i = 0; i < layers; ++i) {
				getLayers().get(firstStickyLayer - 1).close();
			}
		}
	}
	
	/**
	 * Gets the graphics layer stack.
	 * <p>
	 * Graphics render is organized in {@link Layer}s. Each layer is responsible
	 * for displaying a certain type of visuals. Examples of layers are menus
	 * (each separately), HUD, notifications. Layers are rendered from bottom
	 * to top of the stack.
	 * <p>
	 * Graphics layer stack is divided into <i>normal</i> and {@linkplain StickyLayer <i>sticky</i>} layers.
	 * Normal layers are expected to change based on the current game state. Sticky
	 * layers usually stay throughout the launch. All sticky layers are displayed
	 * on top of all normal layers.
	 */
	public static List<Layer> getLayers() {
		return LAYERS;
	}
	
	/**
	 * Invalidates all root components of all {@link GuiLayer}s present in the layer stack.
	 * @see {@link #getLayers()}
	 */
	public static void invalidateEverything() {
		LAYERS.forEach(layer -> {
			if (layer instanceof GuiLayer) {
				Component root = ((GuiLayer) layer).getRoot();
				if (root != null) {
					root.invalidate();
				}
			}
		});
	}
	
	/*
	 * Render
	 */
	
	private static long frame = 0;
	
	static void render() {
		++frame;
		getLayers().forEach(layer -> layer.render());
	}
	
	/**
	 * Returns the number of the frame that is currently being rendered or has finished rendering.
	 * @return the current frame number starting at 0
	 */
	public static long getFrame() {
		return frame;
	}
	
	/*
	 * GLFW event handling
	 */
	
	private static OneTimeKeyStrokeConsumer oneTimeKeyStrokeConsumer = null;
	
	/**
	 * Calls the consumer on the single next key press. The input is discarded afterwards.
	 * Modifier keys and fullscreen switch are never consumed.
	 * @param consumer the consumer
	 */
	public static void onNextKeyStroke(OneTimeKeyStrokeConsumer consumer) {
		oneTimeKeyStrokeConsumer = consumer;
	}
	
	static void handleKeyInput(long window, int key, int scancode, int action, int mods) {
		if (window != getWindow()) {
			return;
		}
		
		if (action == GLFW_PRESS && oneTimeKeyStrokeConsumer != null
				&& key != GLFW_KEY_LEFT_SHIFT
				&& key != GLFW_KEY_LEFT_ALT
				&& key != GLFW_KEY_LEFT_CONTROL) {
			oneTimeKeyStrokeConsumer.onKeyStroke(new KeyStroke(key, mods, action));
			oneTimeKeyStrokeConsumer = null;
			return;
		}
		
		KeyInput input = new KeyInput(key, action, mods);
		
		if (ModuleGraphicsInterface.FULLSCREEN_KEY.get().matches(input, true)) {
			setFullscreen(!isFullscreen());
			return;
		}
		
		dispatchInput(input);
	}
	
	static void handleCharInput(long window, int character) {
		if (window != getWindow()) {
			return;
		}

		dispatchInput(new CharInput(character));
	}
	
	static void dispatchInput(Input input) {
		Iterator<InputListener> iterator = getInputListeners().iterator();
		synchronized (iterator) {
			while (iterator.hasNext() && !input.isConsumed()) {
				iterator.next().onInput(input);
			}
		}
	}
	
	static void handleWindowResize(long window, int width, int height) {
		if (window != getWindow()) {
			return;
		}
		if (width == 0 || height == 0) {
			// Minimized
			return;
		}
		
		ModuleGraphicsInterface.WINDOW_MAXIMIZED.set(glfwGetWindowAttrib(getWindow(), GLFW_MAXIMIZED) == GLFW_TRUE);
		
		if (isWindowed()) {
			ModuleGraphicsInterface.WINDOW_WIDTH.set(width);
			ModuleGraphicsInterface.WINDOW_HEIGHT.set(height);
		} else {
			unsavedWidth = width;
			unsavedHeight = height;
		}
		
		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		checkOpenGLErrors();
		
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
	
	static void handleScroll(long window, double xMod, double yMod) {
		if (window != getWindow()) {
			return;
		}
		
		dispatchInput(new ScrollInput((int) yMod));
	}
	
	static void handleWindowClose(long window) {
		CrystalFarm.exit("Window closed", 0);
	}
	
	/*
	 * GLFW window operations
	 */
	
	/**
	 * Gets the GLFW window handle.
	 * @return the window handle usable in GLFW functions
	 */
	public static long getWindow() {
		return window;
	}
	
	static void setWindow(long window) {
		GraphicsInterface.window = window;
	}
	
	/**
	 * Gets the current window width.
	 * @return the width of the display area in pixels 
	 */
	// TODO consider whether to use framebuffers
	public static int getWindowWidth() {
		return isWindowed() ? ModuleGraphicsInterface.WINDOW_WIDTH.get() : unsavedWidth;
	}
	
	/**
	 * Gets the current window height.
	 * @return the height of the display area in pixels 
	 */
	public static int getWindowHeight() {
		return isWindowed() ? ModuleGraphicsInterface.WINDOW_HEIGHT.get() : unsavedHeight;
	}
	
	/**
	 * Gets the current X coordinate of the cursor in display area coordinates.
	 * @return the distance between the cursor and the left display area border
	 */
	public static int getCursorX() {
		return cursorX;
	}
	
	/**
	 * Gets the current Y coordinate of the cursor in display area coordinates.
	 * @return the distance between the cursor and the top display area border
	 */
	public static int getCursorY() {
		return cursorY;
	}
	
	/**
	 * Checks whether the cursor is inside the given region.
	 * @param x the X coordinate of the upper-left corner of the area
	 * @param y the Y coordinate of the upper-left corner of the area
	 * @param width the width of the area
	 * @param height the height of the area
	 * @return {@code true} when the cursor is currently in the given region
	 */
	public static boolean isCursorIn(int x, int y, int width, int height) {
		return cursorX >= x && cursorX < x + width &&
				cursorY >= y && cursorY < y + height;
	}
	
	/**
	 * Checks whether the window is in fullscreen mode.
	 * @return {@code true} when the window is in fullscreen mode
	 */
	public static boolean isFullscreen() {
		return ModuleGraphicsInterface.WINDOW_FULLSCREEN.get();
	}
	
	/**
	 * Checks whether the window is maximized.
	 * @return {@code true} when the window is maximized
	 */
	public static boolean isMaximized() {
		return ModuleGraphicsInterface.WINDOW_MAXIMIZED.get();
	}
	
	/**
	 * Checks whether this window is in in windowed mode and is not maximized.
	 * @return {@code true} when the window is neither in fullscreen nor in maximized mode
	 */
	public static boolean isWindowed() {
		return !isFullscreen() && !isMaximized();
	}
	
	/**
	 * Sets the window's fullscreen state.
	 * @param fullscreen whether to set window to fullscreen
	 */
	public static void setFullscreen(boolean fullscreen) {
		ModuleGraphicsInterface.WINDOW_FULLSCREEN.set(fullscreen);
		syncFullscreen();
	}
	
	static void syncFullscreen() {
		if (isFullscreen()) {
			makeFullscreen();
		} else {
			if (isMaximized()) {
				makeMaximized();
			} else {
				makeWindowed();
			}
		}
	}

	private static void makeFullscreen() {
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowMonitor(
				getWindow(),
				glfwGetPrimaryMonitor(),
				0,
				0,
				unsavedWidth = vidmode.width(),
				unsavedHeight = vidmode.height(),
				0);
	}
	
	private static void makeMaximized() {
		// When initializing window changing monitor breaks everything
		if (!isGraphicsReady()) return;
		
		glfwSetWindowMonitor(
				getWindow(),
				0,
				0,
				0,
				getWindowWidth(),
				getWindowHeight(),
				0);
	}

	private static void makeWindowed() {
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowMonitor(
				getWindow(),
				0,
				(vidmode.width() - getWindowWidth()) / 2,
				(vidmode.height() - getWindowHeight()) / 2,
				getWindowWidth(),
				getWindowHeight(),
				0);
	}

	/*
	 * Timing
	 */
	
	static double frameLength = 1 / 60 * 1000;
	
	/**
	 * Returns the current time in milliseconds. The time is relative to
	 * an unspecified moment but is consistent within a launch. This value
	 * is not guaranteed to be accurate to the millisecond.
	 * @return the elapsed time in 1/1000th of a second
	 */
	public static double time() {
		return glfwGetTime() * 1000.0;
	}
	
	/**
	 * Returns the duration of the last render cycle in milliseconds.
	 * @return the time taken to render the previous frame.
	 */
	public static double frameLength() {
		return frameLength;
	}
	
	/**
	 * Returns the current Frames-Per-Second (FPS).
	 * @return the amount of render cycles per second
	 */
	public static double getFps() {
		return 1000 / frameLength;
	}
	
	/*
	 * Drawing
	 */
	
	private static void applyColor(Color color) {
		glColor4d(color.r, color.g, color.b, color.a);
	}
	
	/**
	 * Fills a rectangle with the given color. The rectangle may not be visible, partially or completely.
	 * <p>
	 * Coordinates are given in <i>display coordinate system</i>, where the point (0;0) corresponds to
	 * the upper-left corner of the screen, X axis is positive to the right and Y axis is positive to the bottom.
	 * @param x the X coordinate of the upper-left corner of the rectangle
	 * @param y the Y coordinate of the upper-left corner of the rectangle
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 * @param color the {@link Color} to use
	 */
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
		
		checkOpenGLErrors();
	}
	
	/**
	 * Fills a rectangle with color {@code color} with a border of color {@code border} and thickness
	 * {@code borderThickness}. The border is drawn inside the rectangle. The rectangle may not be visible,
	 * partially or completely.
	 * <p>
	 * Coordinates are given in <i>display coordinate system</i>, where the point (0;0) corresponds to
	 * the upper-left corner of the screen, X axis is positive to the right and Y axis is positive to the bottom.
	 * @param x the X coordinate of the upper-left corner of the rectangle
	 * @param y the Y coordinate of the upper-left corner of the rectangle
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 * @param color the {@link Color} to use for the inside
	 * @param border the {@link Color} to use for the border
	 * @param borderThickness the width of the border
	 */
	public static void fillRectangle(int x, int y, int width, int height, Color color, Color border, int borderThickness) {
		fillRectangle(x, y, width, height, border);
		fillRectangle(
				x + borderThickness,
				y + borderThickness,
				width - 2*borderThickness,
				height - 2*borderThickness,
				color);
	}
	
	/**
	 * Creates a rectangular mask. The rectangle may not be visible,
	 * partially or completely.
	 * <p>
	 * Coordinates are given in <i>display coordinate system</i>, where the point (0;0) corresponds to
	 * the upper-left corner of the screen, X axis is positive to the right and Y axis is positive to the bottom.
	 * @param x the X coordinate of the upper-left corner of the rectangle
	 * @param y the Y coordinate of the upper-left corner of the rectangle
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 * @see {@link #resetMask()}
	 */
	public static void setMask(int x, int y, int width, int height) {
		glEnable(GL_STENCIL_TEST);
		
	    glStencilFunc(GL_ALWAYS, 1, 0xFF);
	    glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
	    glStencilMask(0xFF);
	    glClear(GL_STENCIL_BUFFER_BIT);
	    
	    width += x;
		height += y;
		
		glBegin(GL_QUADS);
			
			glColor4b((byte) 0, (byte) 0, (byte) 0, (byte) 0);
			glVertex2i(x, y);
			glVertex2i(x, height);
			glVertex2i(width, height);
			glVertex2i(width, y);
		
		glEnd();
		
		glStencilFunc(GL_EQUAL, 1, 0xFF);
	    glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
	    glStencilMask(0x00);
	    
	    checkOpenGLErrors();
	}
	
	/**
	 * Discards any present masks.
	 * @see {@link #setMask(int, int, int, int)}
	 */
	public static void resetMask() {
		glDisable(GL_STENCIL_TEST);
		// glDisable(GL_STENCIL_TEST) should never fail, checkOpenGLErrors() redundant
	}

	private static final int UL_X = 0, UL_Y = 1, LR_X = 2, LR_Y = 3;
	private static final double[] DRAW_TEXTURE__COORDS = new double[4];
	
	/**
	 * Renders the given part of the given texture. The upper-left corner of the rendered fragment
	 * will be located at ({@code x};{@code y}). The dimensions of the fragment are ({@code width};{@code height}).
	 * The texture's size will be changed accordingly. A color filter may be applied with a non-null
	 * {@code filter}. The fragment is rotated in {@code direction} (use {@link Direction#UP} to ignore rotation).
	 * The fragment may not be visible, partially or completely.
	 * <p>
	 * Coordinates are given in <i>display coordinate system</i>, where the point (0;0) corresponds to
	 * the upper-left corner of the screen, X axis is positive to the right and Y axis is positive to the bottom.
	 * @param x the X coordinate of the upper-left corner of the fragment to render
	 * @param y the Y coordinate of the upper-left corner of the fragment to render
	 * @param width the width to fit the fragment into
	 * @param height the height to fit the fragment into
	 * @param texture the {@link Texture} to render
	 * @param tileX the X coordinate of the tile. Ignored when {@code texture.getTileSize() == 0}
	 * @param tileY the Y coordinate of the tile. Ignored when {@code texture.getTileSize() == 0}
	 * @param filter the color filter to apply or {@code null} for none
	 * @param direction the direction in which to rotate the texture
	 * @see {@link Texture#render(int, int, int, int, Color, Direction)
	 */
	public static void drawTexture(
			int x,
			int y,
			int width,
			int height,
			Texture texture,
			double texXStart,
			double texYStart,
			double texXEnd,
			double texYEnd,
			Color filter,
			Direction direction) {
		
		int endX = width + x,
				endY = height + y;
		
		DRAW_TEXTURE__COORDS[UL_X] = texXStart;
		DRAW_TEXTURE__COORDS[UL_Y] = texYStart;
		DRAW_TEXTURE__COORDS[LR_X] = texXEnd;
		DRAW_TEXTURE__COORDS[LR_Y] = texYEnd;
		
		direction.turnCoordiates(DRAW_TEXTURE__COORDS);

		glBindTexture(GL_TEXTURE_2D, texture.getTextureId());
		
		glBegin(GL_QUADS);
		
			if (filter == null) {
				glColor3d(1, 1, 1);
			} else {
				applyColor(filter);
			}
			
			glTexCoord2d(DRAW_TEXTURE__COORDS[UL_X], DRAW_TEXTURE__COORDS[UL_Y]); glVertex2i(x, y);
			glTexCoord2d(DRAW_TEXTURE__COORDS[UL_X], DRAW_TEXTURE__COORDS[LR_Y]); glVertex2i(x, endY);
			glTexCoord2d(DRAW_TEXTURE__COORDS[LR_X], DRAW_TEXTURE__COORDS[LR_Y]); glVertex2i(endX, endY);
			glTexCoord2d(DRAW_TEXTURE__COORDS[LR_X], DRAW_TEXTURE__COORDS[UL_Y]); glVertex2i(endX, y);
			
		glEnd();
		
		glBindTexture(GL_TEXTURE_2D, 0);
		
		checkOpenGLErrors();
	}
	
	/*
	 * Misc
	 */
	
	private static class Request<V> {
		
		private final Callable<V> callable;
		private boolean executed = false;
		private final Thread scheduler;
		private final boolean complain;
		
		private V result = null;
		private Exception exception = null;
		
		public Request(Callable<V> callable, boolean complain) {
			this.callable = callable;
			this.scheduler = Thread.currentThread();
			this.complain = complain;
		}

		public void call() {
			try {
				result = callable.call();
			} catch (Exception e) {
				if (complain) {
					handleCallableException(callable, e, scheduler);
					return;
				}
				exception = e;
			}
			executed = true;
			
			synchronized (this) {
				notifyAll();
			}
		}
		
		public V get() throws Exception {
			if (exception == null) {
				return result;
			}
			
			throw exception;
		}
		
	}
	
	private static final Queue<Request<?>> RUN_QUEUE = new ConcurrentLinkedQueue<>();
	
	static void processRunQueue() {
		while (!RUN_QUEUE.isEmpty()) {
			RUN_QUEUE.poll().call();
		}
	}
	
	/**
	 * Schedules the callable to be called in the render thread as soon as possible.
	 * If invoked from the render thread, executes the callable right now.
	 * <p>Returned value is discarded. All exceptions are treated as critical errors and reported.
	 * 
	 * @param callable the routine to execute
	 */
	public static void run(Callable<?> callable) {
		if (isRenderThread()) {
			try {
				callable.call();
			} catch (Exception e) {
				handleCallableException(callable, e, Thread.currentThread());
			}
		}
		
		runLater(callable);
	}
	
	/**
	 * Schedules the callable to be called in the render thread as soon as possible.
	 * If invoked from the render thread, executes the callable outside the render routine.
	 * <p>Returned value is discarded. All exceptions are treated as critical errors and reported.
	 * 
	 * @param callable the routine to execute
	 */
	public static void runLater(Callable<?> callable) {
		RUN_QUEUE.add(new Request<>(callable, true));
	}
	
	/**
	 * Schedules the callable to be called in the render thread as soon as possible and waits for it to execute.
	 * If invoked from the render thread, executes the callable right now.
	 * 
	 * <p>All exceptions are propagated to invoker.
	 * 
	 * @param callable the routine to execute
	 * @return the valued returned by the callable
	 */
	public static <V> V runNow(Callable<V> callable) throws Exception {
		if (isRenderThread()) {
			return callable.call();
		}
		
		Request<V> request = new Request<V>(callable, false);
		RUN_QUEUE.add(request);
		
		while (!request.executed) {
			try {
				request.wait();
			} catch (InterruptedException e) {
				// Do nothing and wait again
			}
		}
		
		return request.get();
	}
	
	/**
	 * Schedules the runnable to be called in the render thread as soon as possible and waits for it to execute.
	 * If invoked from the render thread, executes the runnable right now.
	 * 
	 * <p>All uncaught exceptions are propagated to invoker.
	 * 
	 * @param runnable the routine to execute
	 */
	public static void runNow(Runnable runnable) {
		if (isRenderThread()) {
			runnable.run();
		}
		
		Request<Void> request = new Request<>(() -> {
			runnable.run();
			return null;
		}, false);
		RUN_QUEUE.add(request);
		
		while (!request.executed) {
			try {
				request.wait();
			} catch (InterruptedException e) {
				// Do nothing and wait again
			}
		}
		
		if (request.exception != null) {
			handleCallableException(runnable, request.exception, request.scheduler);
		}
	}
	
	private static void handleCallableException(Object callable, Exception e, Thread scheduler) {
		ExecutionReport.reportCriticalError(e, null, "Callable %s (%s) scheduled by thread %s failed to execute",
				callable.toString(), callable.getClass().toString(), scheduler.getName());
	}
	
	/**
	 * Dumps the layer stack producing an indented hierarchical human-readable view of the current graphical structure.
	 * This method is designed for debug purposes and may be computationally difficult.
	 * @return a layer stack dump
	 */
	public static String dumpLayers() {
		IndentedStringBuilder sb = new IndentedStringBuilder('\t', 1);
		getLayers().forEach(layer -> layer.dump(sb));
		return sb.toString();
	}
	
	/**
	 * Returns the render state of the caller based on the number of the last frame the caller has rendered.
	 * @param lastFrame the last frame rendered by the caller or -1 if the caller has never been rendered
	 * @return <code>[rendered]</code>, <code>[NOT rendered]</code> or <code>[NEVER rendered]</code>
	 */
	public static String dumpIsRendered(long lastFrame) {
		if (lastFrame == getFrame()) {
			return "[rendered]";
		} else if (lastFrame < 0) {
			return "[NEVER rendered]";
		} else {
			return "[NOT rendered]";
		}
	}
	
	/**
	 * Adds an {@link InputListener}. Layers must not be registered explicitly.
	 * <p>
	 * When dispatching input, the explicitly registered input listeners are notified
	 * first, and only then layers that implement {@code InputListener} are notified
	 * in the order <i>inverse</i> of their render order (from the top of the layer stack
	 * to its bottom).
	 * @param l the listener to add
	 */
	public static void addInputListener(InputListener l) {
		LISTENERS_INPUT.add(l);
	}
	
	/**
	 * Adds an {@link WindowResizeListener}. Layers must not be registered explicitly.
	 * @param l the listener to add
	 */
	public static void addWindowResizeListener(WindowResizeListener l) {
		LISTENERS_WINDOW_RESIZE.add(l);
	}
	
	/**
	 * Checks whether the currently executing thread is the OpenGL render thread.
	 * @return {@code true} when invoked from the render thread
	 */
	public static boolean isRenderThread() {
		return Thread.currentThread() == renderThread;
	}
	
	static void setRenderThread(Thread thread) {
		GraphicsInterface.renderThread = thread;
	}

	/**
	 * Checks whether {@link GraphicsInterface} has been set up.
	 * @return {@code true} when all {@code GraphicsInterface} methods are ready to be invoked.
	 */
	public static boolean isGraphicsReady() {
		return graphicsReady;
	}

	static void setGraphicsReady() {
		GraphicsInterface.graphicsReady = true;
	}

	/**
	 * Gets the {@link GLCapabilities} object current in the render thread.
	 * This object can be used to examine graphics capabilities of OpenGL.
	 * @return the {@code GLCapabilities} used by the render threads
	 */
	public static GLCapabilities getGlCapabilities() {
		return glCapabilities;
	}

	static void setGlCapabilities(GLCapabilities glCapabilities) {
		GraphicsInterface.glCapabilities = glCapabilities;
	}
	
	private static int lastError = GL_NO_ERROR;

	public static int getLastError() {
		return lastError;
	}
	
	public static void checkOpenGLErrors() {
		int error = glGetError();
		if (error != GL_NO_ERROR) {
			lastError = error;
			throw new OpenGLException(error);
		}
	}
	
	private static String getOpenGLErrorName(int code) {
		switch (code) {
		case GL_NO_ERROR:			return "NO_ERROR";
		case GL_INVALID_ENUM:		return "INVALID_ENUM";
		case GL_INVALID_VALUE:		return "INVALID_VALUE";
		case GL_INVALID_OPERATION:	return "INVALID_OPERATION";
		case GL_OUT_OF_MEMORY:		return "OUT_OF_MEMORY";
		case GL_STACK_OVERFLOW:		return "STACK_OVERFLOW";
		case GL_STACK_UNDERFLOW:	return "STACK_UNDERFLOW";
		
		default:					return "Unknown to OpenGL 1.1";
		}
	}
	
	public static String getOpenGLErrorDescription(int code) {
		return getOpenGLErrorName(code) + " / " + code + " / " + new String(NumberUtil.toFullHex(code));
	}

}
