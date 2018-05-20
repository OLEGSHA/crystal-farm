package ru.windcorp.tge2.util.unixarg;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

import ru.windcorp.tge2.util.Version;
import ru.windcorp.tge2.util.debug.ConsoleIO;
import ru.windcorp.tge2.util.textui.TUITable;

public class UnixArgumentSystem {
	
	public static interface UnknownArgumentPolicy {
		public static final UnknownArgumentPolicy
		IGNORE = new UnknownArgumentPolicy() {
			
			@Override
			public boolean onUnknownArgument(String name) {
				return false;
			}
			
		},
		WARN = new UnknownArgumentPolicy() {

			@Override
			public boolean onUnknownArgument(String name) {
				ConsoleIO.write("Unknown argument " + name);
				return false;
			}
			
		},
		TERMINATE = new UnknownArgumentPolicy() {
			
			@Override
			public boolean onUnknownArgument(String name) {
				ConsoleIO.write("Unknown argument " + name);
				return true;
			}
			
		};
		
		public boolean onUnknownArgument(String name);
	}
	
	public static interface InvalidSyntaxPolicy {
		public static final InvalidSyntaxPolicy
		IGNORE = new InvalidSyntaxPolicy() {
			
			@Override
			public boolean onInvalidSyntax(UnixArgument<?> arg, String description) {
				return false;
			}
			
		},
		WARN = new InvalidSyntaxPolicy() {

			@Override
			public boolean onInvalidSyntax(UnixArgument<?> arg, String description) {
				ConsoleIO.write("Invalid use of argument " + arg + ": " + description);
				return false;
			}
			
		},
		TERMINATE = new InvalidSyntaxPolicy() {
			
			@Override
			public boolean onInvalidSyntax(UnixArgument<?> arg, String description) {
				ConsoleIO.write("Invalid use of argument " + arg + ": " + description);
				return true;
			}
			
		};
		
		public boolean onInvalidSyntax(UnixArgument<?> arg, String description);
	}
	
	private final SortedSet<UnixArgument<?>> arguments = Collections.synchronizedSortedSet(new TreeSet<>());
	
	private String name;
	private String description;
	private String usage;
	private Version version;
	
	public UnixArgumentSystem(String name, String description, String usage, Version version) {
		
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
	 * @throws InvocationTargetException 
	 */
	public boolean run(String[] input,
			UnknownArgumentPolicy unknownArgumentPolicy,
			InvalidSyntaxPolicy invalidSyntaxPolicy,
			boolean skipInputWithoutDashes)
					throws InvocationTargetException {
		
		Queue<String> queue = new LinkedList<String>();
		
		for (String s : input) {
			queue.add(s);
		}
		
		return run(queue.iterator(), unknownArgumentPolicy, invalidSyntaxPolicy, skipInputWithoutDashes);
	}
	
	/**
	 * Parses and executes arguments.
	 * @param args the arguments to parse
	 * @return true if the application should terminate
	 * @throws InvocationTargetException 
	 */
	public synchronized boolean run(Iterator<String> input,
			UnknownArgumentPolicy unknownArgumentPolicy,
			InvalidSyntaxPolicy invalidSyntaxPolicy,
			boolean skipInputWithoutDashes)
					throws InvocationTargetException {
		
		if (!input.hasNext()) {
			if (checkRequiredArguments()) {
				ConsoleIO.write("One or more required arguments are missing, the program will terminate");
				return true;
			}
			
			return false;
		}
		
		do {
			
			String next = input.next();
			
			if (next.equals("--help") || next.equals("-h")) {
				showHelp();
				return true;
			}
			
			if (next.equals("--version") || next.equals("-v")) {
				showVersion();
				return true;
			}
			
			try {
				
				if (next.startsWith("--")) {
					if (parseLongArgument(next.substring("--".length()), input)) {
						return true;
					}
				} else if (next.startsWith("-")) {
					for (char c : next.substring("-".length()).toCharArray()) {
						if (parseShortArgument(c, input)) {
							return true;
						}
					}
				} else if (skipInputWithoutDashes) {
					continue;
				} else {
					throw new UnixArgumentUnknownException(next);
				}
				
			} catch (UnixArgumentInvalidSyntaxException e) {
				if (invalidSyntaxPolicy.onInvalidSyntax(e.getArgument(), e.getMessage())) {
					return true;
				}
			} catch (UnixArgumentUnknownException e) {
				if (unknownArgumentPolicy.onUnknownArgument(e.getMessage())) {
					return true;
				}
			}
			
		} while (input.hasNext());
		
		if (checkRequiredArguments()) {
			ConsoleIO.write("One or more required arguments are missing, the program will terminate");
			return true;
		}
		
		return false;
	}

	private boolean parseShortArgument(char c, Iterator<String> input)
			throws InvocationTargetException, UnixArgumentInvalidSyntaxException, UnixArgumentUnknownException {
		synchronized (getArguments()) {
			for (UnixArgument<?> a : getArguments()) {
				if (a.getLetter() != null && a.getLetter().charValue() == c) {
					
					if (a.canRun()) {
						return a.parseInputAndRun(input);
					} else {
						throw new UnixArgumentInvalidSyntaxException("Invalid use of argument " + a, a);
					}
					
				}
			}
		}
		
		throw new UnixArgumentUnknownException("-" + c);
	}

	private boolean parseLongArgument(String name, Iterator<String> input)
			throws InvocationTargetException, UnixArgumentInvalidSyntaxException, UnixArgumentUnknownException {
		synchronized (getArguments()) {
			for (UnixArgument<?> a : getArguments()) {
				if (name.equals(a.getName())) { // a.getName() can be null, then evaluates to false
					
					if (a.canRun()) {
						return a.parseInputAndRun(input);
					} else {
						throw new UnixArgumentInvalidSyntaxException("Invalid use of argument " + a, a);
					}
					
				}
			}
		}
		
		throw new UnixArgumentUnknownException("--" + name);
	}

	private boolean checkRequiredArguments() {
		synchronized (getArguments()) {
			for (UnixArgument<?> a : getArguments()) {
				if (a.isRequired() && !a.hasRun()) {
					return true;
				}
			}
		}
		
		return false;
	}

	private void showHelp() {
		ConsoleIO.write(getName());
		ConsoleIO.write(getDescription());
		if (getUsage() != null) {
			ConsoleIO.write("Usage: " + getUsage());
		}
		
		ConsoleIO.write("Options:");
		
		TUITable table = new TUITable(false, new Object[] {"", "", ""});
		
		for (UnixArgument<?> a : getArguments()) {
			table.addRow(a.getLetter() == null ? "" : "-" + a.getLetter(),
					a.getName() == null ? "" : "--" + a.getName(),
					a.getDescritpion());
		}
		
		ConsoleIO.write(table);
	}
	
	private void showVersion() {
		ConsoleIO.write(getName() + " version " + getVersion());
	}
	
	public void reset() {
		synchronized (getArguments()) {
			for (UnixArgument<?> a : getArguments()) {
				a.reset();
			}
		}
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
