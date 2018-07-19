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

import ru.windcorp.crystalfarm.struct.mod.Mod;

public abstract class GridTile extends Tile {

	private int intX, intY;

	public GridTile(Mod mod, String id) {
		super(mod, id);
	}
	
	synchronized void adopt(GridTileLevel<?> level, int x, int y) {
		setLevel(level);
		
		super.setXY(x, y);
		this.intX = x;
		this.intY = y;
	}
	
	@Override
	public GridTileLevel<?> getLevel() {
		return (GridTileLevel<?>) super.getLevel();
	}

	public int getIntX() {
		return intX;
	}

	public int getIntY() {
		return intY;
	}
	
	@Override
	protected synchronized void setXY(double x, double y) {
		throw new UnsupportedOperationException("FullGridTile cannot be moved");
	}
	
	@Override
	protected GridTile clone() {
		GridTile clone = (GridTile) super.clone();
		return clone;
	}

}
