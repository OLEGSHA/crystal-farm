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

import ru.windcorp.tge2.util.exceptions.SyntaxException;

public abstract class Updateable implements Cloneable {
	
	public static final int CHANGE_ALL = 0xFF_FF_FF_FF;
	
	private int change = CHANGE_ALL;
	
	public int getChange() {
		return change;
	}
	
	protected void setChangeBit(int bit) {
		change |= 1 << bit;
	}
	
	protected boolean getChangeBit(int bit) {
		return (change & (1 << bit)) != 0;
	}
	
	protected void clearChange() {
		change = 0;
	}
	
	protected void setChangeAll() {
		change = CHANGE_ALL;
	}
	
	@Override
	protected Updateable clone() {
		Updateable clone = null;
		try {
			clone = (Updateable) super.clone();
		} catch (CloneNotSupportedException e) {
			// Never happens
		}
		clone.setChangeAll();
		return clone;
	}
	
	public abstract void read(DataInput input, int change) throws IOException, SyntaxException;
	public abstract void write(DataOutput output, int change) throws IOException;

	public void readUpdate(DataInput input) throws IOException, SyntaxException {
		int change = input.readInt();
		if (change == 0) {
			return;
		}
		
		read(input, change);
	}
	
	public void writeUpdate(DataOutput output) throws IOException {
		int change = getChange();
		if (change == 0) {
			return;
		}
		
		output.writeInt(getChange());
		write(output, getChange());
		clearChange();
	}
	
	public void readAll(DataInput input) throws IOException, SyntaxException {
		setChangeAll();
		read(input, CHANGE_ALL);
	}
	
	public void writeAll(DataOutput output) throws IOException {
		setChangeAll();
		write(output, CHANGE_ALL);
	}
	
}
