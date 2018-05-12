package ru.windcorp.tge2.util.exceptions;

public class ImplementationMistakeException extends RuntimeException {

	private static final long serialVersionUID = -7537367528993520949L;

	public ImplementationMistakeException() {}

	protected ImplementationMistakeException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public ImplementationMistakeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ImplementationMistakeException(String arg0) {
		super(arg0);
	}

	public ImplementationMistakeException(Throwable arg0) {
		super(arg0);
	}

}
