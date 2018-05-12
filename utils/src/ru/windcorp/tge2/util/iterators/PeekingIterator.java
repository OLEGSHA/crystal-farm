package ru.windcorp.tge2.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PeekingIterator<E> implements Iterator<E> {
	
	private final Iterator<? extends E> source;
	private E next = null;
	
	public PeekingIterator(Iterator<? extends E> source) {
		this.source = source;
	}

	@Override
	public boolean hasNext() {
		return next != null || source.hasNext();
	}
	
	public E peek() {
		if (next == null) {
			if (source.hasNext()) {
				next = source.next();
			} else {
				throw new NoSuchElementException();
			}
		}
		
		return next;
	}

	@Override
	public E next() {
		E element = peek();
		next = null;
		return element;
	}

}
