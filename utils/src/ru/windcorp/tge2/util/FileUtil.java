package ru.windcorp.tge2.util;

import java.io.*;
import java.util.Calendar;

public class FileUtil {

	public static FileOutputStream createFileAndStream(File file) throws IOException, FileNotFoundException {
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				throw new IOException("Failed to create directory \"" + file + "\" and its parent(s)");
			}
		}
		
		return new FileOutputStream(file);
	}
	
	public static File createFile(String path) throws IOException {
		File file = new File(path);
		
		if (!file.exists()) {
			if (file.isDirectory()) {
				if (!file.mkdirs())
					throw new IOException("Could not create directory " + file);
			} else {
				if (!file.getParentFile().exists()) {
					if (!file.getParentFile().mkdirs())
						throw new IOException("Could not create directory " + file.getParent());
				} else {
					
				}
				
				file.createNewFile();
			}
		}
		
		return file;
	}
	
	public static File createFileWithDate(String pathFormat) throws IOException {
		return createFile(String.format(pathFormat, Calendar.getInstance()));
	}
	
	public static String getLocalPath(String path) {
		return path.replace('/', File.separatorChar);
	}
	
}
