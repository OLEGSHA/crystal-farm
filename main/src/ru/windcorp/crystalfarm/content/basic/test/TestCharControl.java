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

import java.io.DataInput;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.logic.DynamicTile;
import ru.windcorp.crystalfarm.logic.DynamicTileLevel;
import ru.windcorp.crystalfarm.logic.action.HoldableControlAction;
import ru.windcorp.crystalfarm.logic.server.Agent;

public class TestCharControl extends HoldableControlAction {
	
	private final double vx, vy;

	public TestCharControl(String name, String key, double vx, double vy) {
		super(InbuiltMod.INST,
				"TestCharMove" + name,
				"Move " + name,
				"PRESS " + key, false);
		this.vx = vx;
		this.vy = vy;
	}

	@Override
	public void onPressed(Agent agent, DataInput input) {
		TestCharTile tile = getChar(agent);
		tile.setWalkSpeedX(vx);
		tile.setWalkSpeedY(vy);
	}

	@Override
	public void onReleased(Agent agent, DataInput input) {
		TestCharTile tile = getChar(agent);
		tile.setWalkSpeedX(0);
		tile.setWalkSpeedY(0);
	}
	
	@SuppressWarnings("unchecked")
	private TestCharTile getChar(Agent agent) {
		return (TestCharTile) ((DynamicTileLevel<DynamicTile>) agent.getIsland().getLevel("Inbuilt:TestDynLevel")).getTileByStatId(0);
	}

}
