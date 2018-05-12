package ru.windcorp.tge2.util.commands;

import java.util.Collection;
import java.util.List;

public class SubCommandRegistry extends Command {
	
	public static String subCommandSyntax = "%s <subcommand>";

	private final CommandRegistry registry = new CommandRegistry();
	
	public SubCommandRegistry(String[] names, String description, String syntax, Command... commands) {
		super(names, description, names[0]);
		for (Command cmd : commands) {
			getRegistry().register(cmd);
		}
	}
	
	public SubCommandRegistry(String[] names, String description, Command... commands) {
		this(names, description, String.format(subCommandSyntax, names[0]), commands);
	}

	@Override
	public void execute(List<String> args, String alias, CommandRegistry registry) {
		if (args.size() == 0) {
			CommandSystem.showHelp(getRegistry());
			return;
		}
		
		getRegistry().run(args.get(0), args.subList(1, args.size()));
	}

	public CommandRegistry getRegistry() {
		return registry;
	}

	public Collection<Command> getCommands() {
		return registry.getCommands();
	}

	public void register(Command cmd) {
		registry.register(cmd);
	}

	public Command findOwn(String name) {
		return registry.findOwn(name);
	}

	public Command find(String name) {
		return registry.find(name);
	}

}
