package ru.windcorp.tge2.util.selectors;

import ru.windcorp.tge2.util.exceptions.SyntaxException;

public abstract class NamedSelector<T> implements Selector<T> {
	
	private final String[] names;

	public NamedSelector(String... names) {
		this.names = names;
	}

	public boolean matchesName(String name) {
		for (String n : names) {
			if (n.equalsIgnoreCase(name)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public Selector<T> derive(String name) throws SyntaxException {
		return matchesName(name) ? this : null;
	}
	
	@Override
	public String toString() {
		return names[0];
	}

}
