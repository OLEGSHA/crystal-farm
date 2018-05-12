package ru.windcorp.tge2.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RangeIterator<E> implements Iterator<E> {
	
	private final Iterator<E> parent;
	private final int from;
	private final int amount;
	
	private int nextIndex = 0;

	public RangeIterator(Iterator<E> iterator, int from, int amount) {
		this.parent = iterator;
		this.from = from;
		this.amount = amount < 0 ? Integer.MAX_VALUE : amount;
	}
	
	public RangeIterator(Iterator<E> iterator, int from) {
		this(iterator, from, -1);
	}

	@Override
	public boolean hasNext() {
		update();
		return nextIndex < from + amount && parent.hasNext();
	}

	@Override
	public E next() {
		update();
		if (nextIndex >= from + amount) {
			throw new NoSuchElementException("RangeIterator about to retrieve element " + nextIndex 
					+ " which exceeds upper boundary " + (from + amount));
		}
		
		E result = parent.next();
		nextIndex++;
		return result;
	}
	
	protected void update() {
		while (nextIndex < from && parent.hasNext()) {
			parent.next();
			nextIndex++;
		}
	}

}
