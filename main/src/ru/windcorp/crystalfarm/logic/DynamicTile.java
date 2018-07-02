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

import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.translation.TString;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public abstract class DynamicTile extends Tile {
	
	public static final int CHANGE_BIT_POSITION = 0;
	
	private int dynId;
	private long statId;
	
	private double x, y;

	public DynamicTile(Mod mod, String id, TString name) {
		super(mod, id, name);
	}

	public int getDynId() {
		return dynId;
	}
	
	public long getStatId() {
		return statId;
	}

	public void setStatId(long statId) {
		this.statId = statId;
	}

	public synchronized double getX() {
		return x;
	}

	public synchronized double getY() {
		return y;
	}
	
	public synchronized void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		setChangeBit(CHANGE_BIT_POSITION);
	}

	synchronized void adopt(DynamicTileLevel<?> dynamicTileLevel, int dynId, long statId) {
		setLevel(dynamicTileLevel);
		this.dynId = dynId;
		this.statId = statId;
	}
	
	@Override
	public void read(DataInput input, int change) throws IOException, SyntaxException {
		super.read(input, change);
		
		if (getChangeBit(change, CHANGE_BIT_POSITION)) {
			x = input.readDouble();
			y = input.readDouble();
		}
	}
	
	@Override
	public void write(DataOutput output, int change) throws IOException {
		super.write(output, change);
		
		if (getChangeBit(change, CHANGE_BIT_POSITION)) {
			output.writeDouble(getX());
			output.writeDouble(getY());
		}
	}
	
	@Override
	protected DynamicTile clone() {
		return (DynamicTile) super.clone();
	}

}
