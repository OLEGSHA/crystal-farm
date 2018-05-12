package ru.windcorp.tge2.util.grh;

import java.io.IOException;
import java.io.OutputStream;

public interface OutputSupplier {
	
	public OutputStream supply() throws IOException;

}
