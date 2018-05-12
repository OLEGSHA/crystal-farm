package ru.windcorp.tge2.util.selectors;

import ru.windcorp.tge2.util.interfaces.Filter;

public class FilterWrapper<T> extends NamedSelector<T> {
	
	private final Filter<? super T> filter;

	public FilterWrapper(String name, Filter<? super T> filter) {
		super(name);
		this.filter = filter;
	}

	@Override
	public boolean accept(T obj) {
		return filter.accept(obj);
	}

}
