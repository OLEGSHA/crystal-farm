package ru.windcorp.tge2.util.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Reiterator<E> implements Iterable<E> {
	
	private class ReiteratorIterator implements Iterator<E> {
		
		int index = 0; 

		@Override
		public boolean hasNext() {
			synchronized (source) {
				if (index >= data.size()) {
					if (!source.hasNext()) {
						return false;
					} else {
						data.add(source.next());
					}
				}
				
				return true;
			}
		}

		@Override
		public E next() {
			E result;
			synchronized (source) {
				if (!hasNext()) throw new NoSuchElementException();
				result = data.get(index);
			}
			index++;
			return result;
		}

	}

	private final Iterator<E> source;
	private final ArrayList<E> data = new ArrayList<E>();
	
	public Reiterator(Iterator<E> source) {
		this.source = source;
	}

	@Override
	public Iterator<E> iterator() {
		return new ReiteratorIterator();
	}

}
