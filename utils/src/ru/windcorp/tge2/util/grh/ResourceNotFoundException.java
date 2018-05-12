package ru.windcorp.tge2.util.grh;

import java.io.IOException;

public class ResourceNotFoundException extends IOException {

	private static final long serialVersionUID = -1205371877768089173L;

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(String arg0) {
		super(arg0);
	}

	public ResourceNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public ResourceNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
