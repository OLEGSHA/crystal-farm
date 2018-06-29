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
import ru.windcorp.crystalfarm.logic.GameManager;
import ru.windcorp.crystalfarm.logic.action.ControlAction;
import ru.windcorp.crystalfarm.logic.server.Agent;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public class TestCameraZoomControl extends ControlAction {
	
	private final double factor;

	public TestCameraZoomControl(String name, double factor, String key) {
		super(InbuiltMod.INST, "CameraZoom" + name, "Zoom camera " + name, key, true);
		this.factor = factor;
	}

	@Override
	public void run(Agent agent, DataInput input) throws IOException, SyntaxException {
		GameManager.getLocalClient().getView().zoom(factor);
	}

}
