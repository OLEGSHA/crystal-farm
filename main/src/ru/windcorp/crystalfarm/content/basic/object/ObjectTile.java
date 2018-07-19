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
package ru.windcorp.crystalfarm.content.basic.object;

import ru.windcorp.crystalfarm.client.View;
import ru.windcorp.crystalfarm.graphics.texture.ComplexTexture;
import ru.windcorp.crystalfarm.logic.Collideable;
import ru.windcorp.crystalfarm.logic.GridTile;
import ru.windcorp.crystalfarm.struct.mod.Mod;

public abstract class ObjectTile extends GridTile implements Collideable {
	
	private ComplexTexture texture;
	
	public ObjectTile(Mod mod, String id) {
		super(mod, id);
	}

	public ObjectTile(Mod mod, String id, int... textureData) {
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
	public double getMinX() {
		return getX();
	}
	
	@Override
	public double getMaxX() {
		return getY();
	}
	
	@Override
	public double getWidth() {
		return 1;
	}
	
	@Override
	public double getHeight() {
		return 1;
	}
	
	@Override
	public ObjectLevel getLevel() {
		return (ObjectLevel) super.getLevel();
	}
	
	@Override
	protected String getPrefix() {
		return super.getPrefix() + "object.";
	}
	
	@Override
	protected ObjectTile clone() {
		ObjectTile clone = (ObjectTile) super.clone();
		clone.texture = clone.texture.clone();
		return clone;
	}

}
