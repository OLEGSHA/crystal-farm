package ru.windcorp.tge2.util.arg;

import java.util.Collection;
import java.util.LinkedList;

import ru.windcorp.tge2.util.debug.ConsoleIO;
import ru.windcorp.tge2.util.textui.TextUI;

public class UserInputArgument extends Argument {
	
	private final String stopWord;
	private final ArgumentParser parser;
	private final int unknownArgPolicy;
	private final String prefix;
	private final char separator;

	public UserInputArgument(String stopWord,
			ArgumentParser parser, int unknownArgPolicy, String prefix, char separator) {
		super("Reads additional arguments from standard input (stdin)",
				"[timeout in seconds]", false, new String[] {"userinput", "customargs"});
		this.stopWord = stopWord;
		this.parser = parser;
		this.unknownArgPolicy = unknownArgPolicy;
		this.prefix = prefix;
		this.separator = separator;
	}

	@Override
	protected String implProcess(String declar) {

		TextUI.thickLine();
		
		try {
			
			if (declar != null && !declar.isEmpty()) {
					
				try {
					if (!TextUI.confirm(null, "Press ENTER to input additional arguments",
							(long) (1000 * Double.parseDouble(declar)))) {
						ConsoleIO.write("User argument input skipped");
						return null;
					}
				} catch (NumberFormatException e) {
					return "\"" + declar + "\" is not a floating-point number";
				}
				
			}
			
			ConsoleIO.write("Input arguments line by line. Stop with " + getStopWord() + ". Empty lines are ignored.");
			
			Collection<String> newArgs = new LinkedList<String>();
			
			String next;
			while ((next = ConsoleIO.readLine()) != null && !getStopWord().equals(next)) {
				if (next.isEmpty()) continue;
				
				newArgs.add(next);
			}
			
			ConsoleIO.write("Processing " + newArgs.size() + " arguments...");
			
			getParser().parse(newArgs.iterator(), getUnknownArgPolicy(), getPrefix(), getSeparator());
			
			ConsoleIO.write("User input arguments complete");
			
			return null;
		} finally {
			TextUI.thickLine();
		}
	}

	public String getStopWord() {
		return stopWord;
	}

	public ArgumentParser getParser() {
		return parser;
	}

	public int getUnknownArgPolicy() {
		return unknownArgPolicy;
	}

	public String getPrefix() {
		return prefix;
	}

	public char getSeparator() {
		return separator;
	}

}