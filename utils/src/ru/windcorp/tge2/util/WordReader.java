package ru.windcorp.tge2.util;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class WordReader implements Iterator<String> {
	
	private final Reader reader;
	
	private char[] wordBuffer = new char[1024];
	private final CharBuffer inputBuffer;
	
	private String next = null;
	private boolean isExhausted = false;
	
	private IOException lastException = null;

	public WordReader(Reader src, int bufferSize) {
		this.reader = src;
		this.inputBuffer = CharBuffer.allocate(bufferSize);
	}
	
	public WordReader(Reader src) {
		this(src, 2048);
	}
	
	public WordReader(char[] array, int offset, int length) {
		this.reader = null;
		this.inputBuffer = CharBuffer.wrap(Arrays.copyOfRange(array, offset, length + offset));
	}
	
	public WordReader(char[] array) {
		this.reader = null;
		this.inputBuffer = CharBuffer.wrap(array);
	}
	
	public WordReader(String str) {
		this(str.toCharArray());
	}
	
	@Override
	public String next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		
		String result = next;
		next = null;
		return result;
	}
	
	@Override
	public boolean hasNext() {
		if (next != null) {
			return true;
		}
		
		if (isExhausted) {
			return false;
		}
		
		int length = 0;
		char c;
		while (true) {
			c = nextChar();
			
			if (isExhausted) break;
			
			if (Character.isWhitespace(c)) {
				if (length == 0) continue;
				else break;
			}
			
			if (wordBuffer.length == length) {
				char[] newBuf = new char[wordBuffer.length * 2];
				System.arraycopy(wordBuffer, 0, newBuf, 0, wordBuffer.length);
				wordBuffer = newBuf;
			}
			
			wordBuffer[length++] = c;
		}
		
		if (length == 0) {
			return false;
		}
		
		next = new String(wordBuffer, 0, length);
		return true;
	}

	private char nextChar() {
		if (!inputBuffer.hasRemaining()) {
			if (reader == null) {
				isExhausted = true;
				return 0;
			}
			
			inputBuffer.rewind();
			try {
				if (reader.read(inputBuffer) == -1) {
					isExhausted = true;
				}
			} catch (IOException e) {
				lastException = e;
				isExhausted = true;
				return 0;
			}
			
		}

		return inputBuffer.get();
	}
	
	public IOException getLastException() {
		return lastException;
	}

}
