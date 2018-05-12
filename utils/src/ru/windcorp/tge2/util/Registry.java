package ru.windcorp.tge2.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Registry<K, V> {
	
	private final Map<K, V> map = new HashMap<K, V>();

	public Map<K, V> getMap() {
		return map;
	}

	public boolean containsKey(K key) {
		return getMap().containsKey(key);
	}

	public boolean contains(V value) {
		return getMap().containsValue(value);
	}

	public V get(K key) {
		return getMap().get(key);
	}

	public V getOrDefault(K key, V defaultValue) {
		V value = getMap().get(key);
		return value == null ? defaultValue : value;
	}

	public void register(K key, V value) {
		if (!(containsKey(key) || contains(value))) {
			getMap().put(key, value);
		}
	}
	
	public void register(V value) {
		register(createKeyFor(value), value);
	}
	
	public Collection<V> getEntries() {
		return getMap().values();
	}
	
	public K createKeyFor(V value) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return getMap().toString();
	}

}
