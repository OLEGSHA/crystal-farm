package ru.windcorp.tge2.util.debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;

public class ConsoleIO {
	
	public static final BufferedReader SYS_IN_READER = new BufferedReader(new InputStreamReader(System.in));
	
	public static String readLine() {
		try {
			return SYS_IN_READER.readLine();
		} catch (IOException e) {
			throw new UncheckedIOException("Exception while reading from System.in", e);
		}
	}
	
	public static void write(Object obj) {
		System.out.println(obj.toString());
	}

}
