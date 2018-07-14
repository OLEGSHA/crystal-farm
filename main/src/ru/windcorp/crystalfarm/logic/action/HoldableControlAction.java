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
package ru.windcorp.crystalfarm.logic.action;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import ru.windcorp.crystalfarm.cfg.Setting;
import ru.windcorp.crystalfarm.client.Proxy;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.crystalfarm.input.KeyStroke;
import ru.windcorp.crystalfarm.logic.server.Agent;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public abstract class HoldableControlAction extends ControlAction {

	public HoldableControlAction(Mod mod, String name, boolean isLocal, Setting<KeyStroke> setting) {
		super(mod, name, isLocal, setting);
	}

	public HoldableControlAction(Mod mod, String name, String description, KeyStroke defaultKey, boolean isLocal) {
		super(mod, name, description, defaultKey, isLocal);
	}

	public HoldableControlAction(Mod mod, String name, String description, String key, boolean isLocal) {
		super(mod, name, description, key, isLocal);
	}
	
	@Override
	public boolean matches(KeyInput input) {
		if (input.isRepeated()) {
			return false;
		}
		
		return getKeyStroke().matches(input, false);
	}

	@Override
	public void run(Agent agent, DataInput input) throws IOException, SyntaxException {
		if (input.readBoolean()) {
			onPressed(agent, input);
		} else {
			onReleased(agent, input);
		}
	}

	public abstract void onPressed(Agent agent, DataInput input) throws IOException, SyntaxException;
	
	public abstract void onReleased(Agent agent, DataInput input) throws IOException, SyntaxException;

	@Override
	public void write(Proxy proxy, KeyInput param, DataOutput output) throws IOException {
		output.writeBoolean(param.isPressed());
	}

}
