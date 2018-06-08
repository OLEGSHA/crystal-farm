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
package ru.windcorp.crystalfarm.devtools;

import static ru.windcorp.tge2.util.textui.TextUI.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.windcorp.crystalfarm.CrystalFarm;
import ru.windcorp.tge2.util.Version;

import static ru.windcorp.tge2.util.debug.ConsoleIO.*;

public class CFDTCreateCFFont {
	
	public static final Version VERSION = new Version(1, 0);
	
	private static Graphics2D graphics;
	private static FontMetrics metrics;
	
	private static BufferedImage canvas;
	
	private static int width, height, descend;
	
	private static final Color BG = new Color(0x00, 0x00, 0x00, 0x00);
	private static final Color FG = new Color(0xFF, 0xFF, 0xFF, 0xFF);
	private static final Composite ALPHA_OVERWRITE = AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1);
	private static final Composite ALPHA_BLEND = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
	private static final char[] CHAR_INPUT = new char[1];
	
	public static void main(String[] args) {
		
		write("CFDT Create CFFont v. " + VERSION);
		write("  for " + CrystalFarm.FULL_NAME + " v. " + CrystalFarm.VERSION_CODENAME + "/" + CrystalFarm.VERSION);
		write("");
		write("Crystal Farm Development Tool \"Create CFFont\"");
		write("Licensed under the terms of " + CrystalFarm.LICENSE);
		write("");
		
		File file = new File(prompt("Output file:"));
		if (file.exists()) {
			write("File " + file.getAbsolutePath() + " exists");
			if (ask("Delete file?", "Delete and continue", "Cancel")) {
				if (!file.delete()) {
					write("Could not delete");
					return;
				}
			} else {
				return;
			}
		}
		
		Font base = Font.decode(prompt("Font:"));
		if (base == null) {
			write("Font not found");
			write("Available fonts:");
			
			for (Font f : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
				write(" - " + f.getName());
			}
			
			return;
		}
		
		height = (int) (long) readInteger("Height:", null, false, 8, 256, null, null);
		width = height * 2;
		
		String unknownCharResponse = prompt("Unknown character:");
		if (unknownCharResponse.length() != 1) {
			write("Invalid response length (1 character expected)");
			return;
		}
		char unknownChar = unknownCharResponse.charAt(0);
		
		String maxCharResponse = prompt("Max character (empty for all):");
		if (maxCharResponse.length() > 1) {
			write("Invalid response length (1 character expected)");
			return;
		}
		char maxChar = maxCharResponse.isEmpty() ? Character.MAX_SURROGATE : maxCharResponse.charAt(0);
		
		thickLine();
		
		write("Initializing graphics and buffers...");
		
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		graphics = canvas.createGraphics();
		graphics.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		write("Setting up font...");
		
		Font font = base.deriveFont((float) height);
		graphics.setFont(font);
		
		metrics = graphics.getFontMetrics();
		descend = metrics.getDescent();
		write("Using font size " + height);
		
		write("Preparing chartable...");
		
		int banks = 0;
		int[] symbolsPerBank = new int[0xFF];
		char[][] chartable = new char[0xFF][];
		int totalSymbols = 0;
		int writtenSymbols = 0;
		
		for (char c = 1; c < maxChar; ++c) {
			if (font.canDisplay(c)) {
				int bank = c / 0xFF;
				
				if (chartable[bank] == null) {
					chartable[bank] = new char[0xFF];
					banks++;
				}
				
				chartable[bank][c % 0xFF] = c;
				symbolsPerBank[bank]++;
				totalSymbols++;
			}
		}

		try {
			try (DataOutputStream output = new DataOutputStream(new FileOutputStream(file, false))) {
				
				write("Writing header");
				output.write(height);
				output.write(banks);
				
				write("Writing unknown character");
				writeChar(output, unknownChar);
				
				for (int bank = 0; bank < 0xFF; ++bank) {
					write("Writing bank " + bank);
					if (chartable[bank] == null) {
						write("  Skipped");
						continue;
					}
					
					output.write(bank);
					output.write(symbolsPerBank[bank]);
					
					for (int symbol = 0; symbol < 0xFF; ++symbol) {
						if (chartable[bank][symbol] == 0) {
							continue;
						}
						
						write("  '" + chartable[bank][symbol] + "': " + writtenSymbols++ + "/" + totalSymbols);
						writeChar(output, chartable[bank][symbol]);
					}
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(System.out);
			write("Failed to setup output");
			return;
		} catch (IOException e) {
			e.printStackTrace(System.out);
			write("Failed to write output");
			return;
		}
		
	}
	
	private static void writeChar(DataOutputStream output, char c) throws IOException {
		CHAR_INPUT[0] = c;
		
		graphics.setComposite(ALPHA_OVERWRITE);
		graphics.setColor(BG);
		graphics.fillRect(0, 0, width, height);
		
		graphics.setComposite(ALPHA_BLEND);
		graphics.setColor(FG);
		graphics.drawChars(CHAR_INPUT, 0, 1, 0, height - descend);
		
		int charWidth = metrics.charWidth(c);
		
		output.writeChar(c);
		output.writeByte(charWidth);
		
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < charWidth; ++x) {
				output.writeByte(canvas.getRGB(x, y) & 0xFF);
			}
		}
	}

}
