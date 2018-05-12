package ru.windcorp.tge2.util;

import java.util.Comparator;

public class HashComparator implements Comparator<Object> {
	
	public static final HashComparator INST = new HashComparator();

	@Override
	public int compare(Object arg0, Object arg1) {
		return System.identityHashCode(arg0) - System.identityHashCode(arg1);
	}

}
