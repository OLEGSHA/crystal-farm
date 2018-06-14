package ru.windcorp.tge2.util.stream;

import java.io.OutputStream;

public class StringBuilderOutputStream extends OutputStream {

	private final StringBuilder stringBuilder;
	
	public StringBuilderOutputStream() {
		this.stringBuilder = new StringBuilder();
	}
	
	public StringBuilderOutputStream(StringBuilder builder) {
		this.stringBuilder = builder;
	}
	
	@Override
	public void write(int b) {
		getStringBuilder().append((char) b);
	}

	@Override
	public void write(byte[] bytes) {
		getStringBuilder().ensureCapacity(getStringBuilder().capacity() + bytes.length);
		for (byte b : bytes) {
			getStringBuilder().append((char) b);
		}
	}

	@Override
	public String toString() {
		return getStringBuilder().toString();
	}

	public StringBuilder getStringBuilder() {
		return stringBuilder;
	}

}
