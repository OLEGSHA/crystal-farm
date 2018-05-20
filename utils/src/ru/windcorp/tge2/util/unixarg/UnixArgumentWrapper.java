package ru.windcorp.tge2.util.unixarg;

import ru.windcorp.tge2.util.arg.Argument;

public class UnixArgumentWrapper extends UnixArgument<String> {
	
	private final Argument src;

	public UnixArgumentWrapper(String name, Character letter, String description, boolean isArgumentRequired, Argument src) {
		super(name, letter, description, String.class, src.isRequired(), isArgumentRequired, true);
		this.src = src;
	}

	public Argument getSource() {
		return src;
	}

	@Override
	protected boolean runImpl(String arg) throws UnixArgumentInvalidSyntaxException {
		String problem = getSource().process(arg);
		
		if (problem != null) {
			throw new UnixArgumentInvalidSyntaxException(problem, this);
		}
		
		return false;
	}

}
