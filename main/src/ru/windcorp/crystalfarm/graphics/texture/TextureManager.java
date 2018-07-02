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
package ru.windcorp.crystalfarm.graphics.texture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL12;

import ru.windcorp.crystalfarm.CrystalFarmResourceManagers;
import ru.windcorp.crystalfarm.graphics.GraphicsInterface;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.grh.*;
import ru.windcorp.tge2.util.vectors.Vector2;

import static org.lwjgl.opengl.GL11.*;

public class TextureManager {
	
	private static final ColorModel COLOR_MODEL = new ComponentColorModel(
			ColorSpace.getInstance(ColorSpace.CS_sRGB),	// Use RGB
			new int[] {8,8,8,8},						// Use every bit
			true,										// Has alpha
			false,										// Not premultiplied
			ComponentColorModel.TRANSLUCENT,			// Can have any alpha
			DataBuffer.TYPE_BYTE);						// Alpha is specified with one byte
	
	private static final Hashtable<?, ?> CANVAS_HASHTABLE = new Hashtable<Object, Object>();
	private static final java.awt.Color CANVAS_BG = new Color(0, 0, 0, 0);
	
	private static final Map<String, Texture> TEXTURES = Collections.synchronizedMap(new HashMap<>());
	
	private static final Queue<Vector2<TexturePrimitive, ByteBuffer>> LOAD_QUEUE = new ConcurrentLinkedQueue<>();
	
	private static boolean enableDebug;
	
	private static Resource getResource(String textureName) {
		return CrystalFarmResourceManagers.RM_ASSETS.getResource("texture/" + textureName + ".png");
	}
	
	public static Vector2<TexturePrimitive, ByteBuffer> loadToByteBuffer(String textureName, boolean filter) {
		if (getEnableDebug()) Log.debug("Loading texture " + textureName + " into memory");
		Resource resource = getResource(textureName);
		
		String problem = resource.canRead();
		if (problem != null) {
			ExecutionReport.reportCriticalError(null,
					ExecutionReport.rscMissing(resource.toString(), problem), null);
			return null;
		}
		
		BufferedImage source;
		
		try {
			source = ImageIO.read(resource.getInputStream());
		} catch (IOException e) {
			ExecutionReport.reportCriticalError(e,
					ExecutionReport.rscCorrupt(resource.toString(), "Could not read or parse texture file"), null);
			return null;
		}
		
		int textureWidth = toPowerOf2(source.getWidth()),
				textureHeight = toPowerOf2(source.getHeight());
		
		WritableRaster raster = Raster.createInterleavedRaster(
				DataBuffer.TYPE_BYTE,
				textureWidth,
				textureHeight,
				4,						// RGBA
				null);
		
		BufferedImage canvas = new BufferedImage(COLOR_MODEL, raster, false, CANVAS_HASHTABLE);
		
		Graphics g = canvas.createGraphics();
		g.setColor(CANVAS_BG);
		g.fillRect(0, 0, source.getWidth(), source.getHeight());
		g.drawImage(source, 0, 0, null);
		g.dispose();
		
		byte[] data = ((DataBufferByte) canvas.getRaster().getDataBuffer()).getData();
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
		buffer.order(ByteOrder.nativeOrder());
		buffer.put(data);
		buffer.flip();
		
		TexturePrimitive result = new TexturePrimitive(textureName, source.getWidth(), source.getHeight(), textureWidth, textureHeight, filter);
		
		return new Vector2<>(result, buffer);
	}
	
	public static TexturePrimitive loadTexture(String textureName, boolean filter) {
		Vector2<TexturePrimitive, ByteBuffer> vec = loadToByteBuffer(textureName, filter);
		addToLoadQueue(vec);
		return vec.a;
	}
	
	private static int toPowerOf2(int i) {
		int result = 1;
		do {
			result *= 2;
		} while (result < i);
		return result;
	}
	
	private static void loadInGL(TexturePrimitive image, ByteBuffer data) {
		if (getEnableDebug()) Log.debug("Loading texture " + image + " into OpenGL");
		
		int textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);
		
		if (image.isFiltered()) {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		} else {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		}
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		glTexImage2D(
				GL_TEXTURE_2D,				// Load 2D image
				0,							// Not mipmapped
				GL_RGBA,					// Use RGBA
				image.getTextureWidth(),	// Width
				image.getTextureHeight(),	// Height
				0,							// No border
				GL_RGBA,					// Use RGBA (req.)
				GL_UNSIGNED_BYTE,			// Use unsigned bytes
				data);						// Data buffer
		
		glBindTexture(GL_TEXTURE_2D, 0);
		
		image.setTextureId(textureId);
	}
	
	public static void addToLoadQueue(Vector2<TexturePrimitive, ByteBuffer> vec) {
		if (GraphicsInterface.isRenderThread()) {
			loadInGL(vec.a, vec.b);
		} else {
			LOAD_QUEUE.add(vec);
		}
	}
	
	public static void processLoadQueue() {
		Vector2<TexturePrimitive, ByteBuffer> vec;
		
		while (!LOAD_QUEUE.isEmpty()) {
			vec = LOAD_QUEUE.poll();
			loadInGL(vec.a, vec.b);
		}
	}
	
	public static void register(Texture texture) {
		if (TEXTURES.put(texture.getName(), texture) != null) {
			ExecutionReport.reportCriticalError(null, null,
					"Texture %s has already been registered", texture.getName());
		}
	}
	
	public static <T extends Texture> T get(String name, Class<T> classFilter) {
		Texture texture = TEXTURES.get(name);
		if (texture == null || !classFilter.isInstance(texture)) {
			return null;
		}
		
		return classFilter.cast(texture);
	}

	public static boolean getEnableDebug() {
		return enableDebug;
	}

	public static void setEnableDebug(boolean enableDebug) {
		TextureManager.enableDebug = enableDebug;
	}

}
