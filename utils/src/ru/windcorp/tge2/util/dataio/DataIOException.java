package ru.windcorp.tge2.util.dataio;

import java.io.IOException;

public class DataIOException extends IOException {

	private static final long serialVersionUID = -2337229199281648184L;

	public DataIOException() {
		super();
	}

	public DataIOException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DataIOException(String arg0) {
		super(arg0);
	}

	public DataIOException(Throwable arg0) {
		super(arg0);
	}

}
