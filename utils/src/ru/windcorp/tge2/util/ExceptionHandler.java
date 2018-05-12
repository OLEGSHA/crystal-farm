package ru.windcorp.tge2.util;

import java.lang.Thread.UncaughtExceptionHandler;

import ru.windcorp.tge2.util.eventsys.Listener;
import ru.windcorp.tge2.util.eventsys.LoudspeakerListener;

public class ExceptionHandler {
	
	private static final LoudspeakerListener<Exception> DISPATCHER = new LoudspeakerListener<Exception>();
	
	public static void handle(Exception e, String desc) {
		dispatchException(e);
		System.out.println("Exception caught, provided description: " + desc + "\nPrinting stack trace...");
		e.printStackTrace(System.out);
	}
	
	public static void handlef(Exception e, String format, Object... args) {
		handle(e, String.format(format, args));
	}
	
	private static void dispatchException(Exception e) {
		try {
			DISPATCHER.onEvent(e);
		} catch (Exception e1) {
			System.out.printf("Failed to handle exception %s due to %s\n", e.getClass().getName(), e1.getClass().getName());
			e1.printStackTrace();
		}
	}
	
	public static void addHandler(Listener<Exception> listener) {
		DISPATCHER.addListener(listener);
	}
	
	public static void removeHandler(Listener<Exception> listener) {
		DISPATCHER.removeListener(listener);
	}
	
	public static void enableUncaughtExceptionHandling() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.err.println("Uncaught throwable in thread " + t.getName());
				e.printStackTrace();
				
				if (e instanceof Exception) {
					handlef((Exception) e,
							"Uncaught exception %s in thread %s",
							e.getClass(),
							t.getName());
				}
			}
			
		});
	}

}
