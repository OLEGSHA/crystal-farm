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

import ru.windcorp.crystalfarm.client.View;
import ru.windcorp.crystalfarm.graphics.GraphicsInterface;
import ru.windcorp.crystalfarm.graphics.texture.SimpleTexture;
import ru.windcorp.crystalfarm.graphics.texture.Texture;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.translation.TString;
import ru.windcorp.crystalfarm.util.Direction;

public abstract class GridTile extends Tile {
	
	private Texture texture;
	private int x, y;

	public GridTile(Mod mod, String id, TString name) {
		super(mod, id, name);
		this.setTexture(SimpleTexture.get("tile/" + mod.getName() + "/" + id));
	}
	
	synchronized void adopt(GridTileLevel<?> level, int x, int y) {
		setLevel(level);
		this.x = x;
		this.y = y;
	}
	
	@Override
	public GridTileLevel<?> getLevel() {
		return (GridTileLevel<?>) super.getLevel();
	}

	public synchronized int getX() {
		return x;
	}

	public synchronized int getY() {
		return y;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	@Override
	public void render(View view, int x, int y) {
		GraphicsInterface.drawTexture(
				x, y,
				texture,
				0, 0,
				null, Direction.UP);
	}

}
