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
import ru.windcorp.crystalfarm.translation.TString;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public abstract class Tile extends Updateable {
	
	private final Mod mod;
	private final String id;
	private final TString name;
	
	private WeakReference<TileLevel<?>> level = null;
	private int nid;
	
	private boolean isTickable;

	public Tile(Mod mod, String id, TString name) {
		this.mod = mod;
		this.id = mod.getName() + ":" + id;
		this.name = name;
	}
	
	protected void setLevel(TileLevel<?> level) {
		if (level == null) {
			this.level = null;
			return;
		}
		
		this.level = new WeakReference<TileLevel<?>>(level);

		if (isTickable()) {
			level.getTickableTiles().add(this);
		}
	}
	
	public TileLevel<?> getLevel() {
		if (this.level == null) {
			return null;
		}
		
		return this.level.get();
	}
	
	public Island getIsland() {
		Level level = getLevel();
		if (level == null) {
			return null;
		}
		
		return level.getIsland();
	}
	
	public World getWorld() {
		Level level = getLevel();
		if (level == null) {
			return null;
		}
		
		return level.getWorld();
	}
	
	void setNid(int nid) {
		this.nid = nid;
	}
	
	int getNid() {
		return nid;
	}

	public Mod getMod() {
		return mod;
	}

	public String getId() {
		return id;
	}

	public TString getName() {
		return name;
	}
	
	public boolean isTickable() {
		return isTickable;
	}

	public void setTickable(boolean isTickable) {
		this.isTickable = isTickable;
	}

	@Override
	protected Tile clone() {
		return (Tile) super.clone();
	}
	
	@Override
	public void read(DataInput input, int change) throws IOException, SyntaxException {
		// Do nothing
	}
	
	@Override
	public void write(DataOutput output, int change) throws IOException {
		// Do nothing
	}
	
	public abstract void render(View view, int x, int y);
	
	public void tick(World world, Island island, Level level, long length, long time) {
		// Do nothing
	}

}
