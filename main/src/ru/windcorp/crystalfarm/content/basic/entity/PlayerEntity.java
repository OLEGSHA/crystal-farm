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

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.content.basic.items.ItemUser;
import ru.windcorp.crystalfarm.logic.Island;
import ru.windcorp.crystalfarm.logic.Level;
import ru.windcorp.crystalfarm.logic.Units;
import ru.windcorp.crystalfarm.logic.server.PlayerProfile;
import ru.windcorp.crystalfarm.logic.server.World;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public class PlayerEntity extends EntityTile implements ItemUser {
	
	public static final int CHANGE_BIT_PROFILE = 2;
	
	private String login;

	private static final double SQRT_2_HALVED = Math.sqrt(2) / 2;
	private static final double BASIC_PLAYER_SPEED = 5 * Units.METERS_PER_SECOND;
	private static final double PLAYER_ACCELERATION = 80 * Units.METERS_PER_SECOND_SQUARED;
	
	private double walkSpeed = 1;
	private int walkControlsX = 0;
	private int walkControlsY = 0;

	public PlayerEntity() {
		super(InbuiltMod.INST, "Player", null);
		setDefaultName();
	}
	
	public PlayerProfile getProfile() {
		return getWorld().getPlayerProfile(getLogin());
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
		setChangeBit(CHANGE_BIT_PROFILE);
	}
	
	public double getWalkSpeed() {
		return walkSpeed;
	}

	public void multWalkSpeed(double walkSpeed) {
		this.walkSpeed *= walkSpeed;
	}

	public int getWalkControlsX() {
		return walkControlsX;
	}

	public void changeWalkControlsX(int walkControlsXMod) {
		this.walkControlsX += walkControlsXMod;
	}

	public int getWalkControlsY() {
		return walkControlsY;
	}

	public void changeWalkControlsY(int walkControlsYMod) {
		this.walkControlsY += walkControlsYMod;
	}

	@Override
	public synchronized void tick(World world, Island island, Level level, long length, long time) {
		double walkSpeedModX, walkSpeedModY;
		double walkMaxSpeed = BASIC_PLAYER_SPEED * walkSpeed;
		double vX = getVelocityX(), vY = getVelocityY();
		
		if (walkControlsY == 0) {
			walkSpeedModX = walkControlsX;
			walkSpeedModY = 0;
		} else {
			if (walkControlsX == 0) {
				walkSpeedModX = 0;
				walkSpeedModY = walkControlsY;
			} else {
				walkSpeedModX = SQRT_2_HALVED * walkControlsX;
				walkSpeedModY = SQRT_2_HALVED * walkControlsY;
			}
		}
		
		walkSpeedModX *= walkSpeed * PLAYER_ACCELERATION * length;
		walkSpeedModY *= walkSpeed * PLAYER_ACCELERATION * length;
		
		if (Math.abs(vX + walkSpeedModX) > walkMaxSpeed) {
			if (Math.abs(vX) > walkMaxSpeed) {
				walkSpeedModX = 0;
			} else {
				walkSpeedModX = Math.copySign(Math.copySign(walkMaxSpeed, vX) - vX, walkSpeedModX);
			}
		}
		
		if (Math.abs(vY + walkSpeedModY) > walkMaxSpeed) {
			if (Math.abs(vY) > walkMaxSpeed) {
				walkSpeedModY = 0;
			} else {
				walkSpeedModY = Math.copySign(Math.copySign(walkMaxSpeed, vY) - vY, walkSpeedModY);
			}
		}
		
		setVelocityX(vX + walkSpeedModX);
		setVelocityY(vY + walkSpeedModY);
		
		super.tick(world, island, level, length, time);
	}
	
	@Override
	public void read(DataInput input, int change) throws IOException, SyntaxException {
		super.read(input, change);
		
		if (getChangeBit(change, CHANGE_BIT_PROFILE)) {
			login = input.readUTF();
		}
	}
	
	@Override
	public void write(DataOutput output, int change) throws IOException {
		super.write(output, change);
		
		if (getChangeBit(change, CHANGE_BIT_PROFILE)) {
			output.writeUTF(login);
		}
	}

}
