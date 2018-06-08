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
package ru.windcorp.crystalfarm.graphics.fonts;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ru.windcorp.crystalfarm.CrystalFarmResourceManagers;
import ru.windcorp.crystalfarm.graphics.texture.TextureManager;
import ru.windcorp.crystalfarm.graphics.texture.TexturePrimitive;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.grh.Resource;

/**
 * CFFont syntax:
 */
public class FontManager {
	
	private static final Map<String, Font> FONTS = Collections.synchronizedMap(new HashMap<>());
	
	public static Font getFont(String name) {
		if (!FONTS.containsKey(name)) {
			FONTS.put(name, createFont(name));
		}
		
		return FONTS.get(name);
	}
	
	private static Font createFont(String name) {
		Resource resource = CrystalFarmResourceManagers.RM_ASSETS.getResource("fonts/" + name + ".cffont");
		
		String problem = resource.canRead();
		if (problem != null) {
			ExecutionReport.reportCriticalError(null,
					ExecutionReport.rscUnrch(resource.toString(), problem),
					null);
			return null;
		}
		
		try {
			return loadFont(name, new DataInputStream(resource.getInputStream()));
		} catch (IOException e) {
			ExecutionReport.reportCriticalError(e,
					ExecutionReport.rscUnrch(resource.toString(), "Could not read font file"),
					null);
			return null;
		}
	}
	
	private static Font loadFont(String name, DataInput input) throws IOException {
		int height = input.readByte();
		int banks = input.readUnsignedByte();
		
		Log.debugObj(banks, height);
		
		FontSymbol[][] data = new FontSymbol[0xFF][];
		FontSymbol unknown = readSymbol(input, name, height);
		
		for (int bank = 0; bank < banks; ++bank) {
			
			int bankMSB = input.readUnsignedByte();
			int symbols = input.readUnsignedByte();
			
			Log.debug("In bank " + Integer.toHexString(bankMSB) + " symbols " + symbols);
			
			data[bankMSB] = new FontSymbol[0xFF];
			
			for (int symbol = 0; symbol < symbols; ++symbol) {
				FontSymbol current = readSymbol(input, name, height);
				data[bankMSB][current.getChar() % 0xFF] = current;
			}
			
		}
		
		return new Font(name, data, unknown, height);
	}
	
	private static final byte[] WHITE_ARRAY3 = new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
	private static final byte[] WHITE_ARRAY4 = new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

	private static FontSymbol readSymbol(DataInput input, String name, int height) throws IOException {
		char character = input.readChar();
		int width = input.readUnsignedByte();
		
		Log.debugObj(character, width);
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(height * height * 4);
		
		for (int i = 0; i < height * height; ++i) {
			if (i % height < width) {
				buffer.put(WHITE_ARRAY3);
				buffer.put(input.readByte());
			} else {
				buffer.put(WHITE_ARRAY4);
			}
		}
		
		buffer.flip();
		
		TexturePrimitive texture = new TexturePrimitive("Font:" + name + ":" + character, width, height, height, height);
		TextureManager.addToLoadQueue(texture, buffer);
		return new FontSymbol(texture, character);
	}

}
