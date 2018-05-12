package ru.windcorp.tge2.util.interfaces;

import java.util.ArrayList;
import java.util.List;

public class ComplexFilter<T> implements Filter<T> {
	
	public static final int TYPE_AND = 0,
			TYPE_OR = 1;
	
	private final int type;
	private final List<Filter<T>> subfilters = new ArrayList<Filter<T>>();

	public ComplexFilter(int type, Filter<T>... subfilters) {
		this.type = type;
		
		for (Filter<T> f : subfilters) {
			addSubfilter(f);
		}
	}

	@Override
	public boolean accept(T obj) {
		boolean pass = true;
		for (Filter<T> f : getSubfilters()) {
			pass = processType(f.accept(obj), pass);
		}
		return pass;
	}

	protected boolean processType(boolean accept, boolean pass) {
		switch (getType()) {
		case TYPE_AND: return accept && pass;
		case TYPE_OR: return accept || pass;
		}
		
		return false;
	}

	public int getType() {
		return type;
	}

	public List<Filter<T>> getSubfilters() {
		return subfilters;
	}
	
	public void addSubfilter(Filter<T> filter) {
		getSubfilters().add(filter);
	}
	
	public boolean removeSubfilter(Filter<T> filter) {
		return getSubfilters().remove(filter);
	}

}
