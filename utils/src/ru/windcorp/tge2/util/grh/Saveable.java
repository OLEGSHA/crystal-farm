package ru.windcorp.tge2.util.grh;

import java.io.IOException;

public interface Saveable<T> {

	public void save(T output) throws IOException;
	
}
