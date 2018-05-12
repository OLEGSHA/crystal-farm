package ru.windcorp.tge2.util.selectors;

import java.util.Stack;

import ru.windcorp.tge2.util.interfaces.Filter;
import ru.windcorp.tge2.util.interfaces.Filters;

public class OperatorNot extends AbstractSelectorOperator {
	
	public OperatorNot(String... names) {
		super(names);
	}

	@Override
	public <T> void process(Stack<Filter<? super T>> stack) {
		stack.push(Filters.not(stack.pop()));
	}

}
