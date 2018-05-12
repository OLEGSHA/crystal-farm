package ru.windcorp.tge2.util.selectors;

import java.util.Stack;

import ru.windcorp.tge2.util.interfaces.Filter;
import ru.windcorp.tge2.util.interfaces.Filters;

public class OperatorXor extends AbstractSelectorOperator {
	
	public OperatorXor(String... names) {
		super(names);
	}

	@Override
	public <T> void process(Stack<Filter<? super T>> stack) {
		Filter<? super T> arg2 = stack.pop();
		stack.push(Filters.xor(stack.pop(), arg2));
	}

}
