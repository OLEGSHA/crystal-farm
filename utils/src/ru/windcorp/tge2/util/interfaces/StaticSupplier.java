package ru.windcorp.tge2.util.interfaces;

public class StaticSupplier<T> implements Supplier<T> {
	
	private final T object;

	public StaticSupplier(T object) {
		this.object = object;
	}

	@Override
	public T supply() {
		return object;
	}

}
