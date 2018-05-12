package ru.windcorp.tge2.util;

import java.security.SecureRandom;
import java.util.Random;

import ru.windcorp.tge2.util.interfaces.Supplier;

public class PasswordGenerator implements Supplier<String> {

	private static final Random RANDOM = new SecureRandom();
	
	private final int length;
	private final char[] chars;
	
	public PasswordGenerator(int length, char[] chars) {
		this.length = length;
		this.chars = chars;
		
		if (chars == null) {
			throw new IllegalArgumentException("chars cannot be null");
		}
	}
	
	public PasswordGenerator(int length, String chars) {
		this(length, chars.toCharArray());
	}
	
	public PasswordGenerator(int length) {
		this(length, "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890"
				+ "-=!@#$%^&*()_+[]\\;',./{}|:\"<>?`~");
	}
	
	public PasswordGenerator(char[] chars) {
		this(16, chars);
	}
	
	public PasswordGenerator(String chars) {
		this(16, chars);
	}
	
	public PasswordGenerator() {
		this(16);
	}

	public int getLength() {
		return length;
	}

	public char[] getChars() {
		return chars;
	}

	@Override
	public String supply() {
		char[] result = new char[getLength()];
		for (int i = 0; i < result.length; ++i) {
			result[i] = getChars()[RANDOM.nextInt(getChars().length)];
		}
		return new String(result);
	}

}
