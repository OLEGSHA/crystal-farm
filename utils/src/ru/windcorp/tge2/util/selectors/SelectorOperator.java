package ru.windcorp.tge2.util.selectors;

import java.util.Stack;

import ru.windcorp.tge2.util.interfaces.Filter;

public interface SelectorOperator {
	
	public <T> void process(Stack<Filter<? super T>> stack);
	
	public boolean matchesName(String name);

}
