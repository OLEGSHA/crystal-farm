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
import java.io.DataOutput;
import java.io.IOException;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.logic.Data;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public class WorldMeta extends Data {

	private String displayName	= null;
	private long time			= 0;
	private long tickLength		= 1000 / 20;
	
	public WorldMeta() {
		super(InbuiltMod.INST, "IslandMeta");
	}

	@Override
	public void read(DataInput input, int change) throws IOException, SyntaxException {
		displayName		= input.readUTF();
		time			= input.readLong();
		tickLength		= input.readLong();
	}

	@Override
	public void write(DataOutput output, int change) throws IOException {
		output.writeUTF(displayName);
		output.writeLong(time);
		output.writeLong(tickLength);
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public long getTime() {
		return time;
	}
	
	public long getTickLength() {
		return tickLength;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		setChangeAll();
	}
	
	public void setTime(long time) {
		this.time = time;
		setChangeAll();
	}
	
	public void setTickLength(long tickLength) {
		this.tickLength = tickLength;
		setChangeAll();
	}

}
