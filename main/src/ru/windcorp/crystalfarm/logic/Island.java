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
import java.lang.ref.WeakReference;

import ru.windcorp.crystalfarm.client.View;
import ru.windcorp.crystalfarm.logic.exception.UnknownIslandDataException;
import ru.windcorp.crystalfarm.logic.exception.UnknownLevelException;
import ru.windcorp.crystalfarm.logic.server.World;
import ru.windcorp.tge2.util.Nameable;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.stream.CountingDataInput;
import ru.windcorp.tge2.util.stream.CountingDataOutput;

public class Island extends Nameable {
	
	private final IslandMeta meta = new IslandMeta();
	private final int size;
	private final Level[] levels;
	private final Data[] data;
	
	private WeakReference<World> world = null;
	
	protected Island(String name, int size, Level[] levels, Data[] data) {
		super(name);
		this.size = size;
		this.levels = levels;
		this.data = data;
		
		for (Level l : levels) {
			l.setIsland(this);
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public void setWorld(World world) {
		if (world == null) this.world = null;
		this.world = new WeakReference<World>(world);
	}
	
	public World getWorld() {
		if (this.world == null) {
			return null;
		}
		
		return this.world.get();
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
	
	public <T extends Level> T getLevel(String name, Class<T> clazz) {
		return clazz.cast(getLevel(name));
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

	public void render(View view) {
		for (Level l : getLevels()) {
			try {
				l.render(view);
			} catch (Exception e) {
				GameManager.failToMainMenu(e, "client.renderLevel",
						"Could not render level %s due to a runtime exception: %s",
						l.toString(),
						e.toString());
				break;
			}
		}
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
		Log.topic("Meta");
		Log.debug("Writing metadata");
		try {
			getMeta().writeAll(output);
		} finally {
			Log.end("Meta");
		}
		
		Log.topic("Data");
		Log.debug("Writing data");
		try {
			output.writeInt(getData().length);
			for (Data data : getData()) {
				output.writeUTF(data.getName());
				
				output.pushCounter();
				data.writeAll(output);
				output.writeInt((int) output.popCounter());
			}
		} finally {
			Log.end("Data");
		}
		
		Log.topic("Levels");
		Log.debug("Writing levels");
		try {
			output.writeInt(getLevels().length);
			for (Level level : getLevels()) {
				Log.topic(level.getName());
				Log.debug("Writing level " + level.getName());
				try {
					output.writeUTF(level.getName());
					
					output.pushCounter();
					level.write(output);
					output.writeInt((int) output.popCounter());
				} finally {
					Log.end(level.getName());
				}
			}
		} finally {
			Log.end("Levels");
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