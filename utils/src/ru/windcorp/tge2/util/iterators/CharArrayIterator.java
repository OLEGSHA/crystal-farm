package ru.windcorp.tge2.util.iterators;

import java.util.NoSuchElementException;

public class CharArrayIterator {
	
	private final char[] chars;
	private int next = 0;
	
	public CharArrayIterator(String source) {
		this.chars = source.toCharArray();
	}

	public char[] getChars() {
		return chars;
	}
	
	public boolean hasNext() {
		return next < chars.length;
	}
	
	public char peek() {
		try {
			return chars[next];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new NoSuchElementException();
		}
	}
	
	public char next() {
		char c = peek();
		next++;
		return c;
	}

}
