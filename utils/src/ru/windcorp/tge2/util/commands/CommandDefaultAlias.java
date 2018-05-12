package ru.windcorp.tge2.util.commands;

import java.util.List;

public class CommandDefaultAlias extends Command {

	public CommandDefaultAlias() {
		super(new String[] {"alias", "aliases", CommandSystem.GLOBAL_COMMAND_PREFIX + "alias"},
				"Lists command aliases", "alias <ALIAS>");
	}

	@Override
	protected void execute(List<String> args, String _, CommandRegistry registry) {
		if (args.size() == 0) {
			printSyntax();
			return;
		}
		
		String alias = args.get(0);
		
		Command cmd = registry.find(alias);
		
		if (cmd == null) {
			write("No command matching alias \"" + alias + "\" found");
			return;
		}
		
		write("Command " + cmd.getNames()[0] + " has the following aliases:");
		for (String name : cmd.getNames()) {
			write(" - " + name);
		}
	}

}
