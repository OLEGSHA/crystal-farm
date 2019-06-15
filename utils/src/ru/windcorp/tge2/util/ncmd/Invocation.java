/**
 * Copyright (C) 2019 OLEGSHA
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
package ru.windcorp.tge2.util.ncmd;

public class Invocation {
	
	private final Context context;
	private final String fullCommand;
	private final CommandRunner runner;
	
	private final CommandRegistry parent;
	private final Command current;
	private final String args;
	
	public Invocation(Context context, String fullCommand, CommandRunner runner, CommandRegistry parent,
			Command current, String args) {
		this.context = context;
		this.fullCommand = fullCommand;
		this.runner = runner;
		this.parent = parent;
		this.current = current;
		this.args = args;
	}

	public Invocation nextCall(CommandRegistry parent, Command current, String args) {
		return new Invocation(this.context, this.fullCommand, this.runner, parent, current, args);
	}

	public Context getContext() {
		return context;
	}

	public String getFullCommand() {
		return fullCommand;
	}

	public CommandRunner getRunner() {
		return runner;
	}

	public CommandRegistry getParent() {
		return parent;
	}

	public Command getCurrent() {
		return current;
	}

	public String getArgs() {
		return args;
	}

}
