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
import ru.windcorp.crystalfarm.logic.DynamicTile;
import ru.windcorp.crystalfarm.logic.Island;
import ru.windcorp.crystalfarm.logic.Level;
import ru.windcorp.crystalfarm.logic.server.World;
import ru.windcorp.crystalfarm.translation.TString;

public class TestCharTile extends DynamicTile {
	
	private final ComplexTexture texture;
	
	private double walkSpeedX, walkSpeedY, speedX, speedY, deceleration = 0.00001;

	public TestCharTile() {
		super(InbuiltMod.INST, "testCharTile", TString.wrap("Test Tile Name"));
		this.texture = getTextureForTile(this, 1, 1000);
		setTickable(true);
	}

	public ComplexTexture getTexture() {
		return texture;
	}

	@Override
	public void render(View view, int x, int y) {
		getTexture().render(x, y);
	}
	
	@Override
	public void tick(World world, Island island, Level level, long length, long time) {
		//setPosition((Math.sin(time / 10000.0) + 1) * 50 / 2, (Math.cos(time / 10000.0) + 1) * 50 / 2);
		speedX = absMax(walkSpeedX, speedX - absMin(speedX, deceleration*Math.signum(speedX)*length));
		speedY = absMax(walkSpeedY, speedY - absMin(speedY, deceleration*Math.signum(speedY)*length));
		setPosition(getX() + speedX*length,
				getY() + speedY*length);
	}
	
	public double getWalkSpeedX() {
		return walkSpeedX;
	}

	public void setWalkSpeedX(double walkSpeedX) {
		this.walkSpeedX = walkSpeedX;
	}

	public double getWalkSpeedY() {
		return walkSpeedY;
	}

	public void setWalkSpeedY(double walkSpeedY) {
		this.walkSpeedY = walkSpeedY;
	}

	public double getSpeedX() {
		return speedX;
	}

	public void setSpeedX(double speedX) {
		this.speedX = speedX;
	}

	public double getSpeedY() {
		return speedY;
	}

	public void setSpeedY(double speedY) {
		this.speedY = speedY;
	}

	private static double absMax(double a, double b) {
		return (Math.abs(a) > Math.abs(b)) ? a : b;
	}
	
	private static double absMin(double a, double b) {
		return (Math.abs(a) < Math.abs(b)) ? a : b;
	}

}
