package ru.windcorp.tge2.util.perf;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ru.windcorp.tge2.util.interfaces.Generator;

public class LimitedCache<T, P> {
	
	private final Map<P, T> cache = Collections.synchronizedMap(new HashMap<P, T>());
	private final LinkedList<T> queue = new LinkedList<T>();
	private final int limit;
	private final Generator<P, T> generator;
	
	public LimitedCache(Generator<P, T> generator, int limit) {
		this.generator = generator;
		this.limit = limit;
	}
	
	public LimitedCache(Generator<P, T> generator, P[] precache, int limit) {
		this(generator, limit);
		
		for (P param : precache) {
			cache(param);
		}
	}
	
	public Map<P, T> getCache() {
		return cache;
	}

	public LinkedList<T> getQueue() {
		return queue;
	}

	public int getLimit() {
		return limit;
	}

	public Generator<P, T> getGenerator() {
		return generator;
	}

	public T cache(P param) {
		T value = getGenerator().generate(param);
		synchronized (getQueue()) {
			getCache().put(param, value);
			getQueue().addFirst(value);
			clean();
		}
		return value;
	}
	
	public T get(P param) {
		T value = getCache().get(param);
		if (value == null) {
			return cache(param);
		}
		
		synchronized (getQueue()) {
			bump(value);
		}
		return value;
	}
	
	public void clear() {
		synchronized (getQueue()) {
			getCache().clear();
			getQueue().clear();
		}
	}
	
	public void bump(T element) {
		getQueue().remove(element);
		getQueue().addFirst(element);
	}
	
	public void clean() {
		if (getQueue().size() > getLimit()) {
			getQueue().removeLast();
		}
	}
	
}
