package ru.windcorp.tge2.util.debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.IllegalFormatException;

import ru.windcorp.tge2.util.unixarg.UnixArgument;
import ru.windcorp.tge2.util.unixarg.UnixArgumentInvalidSyntaxException;

public class LogUnixArgument extends UnixArgument<String> {

	public LogUnixArgument() {
		super("log", 'l', "change log file to FILE or disable it. FILE is format string, e.g. %1tY is replaced with current year", String.class, false, false, false);
	}

	@Override
	protected boolean runImpl(String arg) throws UnixArgumentInvalidSyntaxException, InvocationTargetException {
		if (arg == null) {
			Debug.setLogFile(null);
			return false;
		}
		
		try {
			arg = String.format(arg, Calendar.getInstance());
		} catch (IllegalFormatException e) {
			throw new UnixArgumentInvalidSyntaxException("Illegal format: " + e, e, this);
		}
		
		try {
			Debug.setLogFile(new LogFile(new File(arg)));
		} catch (FileNotFoundException | SecurityException e) {
			throw new InvocationTargetException(e);
		}
		
		return false;
	}

}
