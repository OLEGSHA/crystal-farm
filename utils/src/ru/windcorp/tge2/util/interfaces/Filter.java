package ru.windcorp.tge2.util.interfaces;

public interface Filter<T> {

	/**
	 * Returns true if the object is accepted; false otherwise.
	 * @param obj - the object to check
	 * @return true is the object passes the filter; false if it does not.
	 * */
	public boolean accept(T obj);
	
}
