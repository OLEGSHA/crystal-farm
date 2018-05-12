package ru.windcorp.tge2.util.commands;

import java.util.Arrays;
import java.util.List;

import ru.windcorp.tge2.util.StringUtil;
import ru.windcorp.tge2.util.iterators.RangeIterator;

public abstract class Command {

	private final String[] names;
	private final String description, syntax;
	
	public Command(String[] names, String description, String syntax) {
		this.names = names;
		this.description = description;
		this.syntax = syntax;
	}

	public String[] getNames() {
		return names;
	}

	public String getDescription() {
		return description;
	}

	public String getSyntax() {
		return syntax;
	}
	
	public void run(List<String> args, String alias, CommandRegistry registry) {
		try {
			execute(args, alias, registry);
		} catch (CommandFailedException e) {
			// do nothing
		} catch (Exception e) {
			fail("Command " + alias + " (" + getNames()[0] + ") has thrown an uncaught exception", e);
		}
	}
	
	protected abstract void execute(List<String> args, String alias, CommandRegistry registry);
	
	public static void write(String text) {
		CommandSystem.commandIO.write(text);
	}
	
	public static void writef(String format, Object... args) {
		write(String.format(format, args));
	}
	
	public static void writeError(String text) {
		CommandSystem.commandIO.writeError(text);
	}
	
	public static void writeErrorf(String format, Object... args) {
		writeError(String.format(format, args));
	}
	
	protected int parseInt(String arg) {
		try {
			return Integer.parseInt(arg);
		} catch (NumberFormatException e) {
			complain(arg, "an integer", e); // always throws
			return 0;
		}
	}
	
	protected void complain(String given, String expected, Exception e) {
		printSyntax();
		fail("\"" + given + "\" is not " + expected, e);
	}
	
	protected void fail(String reason, Exception e) {
		throw new CommandFailedException(reason, e, this);
	}
	
	protected void printSyntax() {
		write(getSyntax());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + Arrays.hashCode(names);
		result = prime * result + ((syntax == null) ? 0 : syntax.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Command other = (Command) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (!Arrays.equals(names, other.names))
			return false;
		if (syntax == null) {
			if (other.syntax != null)
				return false;
		} else if (!syntax.equals(other.syntax))
			return false;
		return true;
	}
	
	public boolean matches(String name) {
		for (String alias : getNames()) {
			if (alias.equalsIgnoreCase(name)) {
				return true;
			}
		}
		
		return false;
	}
	
	protected static String concat(List<String> args, int from, int length) {
		return StringUtil.iteratorToString(
				new RangeIterator<String>(args.iterator(), from, length < 0 ? args.size() : length),
				" ",
				"[null]",
				"",
				"[null args]"
				);
	}
}
