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
package ru.windcorp.crystalfarm.content.basic.entity;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import ru.windcorp.crystalfarm.client.View;
import ru.windcorp.crystalfarm.graphics.texture.ComplexTexture;
import ru.windcorp.crystalfarm.logic.DynamicCollideable;
import ru.windcorp.crystalfarm.logic.DynamicTile;
import ru.windcorp.crystalfarm.logic.Island;
import ru.windcorp.crystalfarm.logic.Level;
import ru.windcorp.crystalfarm.logic.server.World;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.translation.TString;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public abstract class EntityTile extends DynamicTile implements DynamicCollideable {
	
	public static final int CHANGE_BIT_VELOCITY = 1;

	private ComplexTexture texture;
	
	private double velocityX = 0, velocityY = 0;

	public EntityTile(Mod mod, String id, TString name) {
		super(mod, id, name);
		setTickable(true);
	}
	
	public EntityTile(Mod mod, String id, TString name, int... textureData) {
		this(mod, id, name);
		setTexture(getTextureForTile(this, textureData));
	}
	
	public ComplexTexture getTexture() {
		return texture;
	}
	
	public void setTextureState(int state) {
		getTexture().setState(state);
	}

	public void setTexture(ComplexTexture texture) {
		this.texture = texture;
	}

	@Override
	public double getMinX() {
		return getX() + getSize()/2;
	}

	@Override
	public double getMinY() {
		return getY() + getSize()/2;
	}

	@Override
	protected void renderImpl(View view, int x, int y) {
		getTexture().render(x, y);
	}
	
	@Override
	public synchronized void tick(World world, Island island, Level level, long length, long time) {
		move(
				getX() + getVelocityX() * length,
				getY() + getVelocityY() * length
				);
		
		setVelocityX(
				getVelocityX() - absMin(
						getVelocityX(),
						length * Math.copySign(getVelocityX(), getDeceleration())
						)
				);
		
		setVelocityY(
				getVelocityY() - absMin(
						getVelocityY(),
						length * Math.copySign(getVelocityY(), getDeceleration())
						)
				);
	}

	protected static double absMax(double a, double b) {
		return (Math.abs(a) > Math.abs(b)) ? a : b;
	}
	
	protected static double absMin(double a, double b) {
		return (Math.abs(a) < Math.abs(b)) ? a : b;
	}
	
	@Override
	protected EntityTile clone() {
		EntityTile clone = (EntityTile) super.clone();
		clone.texture = clone.texture.clone();
		clone.velocityX = 0;
		clone.velocityY = 0;
		return clone;
	}
	
	public double getDeceleration() {
		return 0.01;
	}

	@Override
	public synchronized void setXY(double x, double y) {
		setPosition(x, y);
	}
	
	public synchronized void move(double x, double y) {
		setPosition(x, y);
		
		Island island = getIsland();
		if (island != null) {
			for (Level l : island.getLevels()) {
				l.pushOutside(this);
			}
		}
	}

	public synchronized double getVelocityX() {
		return velocityX;
	}

	public synchronized void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
		setChangeBit(CHANGE_BIT_VELOCITY);
	}

	public synchronized double getVelocityY() {
		return velocityY;
	}

	public synchronized void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
		setChangeBit(CHANGE_BIT_VELOCITY);
	}
	
	@Override
	public void read(DataInput input, int change) throws IOException, SyntaxException {
		super.read(input, change);
		
		if (getChangeBit(change, CHANGE_BIT_VELOCITY)) {
			velocityX = input.readDouble();
			velocityY = input.readDouble();
		}
	}
	
	@Override
	public void write(DataOutput output, int change) throws IOException {
		super.write(output, change);
		
		if (getChangeBit(change, CHANGE_BIT_VELOCITY)) {
			output.writeDouble(velocityX);
			output.writeDouble(velocityY);
		}
	}

}
