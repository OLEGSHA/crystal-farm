package ru.windcorp.tge2.util.interfaces;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilteredIterator<E> implements Iterator<E> {
	
	private final Iterator<E> source;
	private final Filter<? super E> filter;
	
	private E next = null;
	
	public FilteredIterator(Iterator<E> source, Filter<? super E> filter) {
		this.source = source;
		this.filter = filter;
	}

	@Override
	public boolean hasNext() {
		while (next != null) {
			if (!getSource().hasNext()) {
				return false;
			}
			
			next = getSource().next();
			if (!getFilter().accept(next)) {
				next = null;
			}
		}
		
		return true;
	}

	@Override
	public E next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		
		return next;
	}

	public Filter<? super E> getFilter() {
		return filter;
	}

	public Iterator<E> getSource() {
		return source;
	}

}
