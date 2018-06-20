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
package ru.windcorp.crystalfarm.logic;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import ru.windcorp.crystalfarm.logic.exception.UnknownIslandDataException;
import ru.windcorp.crystalfarm.logic.exception.UnknownLevelException;
import ru.windcorp.tge2.util.Nameable;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.stream.CountingDataInput;
import ru.windcorp.tge2.util.stream.CountingDataOutput;

public class Island extends Nameable {
	
	private final IslandMeta meta = new IslandMeta();
	private final Level[] levels;
	private final Data[] data;
	
	protected Island(String name, Level[] levels, Data[] data) {
		super(name);
		this.levels = levels;
		this.data = data;
	}
	
	public IslandMeta getMeta() {
		return meta;
	}

	public Level[] getLevels() {
		return levels;
	}
	
	public Level getLevel(String name) {
		for (int i = 0; i < levels.length; ++i) {
			if (levels[i].getName().equals(name)) {
				return levels[i];
			}
		}
		
		return null;
	}

	public Data[] getData() {
		return data;
	}
	
	public Data getData(String name) {
		for (int i = 0; i < data.length; ++i) {
			if (data[i].getName().equals(name)) {
				return data[i];
			}
		}
		
		return null;
	}

	public void render() {
		
	}
	
	public void read(CountingDataInput input) throws IOException, SyntaxException {
		getMeta().readAll(input);
		
		int amount = input.readInt();
		
		if (amount < 0) {
			throw new SyntaxException("Data amount is negative (0x" + Integer.toHexString(amount) + ")");
		}
		
		for (int i = 0; i < amount; ++i) {
			String name = input.readUTF();
			Data data = getData(name);
			
			if (data == null) {
				throw new UnknownIslandDataException("Data with name \"" + name + "\" has not been registered in island " + this,
						name, this);
			}
			
			input.pushCounter();
			data.readAll(input);
			
			int read = (int) input.popCounter();
			int length = input.readInt();
			if (read != length) {
				throw new SyntaxException("Island Data " + name + " corrupted: expected length 0x" + Integer.toHexString(length) + ", got length 0x" + Integer.toHexString(read));
			}
		}
		
		amount = input.readInt();
		
		if (amount < 0) {
			throw new SyntaxException("Level amount is negative (0x" + Integer.toHexString(amount) + ")");
		}
		
		for (int i = 0; i < amount; ++i) {
			String name = input.readUTF();
			Level level = getLevel(name);
			
			if (level == null) {
				throw new UnknownLevelException("Level with name \"" + name + "\" has not been registered in island " + this,
						name, this);
			}
			
			input.pushCounter();
			level.read(input);
			
			int read = (int) input.popCounter();
			int length = input.readInt();
			if (read != length) {
				throw new SyntaxException("Island Level " + name + " corrupted: expected length 0x" + Integer.toHexString(length) + ", got length 0x" + Integer.toHexString(read));
			}
		}
	}
	
	public void write(CountingDataOutput output) throws IOException {
		getMeta().writeAll(output);
		
		output.writeInt(getData().length);
		for (Data data : getData()) {
			output.writeUTF(data.getName());
			
			output.pushCounter();
			data.writeAll(output);
			output.writeInt((int) output.popCounter());
		}
		
		output.writeInt(getLevels().length);
		for (Level level : getLevels()) {
			output.writeUTF(level.getName());
			
			output.pushCounter();
			level.write(output);
			output.writeInt((int) output.popCounter());
		}
	}
	
	public void updateRead(DataInput input) throws IOException, SyntaxException {
		getMeta().readUpdate(input);
		
		for (int i = 0; i < getData().length; ++i) {
			getData()[i].readUpdate(input);
		}
		
		for (int i = 0; i < getLevels().length; ++i) {
			getLevels()[i].readUpdate(input);
		}
	}
	
	public void updateWrite(DataOutput output) throws IOException {
		getMeta().writeUpdate(output);
		
		for (int i = 0; i < getData().length; ++i) {
			getData()[i].writeUpdate(output);
		}
		
		for (int i = 0; i < getLevels().length; ++i) {
			getLevels()[i].writeUpdate(output);
		}
	}

}