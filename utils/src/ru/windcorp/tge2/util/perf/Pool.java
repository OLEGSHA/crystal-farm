package ru.windcorp.tge2.util.perf;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import ru.windcorp.tge2.util.HashComparator;

public class Pool<T> {
	
	private final SortedSet<T> allInstances =
			Collections.synchronizedSortedSet(new TreeSet<T>(new HashComparator()));
	private final SortedSet<T> freeInstances =
			Collections.synchronizedSortedSet(new TreeSet<T>(new HashComparator()));
	
	private final PoolHelper<T> helper;
	private final int maxCapacity;

	public Pool(int initCapacity, int maxCapacity, PoolHelper<T> poolHelper) {
		if (initCapacity > maxCapacity) {
			throw new IllegalArgumentException("initCapacity > maxCapacity (" + initCapacity + " > " + maxCapacity + ")");
		}
		if (initCapacity < 0) {
			throw new IllegalArgumentException("initCapacity < 0 (" + initCapacity + ")");
		}
		
		this.helper = poolHelper;
		this.maxCapacity = maxCapacity;
		
		for (int i = 0; i < initCapacity; ++i) {
			getFreeInstances().add(add());
		}
	}

	protected SortedSet<T> getAllInstances() {
		return allInstances;
	}

	protected SortedSet<T> getFreeInstances() {
		return freeInstances;
	}

	public PoolHelper<T> getHelper() {
		return helper;
	}
	
	public synchronized T acquire() {
		if (getFreeInstances().isEmpty()) {
			return add();
		}
		
		T result = getFreeInstances().first();
		getFreeInstances().remove(result);
		return result;
	}
	
	private T add() {
		T obj = getHelper().newInstance();
		if (getAllInstances().size() < maxCapacity) {
			getAllInstances().add(obj);
		}
		return obj;
	}

	public synchronized void release(T obj) {
		if (obj == null) {
			throw new NullPointerException(
					"Attempted to release a null object to pool " + this);
		}
		if (getFreeInstances().contains(obj)) {
			throw new IllegalArgumentException("Attempted to release a free object to pool " + this);
		}
		if (getAllInstances().contains(obj)) {
			return;
		}
		getFreeInstances().add(obj);
		getHelper().reset(obj);
	}

}
