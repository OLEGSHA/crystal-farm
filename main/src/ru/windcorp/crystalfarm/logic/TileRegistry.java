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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ru.windcorp.tge2.util.Nameable;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class TileRegistry<T extends Tile> extends Nameable {
	
	public TileRegistry(String name) {
		super(name);
	}

	private final Map<String, T> tileMap = Collections.synchronizedMap(new HashMap<>());
	private T fallbackTile = null;
	
	protected Map<String, T> getTileMap() {
		return tileMap;
	}
	
	public Collection<T> getTiles() {
		return tileMap.values();
	}
	
	public T get(String id) {
		return getTileMap().get(id);
	}
	
	public boolean exists(String id) {
		return getTileMap().containsKey(id);
	}
	
	public void register(T tile) {
		synchronized (getTileMap()) {
			if (getTileMap().containsKey(tile.getId())) {
				ExecutionReport.reportError(null, null, "Tile with ID %s has already been registered in %s", tile.getId(), this.toString());
				// Continue
			}
			
			getTileMap().put(tile.getId(), tile);
		}
	}
	
	@SuppressWarnings("unchecked")
	public T createNew(String id) {
		T template = get(id);
		if (template == null) {
			ExecutionReport.reportError(null, null, "Tile with ID %s has not been registered in %s", id, this.toString());
			return null;
		}
		return (T) template.clone();
	}
	
	String[] giveNIDs() {
		synchronized (getTileMap()) {
			String[] result = new String[getTileMap().size()];
			
			int nid = 0;
			for (T tile : getTiles()) {
				tile.setNid(nid);
				result[nid] = tile.getId();
			}
			
			return result;
		}
	}

	public T getFallbackTile() {
		return fallbackTile;
	}
	
	public void setFallbackTile(T fallbackTile) {
		this.fallbackTile = fallbackTile;
	}

}
