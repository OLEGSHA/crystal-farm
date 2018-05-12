package ru.windcorp.tge2.util.perf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ru.windcorp.tge2.util.interfaces.Generator;

public class Cache<T, P> {
	
	private final Map<P, T> cache = Collections.synchronizedMap(new HashMap<P, T>());
	private final Generator<P, T> generator;
	
	public Cache(Generator<P, T> generator) {
		this.generator = generator;
	}
	
	public Cache(Generator<P, T> generator, P[] precache) {
		this(generator);
		
		for (P param : precache) {
			cache(param);
		}
	}
	
	public Map<P, T> getCache() {
		return cache;
	}

	public Generator<P, T> getGenerator() {
		return generator;
	}

	public T cache(P param) {
		T value = getGenerator().generate(param);
		getCache().put(param, value);
		return value;
	}
	
	public T get(P param) {
		T value = getCache().get(param);
		if (value == null) {
			return cache(param);
		}
		return value;
	}
	
	public void clear() {
		getCache().clear();
	}
	
}
