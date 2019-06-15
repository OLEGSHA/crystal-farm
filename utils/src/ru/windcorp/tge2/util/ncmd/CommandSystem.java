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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class CommandSystem {
	
	private final Context context;
	private final CommandRegistry root = new CommandRegistry("ROOT", "Root command");
	
	public CommandSystem(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public CommandRegistry getRoot() {
		return root;
	}
	
	public void addRootCommands(Command... cmds) {
		synchronized (getRoot()) {
			for (Command cmd : cmds) {
				getRoot().add(cmd);
			}
		}
	}
	
	public boolean runCommand(CommandRunner runner, String rawInput) {
		rawInput = rawInput.trim();
		Invocation inv = new Invocation(getContext(), rawInput, runner, null, getRoot(), rawInput);
		try {
			getRoot().run(inv);
			return true;
		} catch (CommandExceptions e) {
			getContext().handle(e);
		} catch (Exception e) {
			getContext().getFallbackExceptionHandler().accept(inv, e);
		}
		return false;
	}
	
	public void runCommands(CommandRunner runner, Supplier<String> input) {
		String cmd;
		while ((cmd = input.get()) != null) {
			runCommand(runner, cmd);
		}
	}
	
	public void runCommandsInParallel(
			CommandRunner runner,
			Supplier<String> input,
			AtomicBoolean killSwitch, String name, boolean daemon) {
		
		Thread thread = new Thread(() -> {
			String cmd;
			while (!killSwitch.get() && (cmd = input.get()) != null) {
				runCommand(runner, cmd);
			}
		}, name);
		
		thread.setDaemon(daemon);
		thread.start();
	}

}
