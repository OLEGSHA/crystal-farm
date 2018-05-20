package ru.windcorp.tge2.util.unixarg;

public class UnixArgumentInvalidSyntaxException extends Exception {

	private static final long serialVersionUID = 5689716525982612158L;
	
	private final UnixArgument<?> argument;

	public UnixArgumentInvalidSyntaxException(String description, Throwable cause, UnixArgument<?> argument) {
		super(description, cause);
		this.argument = argument;
	}
	
	public UnixArgumentInvalidSyntaxException(String description, UnixArgument<?> argument) {
		this(description, null, argument);
	}

	public UnixArgument<?> getArgument() {
		return argument;
	}

}
