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
package ru.windcorp.crystalfarm.logic.server;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ru.windcorp.crystalfarm.logic.Data;
import ru.windcorp.crystalfarm.logic.Island;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.stream.CountingDataInput;

public class World {
	
	private final WorldMeta meta = new WorldMeta();
	private final Map<String, Island> islands = Collections.synchronizedMap(new HashMap<>());
	private final Map<String, Data> data = Collections.synchronizedMap(new HashMap<>());
	
	public WorldMeta getMeta() {
		return meta;
	}
	
	public Map<String, Data> getData() {
		return data;
	}
	
	public Data getData(String name) {
		return getData().get(name);
	}
	
	public Map<String, Island> getIslands() {
		return islands;
	}
	
	public Island getIsland(String name) {
		return getIslands().get(name);
	}
	
	public void read(InputStream is) throws IOException, SyntaxException {
		CountingDataInput input = new CountingDataInput(is);
		
		readMeta(input, getMeta());
		
		int amount;
		
		amount = input.readInt();
		if (amount < 0) {
			throw new SyntaxException("Data amount is negative (0x" + Integer.toHexString(amount) + ")");
		}
		
		synchronized (getData()) {
			for (int i = 0; i < amount; ++i) {
				String name = input.readUTF();
				Data data = getData(name);
				if (data == null) {
					throw new SyntaxException("Data with name \"" + name + "\" has not been registered");
				}
				
				input.pushCounter();
				data.readAll(input);
				
				int read = (int) input.popCounter();
				int length = input.readInt();
				if (read != length) {
					throw new SyntaxException("Island Data " + name + " corrupted: expected length 0x" + Integer.toHexString(length) + ", got length 0x" + Integer.toHexString(read));
				}
			}
		}
		
		amount = input.readInt();
		if (amount < 0) {
			throw new SyntaxException("Island amount is negative (0x" + Integer.toHexString(amount) + ")");
		}
		
		synchronized (getIslands()) {
			for (int i = 0; i < amount; ++i) {
				String name = input.readUTF();
				Island data = getIsland(name);
				if (data == null) {
					throw new SyntaxException("Island with name \"" + name + "\" has not been registered");
				}
				
				input.pushCounter();
				data.read(input);
				
				int read = (int) input.popCounter();
				int length = input.readInt();
				if (read != length) {
					throw new SyntaxException("Island " + name + " corrupted: expected length 0x" + Integer.toHexString(length) + ", got length 0x" + Integer.toHexString(read));
				}
			}
		}
	}
	
	public WorldMeta readMeta(InputStream is) throws IOException, SyntaxException {
		WorldMeta result = new WorldMeta();
		readMeta(new DataInputStream(is), result);
		return result;
	}
	
	private void readMeta(DataInput input, WorldMeta meta) throws IOException, SyntaxException {
		meta.readAll(input);
	}
	
	public void write(DataOutput output) throws IOException {
		
	}

}