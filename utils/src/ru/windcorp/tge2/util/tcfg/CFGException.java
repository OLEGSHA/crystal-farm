package ru.windcorp.tge2.util.tcfg;

public class CFGException extends Exception {
	
	private static final long serialVersionUID = 9057100484476874366L;

	public CFGException() {
		super();
	}

	public CFGException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CFGException(String message, Throwable cause) {
		super(message, cause);
	}

	public CFGException(String message) {
		super(message);
	}

	public CFGException(Throwable cause) {
		super(cause);
	}
	
}
