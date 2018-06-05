package ru.windcorp.tge2.util.collections;

import java.util.AbstractList;
import java.util.List;

public class ReverseListView<E> extends AbstractList<E> {
	
	public final List<E> src;

	public ReverseListView(List<E> src) {
		this.src = src;
	}

	@Override
	public E get(int arg0) {
		return src.get(size() - arg0 - 1);
	}

	@Override
	public int size() {
		return src.size();
	}

}
