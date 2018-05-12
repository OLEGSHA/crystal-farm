package ru.windcorp.tge2.util.selectors;

public abstract class AbstractSelectorOperator implements SelectorOperator {
	
	private final String[] names;

	public AbstractSelectorOperator(String[] names) {
		this.names = names;
	}

	@Override
	public boolean matchesName(String name) {
		for (String n : names) {
			if (n.equals(name)) {
				return true;
			}
		}
		
		return false;
	}

}
