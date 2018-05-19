package ru.windcorp.tge2.util.unixarg;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

import ru.windcorp.tge2.util.Version;
import ru.windcorp.tge2.util.debug.ConsoleIO;
import ru.windcorp.tge2.util.textui.TUITable;

public class UnixArgumentSystem {
	
	public static enum UnknownArgumentPolicy {
		IGNORE,
		WARN,
		TERMINATE
	}
	
	public static enum InvalidSyntaxPolicy {
		IGNORE,
		WARN,
		TERMINATE
	}
	
	private final SortedSet<UnixArgument<?>> arguments = Collections.synchronizedSortedSet(new TreeSet<>());
	
	private UnknownArgumentPolicy unknownArgumentPolicy = UnknownArgumentPolicy.IGNORE;
	private InvalidSyntaxPolicy invalidSyntaxPolicy = InvalidSyntaxPolicy.TERMINATE;
	private boolean skipInputWithoutDashes = true;
	
	private String name;
	private String description;
	private String usage;
	private Version version;
	
	public UnixArgumentSystem(UnknownArgumentPolicy unknownArgumentPolicy, InvalidSyntaxPolicy invalidSyntaxPolicy,
			boolean skipInputWithoutDashes, String name, String description, String usage, Version version) {
		
		this.unknownArgumentPolicy = unknownArgumentPolicy;
		this.invalidSyntaxPolicy = invalidSyntaxPolicy;
		this.skipInputWithoutDashes = skipInputWithoutDashes;
		this.name = name;
		this.description = description;
		this.usage = usage;
		this.version = version;
	}

	public SortedSet<UnixArgument<?>> getArguments() {
		return arguments;
	}
	
	public synchronized void addArgument(UnixArgument<?> argument) {
		getArguments().add(argument);
	}

	public synchronized void removeArgument(UnixArgument<?> argument) {
		getArguments().remove(argument);
	}
	
	/**
	 * Parses and executes arguments.
	 * @param args the arguments to parse
	 * @return true if the application should terminate
	 */
	public boolean run(String[] input) {
		Queue<String> queue = new LinkedList<>();
		
		for (String s : input) {
			queue.add(s);
		}
		
		return run(queue);
	}
	
	/**
	 * Parses and executes arguments.
	 * @param args the arguments to parse
	 * @return true if the application should terminate
	 */
	public synchronized boolean run(Queue<String> input) {
		if (!input.isEmpty()) {
			if (input.peek().equals("--help")) {
				showHelp();
				return true;
			}
		}
		
		try {
			argLoop:
			while (!input.isEmpty()) {
				if (input.peek().startsWith("--")) {
					try {
						synchronized (getArguments()) {
							for (UnixArgument<?> arg : getArguments()) {
								if (arg.getName() != null && ("--" + arg.getName()).equals(input.peek())) {
									if (arg.canRun()) {
										input.poll();
										if (arg.parseInputAndRun(input)) {
											return true;
										}
										continue argLoop;
									} else if (onInvalidSyntax("Invalid use of argument " + arg)) {
										return true;
									}
								}
							}
							
							if (onUnknownArgument(input.poll())) {
								return true;
							}
						}
					} catch (UnixArgumentInvalidSyntaxException e) {
						if (onInvalidSyntax("Invalid use of argument " + e.getArgument())) {
							return true;
						}
					}
				} else if (input.peek().startsWith("-")) {
					charLoop:
					for (char c : input.poll().toCharArray()) {
						try {
							synchronized (getArguments()) {
								for (UnixArgument<?> arg : getArguments()) {
									if (arg.getLetter() != null && arg.getLetter().charValue() == c) {
										if (arg.canRun()) {
											if (arg.parseInputAndRun(input)) {
												return true;
											}
											continue charLoop;
										} else if (onInvalidSyntax("Invalid use of argument " + arg)) {
											return true;
										}
									}
								}
								
								if (onUnknownArgument("-" + c)) {
									return true;
								}
							}
						} catch (UnixArgumentInvalidSyntaxException e) {
							if (onInvalidSyntax("Invalid use of argument " + e.getArgument())) {
								return true;
							}
						}
					}
				} else if (!getSkipInputWithoutDashes()) {
					if (onUnknownArgument(input.poll())) {
						return true;
					}
				} else {
					input.poll();
				}
			}
		} catch (InvocationTargetException e) {
			ConsoleIO.write("Unexpected error while processing arguments, the application will terminate");
			e.printStackTrace();
			return true;
		}
		
		return false;
	}

	private boolean onUnknownArgument(String arg) {
		switch (getUnknownArgumentPolicy()) {
		case IGNORE:
			// Do nothing
			break;
		case TERMINATE:
			ConsoleIO.write("Unknown argument " + arg);
			ConsoleIO.write("The prorgam will terminate");
			return true;
		case WARN:
			ConsoleIO.write("Unknown argument " + arg);
			ConsoleIO.write("The prorgam will terminate");
			break;
		}
		
		return false;
	}

	private boolean onInvalidSyntax(String string) {
		switch (getInvalidSyntaxPolicy()) {
		case IGNORE:
			// Do nothing
			break;
		case TERMINATE:
			ConsoleIO.write(string);
			ConsoleIO.write("The prorgam will terminate");
			return true;
		case WARN:
			ConsoleIO.write(string);
			break;
		}
		
		return false;
	}

	private void showHelp() {
		ConsoleIO.write(getName());
		ConsoleIO.write(getDescription());
		ConsoleIO.write("Usage: " + getUsage());
		
		ConsoleIO.write("Options:");
		
		TUITable table = new TUITable(false, new Object[] {"", "", ""});
		
		for (UnixArgument<?> a : getArguments()) {
			table.addRow(a.getLetter() == null ? "" : "-" + a.getLetter(),
					a.getName() == null ? "" : "--" + a.getName(),
					a.getDescritpion());
		}
		
		ConsoleIO.write(table);
	}

	public UnknownArgumentPolicy getUnknownArgumentPolicy() {
		return unknownArgumentPolicy;
	}

	public void setUnknownArgumentPolicy(UnknownArgumentPolicy unknownArgumentPolicy) {
		this.unknownArgumentPolicy = unknownArgumentPolicy;
	}

	public InvalidSyntaxPolicy getInvalidSyntaxPolicy() {
		return invalidSyntaxPolicy;
	}

	public void setInvalidSyntaxPolicy(InvalidSyntaxPolicy invalidSyntaxPolicy) {
		this.invalidSyntaxPolicy = invalidSyntaxPolicy;
	}

	public boolean getSkipInputWithoutDashes() {
		return skipInputWithoutDashes;
	}

	public void setSkipInputWithoutDashes(boolean skipInputWithoutDashes) {
		this.skipInputWithoutDashes = skipInputWithoutDashes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}
	
}
