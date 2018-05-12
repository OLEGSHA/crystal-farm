package ru.windcorp.tge2.util.perf;

public interface PoolHelper<T> {

	public T newInstance();
	
	public void reset(T obj);
	
}
