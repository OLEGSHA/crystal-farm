package ru.windcorp.tge2.util.selectors;

import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.interfaces.Filter;

public interface Selector<T> extends Filter<T> {
	
	public Selector<T> derive(String name) throws SyntaxException;

}
