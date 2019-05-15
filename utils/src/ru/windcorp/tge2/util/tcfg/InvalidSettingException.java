package ru.windcorp.tge2.util.tcfg;

import java.util.Stack;

public class InvalidSettingException extends CFGException {

	private static final long serialVersionUID = 2828365534939572805L;
	
	private static final ThreadLocal<Stack<String>> NOW_PARSING = ThreadLocal.withInitial(() -> new Stack<>());
	
	public static void startParsing(String setting) {
		NOW_PARSING.get().push(setting);
	}
	
	public static String endParsing() {
		return NOW_PARSING.get().pop();
	}
	
	private static String getPrefix() {
		return "Could not parse \"" + endParsing() + "\"";
	}

	public InvalidSettingException() {
		super(getPrefix());
	}

	public InvalidSettingException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(getPrefix() + ": " + message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidSettingException(String message, Throwable cause) {
		super(getPrefix() + ": " + message, cause);
	}

	public InvalidSettingException(String message) {
		super(getPrefix() + ": " + message);
	}

	public InvalidSettingException(Throwable cause) {
		super(getPrefix(), cause);
	}

}
