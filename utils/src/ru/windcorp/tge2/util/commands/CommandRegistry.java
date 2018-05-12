package ru.windcorp.tge2.util.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommandRegistry {
	
	private final Collection<Command> commands = Collections.synchronizedCollection(new ArrayList<Command>(10));

	public Collection<Command> getCommands() {
		return commands;
	}
	
	public void register(Command cmd) {
		getCommands().add(cmd);
	}
	
	public Command findOwn(String name) {
		for (Command c : getCommands()) {
			if (c.matches(name)) {
				return c;
			}
		}
		
		return null;
	}
	
	public Command find(String name) {
		Command tmp = findOwn(name);
		if (tmp != null) return tmp;
		
		tmp = CommandSystem.GLOBAL_COMMANDS.getRegistry().findOwn(name);
		if (tmp != null) return tmp;
		
		if (CommandSystem.GLOBAL_COMMANDS.matches(name)) {
			return CommandSystem.GLOBAL_COMMANDS;
		}
		
		if (CommandSystem.helpCommand.matches(name)) {
			return CommandSystem.helpCommand;
		}
		
		return null;
	}
	
	public void run(String name, List<String> args) {
		Command cmd = find(name);
		if (cmd != null) {
			cmd.execute(args, name, this);
			return;
		}
		
		CommandSystem.commandIO.write(String.format(CommandSystem.commandNotFoundMsg, name));
		CommandSystem.showHelp(this);
	}

}
