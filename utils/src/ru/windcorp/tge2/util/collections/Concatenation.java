package ru.windcorp.tge2.util.collections;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Concatenation<E> extends AbstractCollection<E> {
	
	private final Collection<Collection<? extends E>> collections;
	
	@SafeVarargs
	public Concatenation(Collection<? extends E>... collections) {
		this.collections = Collections.unmodifiableCollection(Arrays.asList(collections));
	}
	
	public Collection<Collection<? extends E>> getCollections() {
		return collections;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			
			private final Iterator<Collection<? extends E>> superIterator = getCollections().iterator();
			private Iterator<? extends E> iterator = superIterator.next().iterator();

			@Override
			public boolean hasNext() {
				while (!iterator.hasNext()) {
					if (superIterator.hasNext()) {
						iterator = superIterator.next().iterator();
					} else {
						return false;
					}
				}
				
				return true;
			}

			@Override
			public E next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				
				return iterator.next();
			}
		};
	}

	@Override
	public int size() {
		int size = 0;
		for (Collection<? extends E> collection : getCollections()) {
			size += collection.size();
		}
		return size;
	}

}
