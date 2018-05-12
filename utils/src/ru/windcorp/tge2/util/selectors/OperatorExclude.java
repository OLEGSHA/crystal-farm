package ru.windcorp.tge2.util.selectors;

import java.util.Stack;

import ru.windcorp.tge2.util.interfaces.Filter;
import ru.windcorp.tge2.util.interfaces.Filters;

public class OperatorExclude extends AbstractSelectorOperator {
	
	public OperatorExclude(String... names) {
		super(names);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void process(Stack<Filter<? super T>> stack) {
		Filter<? super T> arg2 = stack.pop();
		stack.push(Filters.and(stack.pop(), Filters.not(arg2)));
	}

}
