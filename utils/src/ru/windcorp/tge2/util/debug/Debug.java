package ru.windcorp.tge2.util.debug;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import ru.windcorp.tge2.util.NumberUtil;
import ru.windcorp.tge2.util.StringUtil;
import ru.windcorp.tge2.util.arg.Argument;

public class Debug {
	
	public static enum MessageType {
		INFO,		// Information of general importance
		REPORT,		// Information related to a report of any kind
		DEBUG,		// Information that should only concern programmers or system administrators
		WARNING,	// Information that requires user's attention, may or may not related to an error
		ERROR,		// Information related to an error of any importance
		CRITICAL;	// Information related to a critical error that will cause the application or its module to cease activity
		
		private static final int NAME_MAX_LENGTH;
		
		static {
			int tmp = 0;
			
			for (MessageType mt : values()) {
				if (mt.name().length() > tmp) tmp = mt.name().length();
			}
			
			NAME_MAX_LENGTH = tmp;
			
			for (MessageType mt : values()) mt.initEqualLengthString();
		}
		
		private String equalLengthString;
		
		@Override
		public String toString() {
			return name();
		}
		
		public String toUserfriendlyString() {
			return toString().toLowerCase().replace('_', ' ');
		}
		
		public String toEqualLengthString() {
			return equalLengthString;
		}
		
		private void initEqualLengthString() {
			char[] chars = new char[NAME_MAX_LENGTH];
			int i = 0;
			for (; i < name().length(); ++i) {
				chars[i] = name().charAt(i);
			}
			for (; i < chars.length; ++i) {
				chars[i] = ' ';
			}
			
			equalLengthString = new String(chars);
		}
	}
	
	public static final String FORMAT_SHORT = "%1$tH:%1$tM.%1$tS [%2$s] %4$s\n";
	public static final String FORMAT_SHORTEST = "%2$s: %4$s\n";
	public static final String FORMAT_LONG = "%1$td.%1$tm.%1$tY %1$tH:%1$tM:%1$tS [%2$s] %4$s\n";
	public static final String FORMAT_DEBUG = "%1$td.%1$tm.%1$tY %1$tH:%1$tM:%1$tS.%1$tL [%2$s] (%3$s)   %4$s\n";
	
	public static boolean allowDebug = false;
	private static LogFile logFile = null;
	public static String format = FORMAT_LONG;
	private static PrintStream output = null;
	
	private static PrintStream debugPrintStream = new PrintStream(new DebugOutputStream());
	
	public static PrintStream getOutput() {
		return output == null ? System.out : output;
	}

	public static void setOutput(PrintStream output) {
		Debug.output = output;
	}

	public static void log(Object text, MessageType type) {
		if (type == MessageType.DEBUG && !allowDebug) return;
		
		getOutput().printf((allowDebug ? FORMAT_DEBUG : format),
				System.currentTimeMillis(), type.toEqualLengthString(), Thread.currentThread().getName(), text);
		
		if (getLogFile() != null) {
			getLogFile().getPrintStream().printf(format,
					System.currentTimeMillis(), type.toEqualLengthString(), Thread.currentThread().getName(), text);
		}
		
		/* Example:
		 * 23.05.2016 14:16 [INFO    ] Written commentary for Debug.log(Object text, MessageType type)
		 */
	}
	
	public static void debugObj(Object... params) {
		log(argsToStrings(params, 0), MessageType.DEBUG);
	}
	
	public static <T> void debugArray(T[] array) {
		log(StringUtil.arrayToString(array), MessageType.DEBUG);
	}
	
	public static Argument getDebugArgument() {
		return new Argument("Enables debug mode", null, false, "d") {

			@Override
			protected String implProcess(String declar) {
				setDebugMode(true);
				return null;
			}
			
		};
	}
	
	public static <T> T print(T obj) {
		debugRaw(obj);
		return obj;
	}
	
	public static StringBuilder argsToStrings(Object[] params, int methodsToSkip) {
		StringBuilder sb = new StringBuilder(getLastMethod(methodsToSkip + 1));
		
		sb.append(": ");
		
		if (params == null) {
			sb.append("[null]");
		} else if (params.length == 0) {
			sb.append("[empty]");
		} else {
			for (int i = 0; i < params.length; ++i) {
				sb.append("arg" + i + " = \"" + params[i] + "\"");
				if (i < params.length + 1) {
					sb.append("; ");
				}
			}
		}
		
		return sb;
	}
	
	public static void inform(Object message) {
		log(message, MessageType.INFO);
	}
	
	public static void report(Object message) {
		log(message, MessageType.REPORT);
	}
	
	public static void warn(Object message) {
		log(message, MessageType.WARNING);
	}
	
	public static void warnError(Object message) {
		log(message, MessageType.ERROR);
	}
	
	public static void warnCritical(Object message) {
		log(message, MessageType.CRITICAL);
	}
	
	public static void debugRaw(Object message) {
		if (message instanceof byte[]) {
			message = NumberUtil.toUnsignedHexString((byte[]) message);
		}
		
		log(message, MessageType.DEBUG);
	}
	
	public static void traceDebug(Object message) {
		log(getLastMethod(0) + ' ' + message.toString(), MessageType.DEBUG);
	}
	
	public static void entry(Object... params) {
		log("Entered method " + getLastMethod(0) + " with parameters " + StringUtil.arrayToString(params), MessageType.DEBUG);
	}
	
	public static <T> T exit(T result) {
		log("Exiting method " + getLastMethod(0) + " with result " + result.toString(), MessageType.DEBUG);
		return result;
	}
	
	public static void trace() {
		log("Running method " + getLastMethod(0), MessageType.DEBUG);
	}
	
	public static String getLastMethod(int skip) {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		
		StackTraceElement element = trace[skip + 3];
		/*
		 * Stack trace:
		 * (0) Thread.getStackTrace()
		 * (1) Debug.getLastMethod(int)
		 * (2) * caller *
		 * (3) * target *
		 */
		
		if (element.isNativeMethod()) {
			return String.format("%s.%s (nat)", element.getClassName(), element.getMethodName());
		}
		
		return String.format("%s.%s (%d)", element.getClassName(), element.getMethodName(), element.getLineNumber());
	}

	public static LogFile getLogFile() {
		return logFile;
	}

	public static void setLogFile(LogFile logFile) {
		Debug.logFile = logFile;
	}
	
	public static void setDebugMode(boolean debug) {
		allowDebug = debug;
	}
	
	@Deprecated
	public static void setDebugMode() {
		setDebugMode(true);
	}
	
	public static PrintStream getDebugPrintStream() {
		return debugPrintStream;
	}
	
	public static <T> String getConstantName(Class<?> clazz, String prefix, Class<T> type, T value) {
		for (Field f : clazz.getFields()) {
			try {
				if ((f.getModifiers() & (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)) == (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)
						&& f.getType() == type
						&& f.getName().startsWith(prefix)) {
					
					Object fieldValue = f.get(null);
					
					if (type.isPrimitive() ?
							(
									(fieldValue == null && value == null) ||
									(fieldValue.equals(value))
							)
							: fieldValue == value) {
						return f.getName().substring(prefix.length());
					}
				}
			} catch (Exception e) {
				return null;
			}
		}
		
		return null;
	}

}
