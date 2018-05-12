package ru.windcorp.tge2.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil {
	
	public static void flush(InputStream src, OutputStream dest) throws IOException {
		flush(src, dest, 1024);
	}
	
	public static void flush(InputStream src, OutputStream dest, int bufferSize) throws IOException {
		if (bufferSize <= 0) {
			throw new IllegalArgumentException("bufferSize must be positive");
		}
		
		try {
			flush(src, dest, new byte[bufferSize]);
		} catch (OutOfMemoryError e) {
			throw new IllegalArgumentException("Buffer too big, failed to allocate memory", e);
		}
	}
	
	public static void flush(InputStream src, OutputStream dest, byte[] buffer) throws IOException {
		if (src == null) {
			throw new IllegalArgumentException("src == null", new NullPointerException());
		}
		
		if (dest == null) {
			throw new IllegalArgumentException("dest == null", new NullPointerException());
		}
		
		if (buffer == null) {
			throw new IllegalArgumentException("buffer == null", new NullPointerException());
		}
		
		if (buffer.length == 0) {
			throw new IllegalArgumentException("buffer.length = 0");
		}
		
		int readBytes;
		while ((readBytes = src.read(buffer)) != -1) {
			dest.write(buffer, 0, readBytes);
		}
	}

}
