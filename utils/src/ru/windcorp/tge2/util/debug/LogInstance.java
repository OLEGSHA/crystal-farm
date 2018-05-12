package ru.windcorp.tge2.util.debug;

import java.util.Stack;

import ru.windcorp.tge2.util.perf.StringBuilderPool;

public class LogInstance {
	
	private final Stack<String> topics = new Stack<String>();
	private String topicString = "";
	private boolean topicStringValid = true;
	
	private static final StringBuilderPool STRING_BUILDERS = new StringBuilderPool();
	
	/**
	 * Opens a new topic on top of any existing ones.
	 * @param topic - the new topic
	 */
	public void topic(String topic) {
		getTopics().push(topic);
		invalidateTopicString();
	}
	
	private void invalidateTopicString() {
		topicStringValid = false;
	}

	/**
	 * Ends the current topic. If no topics are open, nothing happens.
	 * */
	public void end() {
		if (!getTopics().isEmpty()) {
			getTopics().remove(getTopics().size() - 1);
			invalidateTopicString();
		}
	}
	
	/**
	 * Ends all open topics.
	 */
	public void endAll() {
		while (!getTopics().isEmpty()) {
			end();
		}
	}
	
	/**
	 * Ends all open topics up to the specified inclusive.
	 * If the specified topic is not present, ends all topics.
	 * @param topic - the topic to search for
	 * @return true is the topic was found, false otherwise
	 */
	public boolean end(String topic) {
		try {
			while (!getTopics().isEmpty()) {
				if (getTopics().pop().equals(topic)) {
					return true;
				}
			}
			
			return false;
		} finally {
			invalidateTopicString();
		}
	}
	
	/**
	 * Returns a textual representation of all open topics.
	 * If no topics are open, an empty String is returned.<p>
	 * Example: [Topic 1] [Topic 2] [Last Topic]
	 * @return a String used for logging
	 */
	/*
	 * For optimization purposes String generation is moved to generateTopicString()
	 */
	public String getTopicStrings() {
		if (topicStringValid) {
			return topicString;
		}
		
		topicStringValid = true;
		return topicString = generateTopicString();
	}
	
	private String generateTopicString() {
		if (getTopics().isEmpty()) {
			return "";
		}
		
		StringBuilder sb = STRING_BUILDERS.acquire();
		sb.append("[" + getTopics().firstElement() + "]");
		for (int i = 1; i < getTopics().size(); ++i) {
			sb.append(" [");
			sb.append(getTopics().get(i));
			sb.append("]");
		}
		
		return STRING_BUILDERS.getAndRelease(sb);
	}

	private String addTopics(String msg) {
		if (getTopics().isEmpty()) {
			return msg;
		}
		return getTopicStrings() + "   " + msg;
	}
	
	public Stack<String> getTopics() {
		return topics;
	}
	
	private static String addTags(String msg, Object... tags) {
		if (tags == null || tags.length == 0) {
			return msg;
		}
		
		StringBuilder sb = STRING_BUILDERS.acquire();

		sb.append(msg);
		sb.append("   ");
		
		for (Object tag : tags) {
			sb.append(" <");
			if (tag != null) {
				sb.append(tag);
			} else {
				sb.append(Debug.getLastMethod(1));
			}
			sb.append(">");
		}
		
		return STRING_BUILDERS.getAndRelease(sb);
	}
	
	public void debug(String msg) {
		Debug.debugRaw(addTopics(msg));
	}
	
	public void debug(String msg, Object... tags) {
		Debug.debugRaw(addTopics(addTags(msg, tags)));
	}
	
	public void debugObj(Object... params) {
		Debug.debugRaw(addTopics(Debug.argsToStrings(params, 1).toString()));
	}
	
	public void info(String msg) {
		Debug.inform(addTopics(msg));
	}
	
	public void info(String msg, Object... tags) {
		Debug.inform(addTopics(addTags(msg, tags)));
	}
	
	public void report(String msg) {
		Debug.report(addTopics(msg));
	}
	
	public void report(String msg, Object... tags) {
		Debug.report(addTopics(addTags(msg, tags)));
	}
	
	public void warn(String msg) {
		Debug.warn(addTopics(msg));
	}
	
	public void warn(String msg, Object... tags) {
		Debug.warn(addTopics(addTags(msg, tags)));
	}
	
	public void error(String msg) {
		Debug.warnError(addTopics(msg));
	}
	
	public void error(String msg, Object... tags) {
		Debug.warnError(addTopics(addTags(msg, tags)));
	}
	
	public void critical(String msg) {
		Debug.warnCritical(addTopics(msg));
	}
	
	public void critical(String msg, Object... tags) {
		Debug.warnCritical(addTopics(addTags(msg, tags)));
	}
	
}
