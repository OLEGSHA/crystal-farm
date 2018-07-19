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
package ru.windcorp.crystalfarm.content.basic.test;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.client.View;
import ru.windcorp.crystalfarm.graphics.texture.ComplexTexture;
import ru.windcorp.crystalfarm.logic.GridTile;
import ru.windcorp.crystalfarm.logic.Units;
import ru.windcorp.crystalfarm.translation.TString;

public class TestTreeTile extends GridTile {
	
	private final ComplexTexture texture;

	public TestTreeTile() {
		super(InbuiltMod.INST, "testTreeTile");
		this.texture = getTextureForTile(this);
		setName(TString.wrap("Test Tree Tile Name"));
		setCanCollide(true);
	}

	@Override
	public void renderImpl(View view) {
		texture.render(getTextureX(), getTextureY());
	}
	
	@Override
	public double getSize() {
		return 0.8 * Units.METERS;
	}

}
