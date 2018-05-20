package ru.windcorp.tge2.util.debug;

import ru.windcorp.tge2.util.unixarg.UnixArgument;
import ru.windcorp.tge2.util.unixarg.UnixArgumentInvalidSyntaxException;

public class DebugUnixArgument extends UnixArgument<Object> {

	public DebugUnixArgument() {
		super("debug", 'd', "enables debug mode", null, false, false, false);
	}

	@Override
	protected boolean runImpl(Object arg) throws UnixArgumentInvalidSyntaxException {
		Debug.setDebugMode(true);
		return false;
	}

}
