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
package ru.windcorp.crystalfarm.content.basic.floor;

import ru.windcorp.crystalfarm.client.View;
import ru.windcorp.crystalfarm.graphics.texture.ComplexTexture;
import ru.windcorp.crystalfarm.logic.GridTile;
import ru.windcorp.crystalfarm.struct.mod.Mod;

public abstract class FloorTile extends GridTile {
	
	private ComplexTexture texture;
	
	public FloorTile(Mod mod, String id) {
		super(mod, id);
	}

	public FloorTile(Mod mod, String id, int... textureData) {
		this(mod, id);
		setTexture(getTextureForTile(this, textureData));
	}

	public ComplexTexture getTexture() {
		return texture;
	}

	public void setTexture(ComplexTexture texture) {
		this.texture = texture;
	}

	@Override
	protected void renderImpl(View view, int x, int y) {
		getTexture().render(x, y);
	}
	
	@Override
	public FloorLevel getLevel() {
		return (FloorLevel) super.getLevel();
	}
	
	@Override
	protected String getPrefix() {
		return super.getPrefix() + "floor.";
	}
	
	@Override
	protected FloorTile clone() {
		FloorTile clone = (FloorTile) super.clone();
		clone.texture = clone.texture.clone();
		return clone;
	}

}
