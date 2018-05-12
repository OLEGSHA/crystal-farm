package ru.windcorp.tge2.util.selectors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.interfaces.Filter;
import ru.windcorp.tge2.util.interfaces.Filters;
import ru.windcorp.tge2.util.iterators.PeekingIterator;

public class SelectorSystem<T> {
	
	public static final char EXPRESSION_OPEN = '(';
	public static final char EXPRESSION_CLOSE = ')';
	
	private final Collection<Selector<? super T>> selectors =
			Collections.synchronizedCollection(new ArrayList<Selector<? super T>>());
	
	private final Collection<SelectorOperator> operators =
			Collections.synchronizedCollection(new ArrayList<SelectorOperator>());
	
	private String stackPrefix = null;
	
	public Collection<Selector<? super T>> getSelectors() {
		return this.selectors;
	}
	
	public Collection<SelectorOperator> getSelectorOperators() {
		return this.operators;
	}
	
	public String getStackPrefix() {
		return stackPrefix;
	}

	public SelectorSystem<T> setStackPrefix(String stackPrefix) {
		this.stackPrefix = stackPrefix;
		return this;
	}

	public SelectorSystem<T> add(Selector<? super T> selector) {
		getSelectors().add(selector);
		return this;
	}
	
	public SelectorSystem<T> add(SelectorOperator operator) {
		getSelectorOperators().add(operator);
		return this;
	}

	public Filter<? super T> parse(Iterator<String> tokens) throws SyntaxException {
		PeekingIterator<String> peeker = new PeekingIterator<String>(tokens);
		
		if (getStackPrefix() != null && peeker.hasNext() && getStackPrefix().equals(peeker.peek())) {
			peeker.next();
			return parseStack(peeker);
		}
		
		Stack<Filter<? super T>> stack = new Stack<Filter<? super T>>();

		synchronized (getSelectorOperators()) {
			synchronized (getSelectors()) {
				
				while (peeker.hasNext()) {
					parseToken(stack, peeker);
				}
				
			}
		}
		
		return compress(stack);
	}
	
	private void parseToken(Stack<Filter<? super T>> stack, Iterator<String> tokens) throws SyntaxException {
		
		if (!tokens.hasNext()) {
			throw new SyntaxException("Not enough tokens");
		}
		String token = tokens.next();
		
		for (SelectorOperator operator : getSelectorOperators()) {
			if (operator.matchesName(token.toLowerCase())) {
				parseToken(stack, tokens);
				operator.process(stack);
				return;
			}
		}
		
		Selector<? super T> tmp;
		for (Selector<? super T> selector : getSelectors()) {
			if ((tmp = selector.derive(token)) != null) {
				stack.push(tmp);
				return;
			}
		}
		
		throw new SyntaxException("Unknown token \"" + token + "\"");
	}
	
	public Filter<? super T> parseStack(Iterator<String> tokens) throws SyntaxException {
		Stack<Filter<? super T>> stack = new Stack<Filter<? super T>>();
		
		Selector<? super T> tmp;
		String token;
		
		synchronized (getSelectorOperators()) {
			synchronized (getSelectors()) {
				
				tokenCycle:
				while (tokens.hasNext()) {
					token = tokens.next();
					
					for (SelectorOperator operator : getSelectorOperators()) {
						if (operator.matchesName(token.toLowerCase())) {
							operator.process(stack);
							continue tokenCycle;
						}
					}
					
					for (Selector<? super T> selector : getSelectors()) {
						if ((tmp = selector.derive(token)) != null) {
							stack.push(tmp);
							continue tokenCycle;
						}
					}
					
					throw new SyntaxException("Unknown token \"" + token + "\"");
					
				}
			}
		}
		
		return compress(stack);
	}
	
	@SuppressWarnings("unchecked")
	private Filter<? super T> compress(Stack<Filter<? super T>> stack) throws SyntaxException {
		if (stack.isEmpty()) {
			throw new SyntaxException("Stack is empty");
		}
		
		if (stack.size() == 1) {
			return stack.pop();
		}
		
		return Filters.or(stack.toArray(new Filter[0]));
	}

}
