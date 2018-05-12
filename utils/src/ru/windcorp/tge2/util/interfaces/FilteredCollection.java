package ru.windcorp.tge2.util.interfaces;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public class FilteredCollection<E> extends AbstractCollection<E> {
	
	private final Collection<E> source;
	private final Filter<? super E> filter;
	
	public FilteredCollection(Collection<E> source, Filter<? super E> filter) {
		this.source = source;
		this.filter = filter;
	}

	@Override
	public Iterator<E> iterator() {
		return new FilteredIterator<E>(getSource().iterator(), getFilter());
	}

	@Override
	public int size() {
		int size = 0;
		synchronized (getSource()) {
			for (E element : getSource()) {
				if (filter.accept(element)) {
					size++;
				}
			}
		}
		return size;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean contains(Object obj) {
		return getSource().contains(obj) && filter.accept((E) obj); // getSource().contains() can only return true if obj is E
	}

	public Collection<E> getSource() {
		return source;
	}

	public Filter<? super E> getFilter() {
		return filter;
	}

}
