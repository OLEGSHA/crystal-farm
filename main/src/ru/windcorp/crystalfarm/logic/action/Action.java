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

import ru.windcorp.crystalfarm.logic.server.Agent;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.tge2.util.Nameable;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public abstract class Action extends Nameable {
	
	private final boolean isLocal;
	private boolean isEnabled = true;

	public Action(Mod mod, String name, boolean isLocal) {
		super(mod.getName() + ":" + name);
		this.isLocal = isLocal;
	}

	public boolean isLocal() {
		return isLocal;
	}
	
	public boolean isEnabled(Agent agent) {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public abstract void run(Agent agent, DataInput input) throws IOException, SyntaxException;
	
	public void run(Agent agent) {
		try {
			run(agent, null);
		} catch (IOException | SyntaxException e) {
			// Do nothing
		}
	}
	
	public void write(DataOutput output) throws IOException {
		// To be overridden
	}
	
}
