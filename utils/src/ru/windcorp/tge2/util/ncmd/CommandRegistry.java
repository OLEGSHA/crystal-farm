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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;

public class CommandRegistry extends Command {
	
	public CommandRegistry(String[] names, String desc, Command... commands) {
		super(names, "", desc);
		for (Command cmd : commands) {
			add(cmd);
		}
	}
	
	public CommandRegistry(String name, String desc, Command... commands) {
		this(new String[] {name}, desc, commands);
	}

	private final Map<String, Command> commandMap = new HashMap<>();
	private final SortedSet<Command> commands = new TreeSet<>(Comparator.comparing(Command::getName));
	private final SortedSet<Command> commandsReadOnly = Collections.unmodifiableSortedSet(commands);
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.Command#run(ru.windcorp.tge2.util.ncmd.CommandRunner, java.lang.String, java.lang.String)
	 */
	@Override
	public synchronized void run(Invocation inv) throws CommandExceptions {
		String args = inv.getArgs();
		
		if (args.isEmpty()) {
			// TODO show help
			return;
		}
		
		String name = null;
		String subArgs;
		{
			char[] chars = args.toCharArray();
			StringBuilder sb = new StringBuilder();
			int i;
			
			for (i = 0; i < chars.length; ++i) {
				if (Character.isWhitespace(chars[i])) {
					name = sb.toString();
					while (Character.isWhitespace(chars[++i]));
					break;
				}
				sb.append(chars[i]);
			}
			
			if (name == null) {
				name = args;
				subArgs = "";
			} else {
				subArgs = String.valueOf(chars, i, chars.length - i);
			}
		}
		
		CommandRegistry globals = inv.getContext().getGlobals();
		if (globals != this) {
			synchronized (globals) {
				if (globals.contains(name)) {
					globals.run(this, inv, name, subArgs);
					return;
				}
			}
		}
		
		run(this, inv, name, subArgs);
	}
	
	protected synchronized void run(CommandRegistry parent, Invocation inv, String name, String args) throws CommandExceptions {
		Command subCmd = commandMap.get(name);
		
		if (subCmd == null) {
			throw new CommandNotFoundException(inv, name);
		}
		
		Supplier<? extends CommandExceptions> supplier = subCmd.canRun(inv);
		if (supplier != null) {
			throw supplier.get();
		}
		
		subCmd.run(inv.nextCall(parent, subCmd, args));
	}
	
	public synchronized void add(Command cmd) {
		if (cmd == this) {
			throw new IllegalArgumentException("Cannot add command registry " + this + " to itself: recursion not allowed");
		}
		
		if (!commands.add(cmd)) {
			throw new IllegalArgumentException("Command " + cmd + " already present in " + this);
		}
		
		String[] names = cmd.getNames();
		for (int i = 0; i < names.length; ++i) {
			Command aliasDuplicate = commandMap.put(names[i], cmd);
			if (aliasDuplicate != null) {
				
				commands.remove(cmd);
				for (int j = 0; j < i; ++j) {
					commandMap.remove(names[j]);
				}
				
				throw new IllegalArgumentException("Duplicate name " + names[i] + " for commands " + cmd + " and " + aliasDuplicate);
			}
		}
	}
	
	public synchronized boolean contains(String name) {
		return commandMap.containsKey(name);
	}
	
	public SortedSet<Command> getCommands() {
		return commandsReadOnly;
	}

}
