package ru.windcorp.tge2.util.arg;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import ru.windcorp.tge2.util.StringUtil;
import ru.windcorp.tge2.util.iterators.ArrayIterator;

public class ArgumentParser {
	
	public static final int UNKNOWN_ARG_POLICY_IGNORE = 0;
	public static final int UNKNOWN_ARG_POLICY_WARN = 1;
	public static final int UNKNOWN_ARG_POLICY_TERMINATE = 2;

	private final Map<String, Argument> argumentMap = Collections.synchronizedMap(new HashMap<String, Argument>());
	private final Set<Argument> internalArgumentSet = Collections.synchronizedSet(new HashSet<Argument>());
	private final Set<Argument> argumentSet = Collections.unmodifiableSet(internalArgumentSet);
	
	protected Map<String, Argument> getArgumentMap() {
		return argumentMap;
	}
	
	protected Set<Argument> getModifiableArgumentSet() {
		return internalArgumentSet;
	}
	
	public Set<Argument> getArguments() {
		return argumentSet;
	}
	
	public void register(Argument arg) {
		getModifiableArgumentSet().add(arg);
		for (String name : arg.getNames()) {
			getArgumentMap().put(name, arg);
		}
	}
	
	public Collection<String> parse(String[] args, int unknownArgumentPolicy, String prefix, char separator) {
		return parse(new ArrayIterator<String>(args), unknownArgumentPolicy, prefix, separator);
	}
	
	/**
	 * Parses program arguments.
	 * @param args the arguments to parse
	 * @param unknownArgumentPolicy the action to perform in case an unknown argument is encountered
	 * @param prefix the prefix that all arguments to need to have to be parsed.
	 * All arguments that do not begin with prefix are ignored
	 * @param separator the character separating argument name and parameters
	 * @return argument declarations that have been processed
	 */
	public Collection<String> parse(Iterator<String> args, int unknownArgumentPolicy, String prefix, char separator) {
		if (args == null) {
			throw new IllegalArgumentException();
		}
		
		Collection<String> processed = new LinkedList<String>();
		
		String[] parts;
		String message;
		
		while (args.hasNext()) {
			String s = args.next();
			
			if (prefix != null) {
				if (s.startsWith(prefix)) {
					s = s.substring(prefix.length());
				} else {
					continue;
				}
			}
			
			if (s.equals("?") ||
					s.toLowerCase().equals("help")) {
				printUsage(prefix, separator);
				System.exit(0);
			}
			
			parts = StringUtil.split(s, separator, 2);
			
			if (getArgumentMap().containsKey(parts[0])) {
				Argument arg = getArgumentMap().get(parts[0]);
				
				if (parts.length == 1) {
					if (arg.getSyntax() != null) {
						System.out.println("Usage: " + arg.toString(separator));
						System.exit(1);
					}
				}
				
				try {
					if ((message = arg.process(parts[1])) != null) {
						System.out.println(message);
						System.out.println("Usage: " + arg.toString(separator));
						System.exit(1);
					}
				} catch (Exception e) {
					System.out.println("Exception encountered while processing argument " + arg.toString(separator) + ": " + e);
					e.printStackTrace(System.out);
					System.exit(1);
				}
				
				processed.add(s);
			} else {
				if ((unknownArgumentPolicy & UNKNOWN_ARG_POLICY_WARN) != 0) {
					System.out.println("Unknown argument " + s);
				}
				
				if ((unknownArgumentPolicy & UNKNOWN_ARG_POLICY_TERMINATE) != 0) {
					System.out.println("Unknown argument encountered. Application will terminate");
					printUsage(prefix, separator);
					System.exit(1);
				}
			}
		}
		
		for (Argument a : getArguments()) {
			if (a.isRequired() && !a.hasAppeared()) {
				System.out.println("One or more of the required arguments has not been encountered. "
						+ "Application will terminate");
				printUsage(prefix, separator);
				System.exit(1);
			}
			
			a.resetAppearance();
		}
		
		return processed;
	}
	
	public void printUsage(String prefix, char separator) {
		System.out.println("Arguments:");
		for (Argument arg : getArguments()) {
			System.out.println("  " + (prefix == null ? "" : prefix) +
					arg.toString(separator) + "  - " + arg.getDescription());
		}
	}
	
}
