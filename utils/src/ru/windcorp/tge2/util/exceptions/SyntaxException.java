package ru.windcorp.tge2.util.exceptions;

public class SyntaxException extends Exception {

	private static final long serialVersionUID = -1235310978559967686L;

	public SyntaxException() {}

	protected SyntaxException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public SyntaxException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SyntaxException(String arg0) {
		super(arg0);
	}

	public SyntaxException(Throwable arg0) {
		super(arg0);
	}

}
