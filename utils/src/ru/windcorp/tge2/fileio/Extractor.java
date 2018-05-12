package ru.windcorp.tge2.fileio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.windcorp.tge2.util.StreamUtil;

public class Extractor {
	
	public static void extract(String local, File destination, Class<?> hook) throws IOException {
		if (hook == null) {
			throw new IllegalArgumentException("hook cannot be null");
		}
		
		InputStream input = hook.getClassLoader().getResourceAsStream(local);
		if (input == null) {
			throw new IOException("Local resource " + local + " not found");
		}
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(destination);
			StreamUtil.flush(input, fos, 4096);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

}
