package ru.windcorp.tge2.util.selectors;

import ru.windcorp.tge2.util.StringUtil;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public abstract class NamedParameterizedSelector<T> extends NamedSelector<T> {
	
	private final char separator;
	private String givenName;
	
	public NamedParameterizedSelector(char separator, String... names) {
		super(names);
		this.separator = separator;
	}
	
	@Override
	public Selector<T> derive(String name) throws SyntaxException {
		String[] parts = StringUtil.split(name, separator, 2);
		
		if (parts[1] == null) {
			return null;
		}
		
		if (!matchesName(parts[0])) {
			return null;
		}
		
		NamedParameterizedSelector<T> selector = derive_impl(parts[1]);
		selector.givenName = name;
		return selector;
	}

	protected abstract NamedParameterizedSelector<T> derive_impl(String param) throws SyntaxException;
	
	@Override
	public String toString() {
		return givenName;
	}

}
