package ru.windcorp.tge2.util.eventsys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Listens to events and re-dispatches them among other listeners.
 * The order of dispatch is not defined.
 * */
public class LoudspeakerListener<T> implements Listener<T> {
	
	protected final List<Listener<T>> listeners = Collections.synchronizedList(new ArrayList<Listener<T>>());

	@Override
	public void onEvent(T obj) {
		synchronized (listeners) {
			for (Listener<T> l : getListeners()) {
				l.onEvent(obj);
			}
		}
	}

	public List<Listener<T>> getListeners() {
		return listeners;
	}
	
	public void addListener(Listener<T> listener) {
		synchronized (listeners) {
			getListeners().add(listener);
		}
	}
	
	public boolean removeListener(Listener<T> listener) {
		synchronized (listeners) {
			return getListeners().remove(listener);
		}
	}

}
