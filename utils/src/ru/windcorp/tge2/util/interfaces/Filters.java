package ru.windcorp.tge2.util.interfaces;

import java.util.ArrayList;
import java.util.Collection;

import ru.windcorp.tge2.util.StringUtil;

public class Filters {
	
	public static <T> Collection<T> filter(Collection<? extends T> source, Filter<? super T> filter) {
		Collection<T> collection = new ArrayList<T>();
		synchronized (source) {
			for (T element : source) {
				if (filter.accept(element)) {
					collection.add(element);
				}
			}
		}
		return collection;
	}
	
	public static <T> Filter<T> not(final Filter<T> source) {
		return new Filter<T>() {

			@Override
			public boolean accept(T obj) {
				return !source.accept(obj);
			}
			
			@Override
			public String toString() {
				return "!" + source;
			}
			
		};
	}
	
	@SafeVarargs
	public static <T> Filter<T> or(final Filter<? super T>... filters) {
		return new Filter<T>() {

			@Override
			public boolean accept(T obj) {
				for (Filter<? super T> filter : filters) {
					if (filter.accept(obj)) {
						return true;
					}
				}
				
				return false;
			}
			
			@Override
			public String toString() {
				return "(" + StringUtil.arrayToString(filters, " | ") + ")";
			}
			
		};
	}
	
	@SafeVarargs
	public static <T> Filter<T> and(final Filter<? super T>... filters) {
		return new Filter<T>() {

			@Override
			public boolean accept(T obj) {
				for (Filter<? super T> filter : filters) {
					if (!filter.accept(obj)) {
						return false;
					}
				}
				
				return true;
			}
			
			@Override
			public String toString() {
				return "(" + StringUtil.arrayToString(filters, " & ") + ")";
			}
			
		};
	}
	
	public static <T> Filter<T> xor(final Filter<? super T> f1, final Filter<? super T> f2) {
		return new Filter<T>() {

			@Override
			public boolean accept(T obj) {
				return f1.accept(obj) != f2.accept(obj);
			}
			
			@Override
			public String toString() {
				return "(" + f1 + " != " + f2 + ")";
			}
			
		};
	}

}
