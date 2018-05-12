package ru.windcorp.tge2.util.commands;

public class CommandDefaultHelp extends HelpCommand {

	public CommandDefaultHelp() {
		super(new String[] {
				"?",
				"help",
				"commands"
		}, "Lists commands");
	}

	@Override
	public void showHelp(CommandRegistry registry) {
		write("Listing commands");
		for (Command cmd : registry.getCommands()) {
			write("  " + cmd.getSyntax() + " - " + cmd.getDescription());
		}
	}

}
