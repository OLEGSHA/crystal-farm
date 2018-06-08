package ru.windcorp.tge2.util.textui;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.windcorp.tge2.util.debug.ConsoleIO;
import ru.windcorp.tge2.util.synch.SynchUtil;

public class TextUI {
	
	@SafeVarargs
	public static <T> T select(String query, T... options) {
		ConsoleIO.write(query);
		ConsoleIO.write("");
		for (int i = 0; i < options.length; ++i) {
			ConsoleIO.write((i + 1) + ".  " + options[i]);
		}
		ConsoleIO.write("");
		
		String awnser;
		int index; // 1 .. options.length (inclusive)
		while (true) {
			awnser = ConsoleIO.readLine();
			
			try {
				index = Integer.parseInt(awnser);
				
				if (index < 1 || index > options.length) {
					continue;
				}
				
				ConsoleIO.write("-> " + options[index - 1]);
				return options[index - 1];
			} catch (NumberFormatException e) {
				continue;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T select(String query, Collection<T> options) {
		return (T) select(query, options.toArray());
	}
	
	public static boolean ask(String question, String yes, String no) {
		ConsoleIO.write(question);
		ConsoleIO.write("");
		ConsoleIO.write("Y - " + yes);
		ConsoleIO.write("N - " + no);
		ConsoleIO.write("");
		
		String awnser;
		while (true) {
			awnser = ConsoleIO.readLine().toUpperCase();
			if (awnser.equals("Y")) {
				ConsoleIO.write("-> " + yes);
				return true;
			} else if (awnser.equals("N")) {
				ConsoleIO.write("-> " + no);
				return false;
			} else {
				continue;
			}
		}
	}
	
	public static String prompt(String prompt) {
		ConsoleIO.write(prompt);
		return ConsoleIO.readLine();
	}
	
	public static void thickLine() {
		ConsoleIO.write("=================================================");
	}
	
	public static void thinLine() {
		ConsoleIO.write("-------------------------------------------------");
	}
	
	private static class Confirmer extends Thread {
		AtomicBoolean complete = new AtomicBoolean(false);
		AtomicBoolean run = new AtomicBoolean(true);
		
		Confirmer() {
			super("TextUI Confirmer");
		}

		@Override
		public void run() {
			try {
				while (System.in.available() == 0 && run.get()) {
					Thread.sleep(200);
				}
				
				System.in.read();
				complete.set(true);
			} catch (IOException e) {
				// Ignore
			} catch (InterruptedException e) {
				// Ignore - we've timed out
			}
		}
		
		
	}
	
	private static class ConfirmerInterrupter extends Thread {
		Confirmer prompter;
		long milliseconds;
		
		ConfirmerInterrupter(long ms, Confirmer prompter) {
			super("TextUI Confirmer Interrupter");
			this.milliseconds = ms;
			this.prompter = prompter;
		}
		
		@Override
		public void run() {
			SynchUtil.pause(milliseconds);
			if (!prompter.complete.get()) {
				prompter.interrupt();
			}
		}
		
	}
	
	public static boolean confirm(String prompt, String pressEnter, long timeout) {

		if (prompt != null) ConsoleIO.write(prompt);
		ConsoleIO.write(pressEnter == null ? "Press ENTER to continue..." : pressEnter);
		
		Confirmer prompter = new Confirmer();
		ConfirmerInterrupter interrupter = new ConfirmerInterrupter(timeout, prompter);
		
		prompter.start();
		new Thread(interrupter).start();
		
		try {
			prompter.join();
		} catch (InterruptedException e) {
			// Ignore
		}
		
		return prompter.complete.get();
		
	}
	
	public static void notify(String notification, String pressEnter) {
		ConsoleIO.write(notification);
		ConsoleIO.write(pressEnter == null ? "Press ENTER to continue..." : pressEnter);
		ConsoleIO.readLine();
	}
	
	public static Long readInteger(String prompt,
			String formattedNotANumber,
			boolean allowExit) {
		return readInteger(prompt, formattedNotANumber, allowExit, Long.MIN_VALUE, Long.MAX_VALUE,
				null, null);
	}

	public static Long readInteger(String prompt, String formattedNotANumber, boolean allowExit,
			long min, long max, String formattedTooLittle, String formattedTooMuch) {
		String line = null;
		long result;
		
		if (formattedNotANumber == null) {
			formattedNotANumber = "\"%s\" is not an integer";
		}
		
		if (formattedTooLittle == null) {
			formattedTooLittle = "%d is less than %d";
		}
		
		if (formattedTooMuch == null) {
			formattedTooMuch = "%d is greater than %d";
		}
		
		if (prompt != null) {
			ConsoleIO.write(prompt);
		}
		
		while (true) {
			try {
				
				line = ConsoleIO.readLine().trim();
				
				if (allowExit && line.isEmpty()) {
					return null;
				}
				
				boolean isNegative = false;
				if (line.startsWith("-")) {
					isNegative = true;
					line = line.substring("-".length());
				} else if (line.startsWith("+")) {
					line = line.substring("+".length());
				}
				
				int radix = 10;
				if (line.startsWith("0x")) {
					radix = 0x10;
					line = line.substring("0x".length());
				} else if (line.startsWith("0")) {
					radix = 010;
					line = line.substring("0".length());
				} else if (line.startsWith("b")) {
					radix = 2;
					line = line.substring("b".length());
				}
				
				result = Long.parseLong(line, radix);
				
				if (isNegative) {
					result = -result;
				}
				
				if (result < min) {
					ConsoleIO.write(String.format(formattedTooLittle, result, min));
					continue;
				}
				
				if (result > max) {
					ConsoleIO.write(String.format(formattedTooMuch, result, max));
					continue;
				}
				
				return result;
				
			} catch (NumberFormatException e) {
				ConsoleIO.write(String.format(formattedNotANumber, line));
			}
		}
	}
	
	public static Double readFPNumber(String prompt, String formattedNotANumber, boolean allowExit) {
		String line = null;
		
		if (formattedNotANumber == null) {
			formattedNotANumber = "\"%s\" is not a number";
		}
		
		if (prompt != null) {
			ConsoleIO.write(prompt);
		}
		
		while (true) {
			try {
				line = ConsoleIO.readLine().trim();
				if (allowExit && line.isEmpty()) {
					return null;
				}
				return Double.parseDouble(line);
			} catch (NumberFormatException e) {
				ConsoleIO.write(String.format(formattedNotANumber, line));
			}
		}
	}
}
