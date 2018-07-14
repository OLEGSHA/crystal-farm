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
package ru.windcorp.crystalfarm.content.basic.controls;

import java.io.DataInput;
import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.content.basic.entity.PlayerEntity;
import ru.windcorp.crystalfarm.logic.action.HoldableControlAction;
import ru.windcorp.crystalfarm.logic.server.Agent;
import ru.windcorp.crystalfarm.logic.server.PlayerAgent;
import ru.windcorp.crystalfarm.logic.server.PlayerProfile;

public class PlayerMoveControl extends HoldableControlAction {
	
	public static final int NORTH = 0;
	public static final int SOUTH = 1;
	public static final int EAST = 2;
	public static final int WEST = 3;
	
	private final int direction;

	public PlayerMoveControl(int direction) {
		super(InbuiltMod.INST, "PlayerMove" + getDirectionName(direction),
				"Move player " + getDirectionName(direction),
				"PRESS " + getDirectionKey(direction),
				false);
		this.direction = direction;
	}

	private static String getDirectionName(int direction) {
		switch (direction) {
		case NORTH: return "North";
		case SOUTH: return "South";
		case EAST: return "East";
		case WEST: return "West";
		default: throw new IllegalArgumentException("Unknown control direction: " + direction);
		}
	}
	
	private static String getDirectionKey(int direction) {
		switch (direction) {
		case NORTH: return "W";
		case SOUTH: return "S";
		case EAST: return "D";
		case WEST: return "A";
		default: throw new IllegalArgumentException("Unknown control direction: " + direction);
		}
	}

	@Override
	public void onPressed(Agent agent, DataInput input) {
		if (!(agent instanceof PlayerAgent)) {
			return;
		}
		
		PlayerProfile profile = ((PlayerAgent) agent).getProfile();
		if (profile == null) {
			return;
		}
		
		PlayerEntity entity = profile.getEntity();
		if (entity == null) {
			return;
		}
		
		switch (direction) {
		case NORTH:
			entity.changeWalkControlsY(-1);
			break;
		case SOUTH:
			entity.changeWalkControlsY(+1);
			break;
		case EAST:
			entity.changeWalkControlsX(+1);
			break;
		case WEST:
			entity.changeWalkControlsX(-1);
			break;
		default: throw new IllegalArgumentException("Unknown control direction: " + direction);
		}
	}
	
	@Override
	public void onReleased(Agent agent, DataInput input) {
		if (!(agent instanceof PlayerAgent)) {
			return;
		}
		
		PlayerProfile profile = ((PlayerAgent) agent).getProfile();
		if (profile == null) {
			return;
		}
		
		PlayerEntity entity = profile.getEntity();
		if (entity == null) {
			return;
		}
		
		switch (direction) {
		case NORTH:
			entity.changeWalkControlsY(+1);
			break;
		case SOUTH:
			entity.changeWalkControlsY(-1);
			break;
		case EAST:
			entity.changeWalkControlsX(-1);
			break;
		case WEST:
			entity.changeWalkControlsX(+1);
			break;
		default: throw new IllegalArgumentException("Unknown control direction: " + direction);
		}
	}

}
