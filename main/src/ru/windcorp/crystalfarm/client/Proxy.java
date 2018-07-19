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
package ru.windcorp.crystalfarm.client;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import ru.windcorp.crystalfarm.logic.Island;
import ru.windcorp.crystalfarm.logic.Units;
import ru.windcorp.crystalfarm.logic.action.Action;
import ru.windcorp.crystalfarm.logic.server.Agent;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public abstract class Proxy {
	
	private final View view = new View(0, 0, 1);
	private Island island;
	
	private PipedInputStream buffer;
	private final DataInput loopbackInput;
	private final DataOutput loopbackOutput;
	
	public Proxy() {
		this.buffer = new PipedInputStream();
		this.loopbackInput = new DataInputStream(this.buffer);
		try {
			this.loopbackOutput = new DataOutputStream(new PipedOutputStream(this.buffer));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public View getView() {
		return view;
	}

	public Island getIsland() {
		return island;
	}
	
	public void setIsland(Island island) {
		this.island = island;
		getView().setIslandSize(getIsland().getSize() * Units.PX_PER_TILE);
		getView().update();
	}

	public abstract <T> void sendAction(Action<T> action, T param);
	
	protected synchronized <T> void runLocally(Agent agent, Action<T> action, T param) {
		try {
			action.write(this, param, loopbackOutput);
		} catch (IOException e) {
			ExecutionReport.reportError(e, null,
					"Failed to run action %s locally (param \"%s\"): IOException while writing to loopback stream", action.getName(), String.valueOf(param));
		}
		
		try {
			action.run(agent, loopbackInput);
		} catch (IOException | SyntaxException e) {
			ExecutionReport.reportError(e, null,
					"Failed to run action %s locally (param \"%s\"): exception while reading from loopback stream", action.getName(), String.valueOf(param));
			
			try {
				buffer.skip(buffer.available());
			} catch (IOException e1) {
				e1.addSuppressed(e);
				ExecutionReport.reportCriticalError(e1, null,
						"Failed to run action %s locally (param \"%s\"): exception while repairing loopback stream", action.getName(), String.valueOf(param));
			}
		}
	}
	
	public <T> void runLocally(Action<T> action, T param) {
		runLocally(null, action, param);
	}
	
}
