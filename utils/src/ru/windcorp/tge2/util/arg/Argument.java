package ru.windcorp.tge2.util.arg;

import ru.windcorp.tge2.util.StringUtil;

public abstract class Argument {
	
	private final String[] names;
	private final String description;
	private final String syntax;
	
	private final boolean isRequired;
	private boolean hasAppeared = false;
	
	public Argument(String description, String syntax, boolean isRequired, String... names) {
		if (description == null ||
				names == null ||
				names.length == 0) {
			throw new IllegalArgumentException();
		}
		
		this.names = names;
		this.description = description;
		this.syntax = syntax;
		this.isRequired = isRequired;
	}
	
	public Argument(String description, String syntax, String... names) {
		this(description, syntax, false, names);
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

	public boolean isRequired() {
		return isRequired;
	}

	/**
	 * Processes provided String as an argument parameter.
	 * This method is called even if no argument parameter is applicable, in which case {@code declar == null}.
	 * It is guaranteed that this method is called when the argument is passed.
	 * @param declar - parameter as a string, may be null or empty
	 * @return Error message or null
	 */
	protected abstract String implProcess(String declar);
	
	public String process(String declar) {
		hasAppeared = true;
		return implProcess(declar);
	}
	
	public void resetAppearance() {
		hasAppeared = false;
	}
	
	public boolean hasAppeared() {
		return hasAppeared;
	}
	
	public String toString(char separator) {
		return (getNames().length == 1 ?
				getNames()[0] :
				"<" + StringUtil.arrayToString(getNames(), " | ") + ">") +
				(getSyntax() == null ? "" : separator + getSyntax());
	}

}
