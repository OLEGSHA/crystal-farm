package ru.windcorp.tge2.util.posixarg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

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
	
	public UnixArgumentSystem(UnknownArgumentPolicy unknownArgumentPolicy, InvalidSyntaxPolicy invalidSyntaxPolicy, boolean skipInputWithoutDashes) {
		this.unknownArgumentPolicy = unknownArgumentPolicy;
		this.invalidSyntaxPolicy = invalidSyntaxPolicy;
		this.skipInputWithoutDashes = skipInputWithoutDashes;
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

	public boolean isSkipInputWithoutDashes() {
		return skipInputWithoutDashes;
	}

	public void setSkipInputWithoutDashes(boolean skipInputWithoutDashes) {
		this.skipInputWithoutDashes = skipInputWithoutDashes;
	}
	
}
