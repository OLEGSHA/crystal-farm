package ru.windcorp.tge2.util.synch;

public class SynchUtil {
	
	public static void lock(Object obj) {
		lock(obj, 0);
	}
	
	public static void lock(Object obj, long timeout) {
		synchronized (obj) {
			try {
				obj.wait(timeout);
			} catch (Exception e) {}
		}
	}
	
	public static void unlock(Object obj) {
		synchronized (obj) {
			obj.notifyAll();
		}
	}
	
	public static void pause(long millis) {
		Object obj = new Object();
		synchronized (obj) {
			try {
				obj.wait(millis);
			} catch (InterruptedException e) {}
		}
	}

}
