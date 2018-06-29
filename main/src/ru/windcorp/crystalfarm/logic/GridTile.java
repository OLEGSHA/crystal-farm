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

import static ru.windcorp.crystalfarm.logic.GameManager.TEXTURE_SIZE;

import ru.windcorp.crystalfarm.client.View;
import ru.windcorp.crystalfarm.graphics.texture.ComplexTexture;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.translation.TString;

public abstract class GridTile extends Tile {
	
	private ComplexTexture texture;
	private int x, y;

	public GridTile(Mod mod, String id, TString name, int... textureData) {
		super(mod, id, name);
		this.setTexture(ComplexTexture.get("tile/" + mod.getName() + "/" + id, TEXTURE_SIZE, textureData));
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

	public ComplexTexture getTexture() {
		return texture;
	}

	public void setTexture(ComplexTexture texture) {
		this.texture = texture;
	}
	
	@Override
	public void render(View view, int x, int y) {
		getTexture().render(x, y);
	}
	
	@Override
	protected GridTile clone() {
		GridTile clone = (GridTile) super.clone();
		clone.texture = clone.texture.clone();
		return clone;
	}

}
