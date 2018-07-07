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
import ru.windcorp.crystalfarm.logic.server.World;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.struct.mod.ModNameable;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.stream.CountingDataInput;
import ru.windcorp.tge2.util.stream.CountingDataOutput;

public abstract class Level extends ModNameable implements Cloneable {
	
	private WeakReference<Island> island = null;

	public Level(Mod mod, String name) {
		super(mod, name);
	}
	
	void setIsland(Island island) {
		if (island == null) this.island = null;
		this.island = new WeakReference<Island>(island);
	}
	
	public Island getIsland() {
		if (this.island == null) {
			return null;
		}
		
		return this.island.get();
	}
	
	public World getWorld() {
		Island island = getIsland();
		if (island == null) {
			return null;
		}
		
		return island.getWorld();
	}
	
	public abstract void render(View view);
	
	@Override
	protected Level clone() {
		Level level = null;
		try {
			level = (Level) super.clone();
		} catch (CloneNotSupportedException e) {
			// Never happens
		}
		
		return level;
	}
	
	public abstract void read(CountingDataInput input) throws IOException, SyntaxException;
	public abstract void write(CountingDataOutput output) throws IOException;
	public abstract void readUpdate(DataInput input) throws IOException, SyntaxException;
	public abstract void writeUpdate(DataOutput output) throws IOException;

	public abstract void tick(World world, Island island, long length, long time);
	
	public boolean checkCollisions(Collideable col) {
		return false;
	}
	
	public void pushOutside(DynamicCollideable col) {
		// Do nothing
	}

}
