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
import java.io.IOException;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.logic.action.ControlAction;
import ru.windcorp.crystalfarm.logic.server.Agent;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public class TestCharControl extends ControlAction {

	public TestCharControl(String name, String key, int vx, int vy) {
		super(InbuiltMod.INST,
				"TestCharMove" + name,
				"Move " + name,
				"PRESS " + key, false);
	}

	@Override
	public void run(Agent agent, DataInput input) throws IOException, SyntaxException {
		// TODO Auto-generated method stub
		System.err.println("Called auto-generated method TestCharControl.TestCharControl");

	}

}