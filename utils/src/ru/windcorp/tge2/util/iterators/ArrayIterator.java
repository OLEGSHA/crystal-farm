package ru.windcorp.tge2.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator<E> implements Iterator<E> {
	
	private final E[] array;
	private int next;
	
	@SafeVarargs
	public ArrayIterator(E... array) {
		this.array = array;
	}

	@Override
	public boolean hasNext() {
		return next < array.length;
	}

	@Override
	public E next() {
		try {
			return array[next++];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new NoSuchElementException();
		}
	}

}
