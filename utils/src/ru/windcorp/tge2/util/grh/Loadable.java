package ru.windcorp.tge2.util.grh;

import java.io.IOException;

public interface Loadable<T> {
	
	public void load(T input) throws IOException;

}
