package ru.windcorp.tge2.util.commands;

public class CommandFailedException extends RuntimeException {

	private static final long serialVersionUID = -2732492470011421037L;
	
	private final Command command;

	public CommandFailedException(Command command) {
		this.command = command;
	}

	public CommandFailedException(String arg0, Throwable arg1, boolean arg2, boolean arg3, Command command) {
		super(arg0, arg1, arg2, arg3);
		this.command = command;
	}

	public CommandFailedException(String arg0, Throwable arg1, Command command) {
		super(arg0, arg1);
		this.command = command;
	}

	public CommandFailedException(String arg0, Command command) {
		super(arg0);
		this.command = command;
	}

	public CommandFailedException(Throwable arg0, Command command) {
		super(arg0);
		this.command = command;
	}

	public Command getCommand() {
		return command;
	}

}
