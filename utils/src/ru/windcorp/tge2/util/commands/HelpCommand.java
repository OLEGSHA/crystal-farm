package ru.windcorp.tge2.util.commands;

import java.util.List;

public abstract class HelpCommand extends Command {

	public HelpCommand(String[] names, String description) {
		super(names, description, names[0]);
	}

	@Override
	protected void execute(List<String> args, String alias, CommandRegistry registry) {
		showHelp(registry);
	}
	
	public abstract void showHelp(CommandRegistry registry);

}
