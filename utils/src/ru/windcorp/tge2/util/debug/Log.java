package ru.windcorp.tge2.util.debug;

import java.util.HashMap;
import java.util.Map;

public class Log {
	
	public static final Map<Thread, LogInstance> LOGS = new HashMap<Thread, LogInstance>();
	
	public static LogInstance getInstance() {
		LogInstance inst = LOGS.get(Thread.currentThread());
		if (inst == null) {
			inst = new LogInstance();
			LOGS.put(Thread.currentThread(), inst);
		}
		
		return inst;
	}
	
	/**
	 * Opens a new topic on top of any existing ones.
	 * @param topic - the new topic
	 */
	public static void topic(String topic) {
		getInstance().topic(topic);
	}

	/**
	 * Ends the current topic. If no topics are open, nothing happens.
	 * */
	public static void end() {
		getInstance().end();
	}
	
	/**
	 * Ends all open topics.
	 */
	public static void endAll() {
		getInstance().endAll();
	}
	
	/**
	 * Ends all open topics up to the specified inclusive.
	 * If the specified topic is not present, ends all topics.
	 * @param topic - the topic to search for
	 * @return true is the topic was found, false otherwise
	 */
	public static boolean end(String topic) {
		return getInstance().end(topic);
	}
	
	public static void debug(String msg) {
		getInstance().debug(msg);
	}
	
	public static void debug(String msg, Object... tags) {
		getInstance().debug(msg, tags);
	}
	
	public static void debugObj(Object... params) {
		getInstance().debugObj(params);
	}
	
	public static void info(String msg) {
		getInstance().info(msg);
	}
	
	public static void info(String msg, Object... tags) {
		getInstance().info(msg, tags);
	}
	
	public static void report(String msg) {
		getInstance().report(msg);
	}
	
	public static void report(String msg, Object... tags) {
		getInstance().report(msg, tags);
	}
	
	public static void warn(String msg) {
		getInstance().warn(msg);
	}
	
	public static void warn(String msg, Object... tags) {
		getInstance().warn(msg, tags);
	}
	
	public static void error(String msg) {
		getInstance().error(msg);
	}
	
	public static void error(String msg, Object... tags) {
		getInstance().error(msg, tags);
	}
	
	public static void critical(String msg) {
		getInstance().critical(msg);
	}
	
	public static void critical(String msg, Object... tags) {
		getInstance().critical(msg, tags);
	}

}
