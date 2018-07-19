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
import ru.windcorp.crystalfarm.graphics.texture.ComplexTexture;
import ru.windcorp.crystalfarm.struct.mod.Mod;

public abstract class FullGridTile extends Tile {
	
	private ComplexTexture texture;
	private int x, y;

	public FullGridTile(Mod mod, String id, int... textureData) {
		super(mod, id);
		this.setTexture(getTextureForTile(this, textureData));
	}
	
	public FullGridTile(Mod mod, String id) {
		super(mod, id);
	}
	
	synchronized void adopt(FullGridTileLevel<?> level, int x, int y) {
		setLevel(level);
		this.x = x;
		this.y = y;
	}
	
	@Override
	public FullGridTileLevel<?> getLevel() {
		return (FullGridTileLevel<?>) super.getLevel();
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
	public void renderImpl(View view, int x, int y) {
		getTexture().render(x, y);
	}
	
	@Override
	protected FullGridTile clone() {
		FullGridTile clone = (FullGridTile) super.clone();
		clone.texture = clone.texture.clone();
		return clone;
	}
	
	@Override
	public double getViewX() {
		return (getX() - getSize()/2)*GameManager.TEXTURE_SIZE;
	}
	
	@Override
	public double getViewY() {
		return (getY() - getSize()/2)*GameManager.TEXTURE_SIZE;
	}

}
