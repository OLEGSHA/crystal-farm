package ru.windcorp.tge2.util.eventsys;

public interface Listener<T> {
	
	/**
	 * Called whenever an appropriate event has occurred.
	 * @param obj - an object specifying the details of the event.
	 * */
	public void onEvent(T obj);

}
