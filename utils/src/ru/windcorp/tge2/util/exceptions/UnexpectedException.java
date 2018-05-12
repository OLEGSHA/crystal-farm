package ru.windcorp.tge2.util.exceptions;

/**
 * Signals an abnormal situation during runtime, such as a CloneNotSupported exception while operating
 * with objects that definitely support cloning.
 * */
public class UnexpectedException extends RuntimeException {
	
	private static final long serialVersionUID = 6269691474908317023L;

	public UnexpectedException() {}

	public UnexpectedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnexpectedException(String message) {
		super(message);
	}

	public UnexpectedException(Throwable cause) {
		super(cause);
	}
	
}
