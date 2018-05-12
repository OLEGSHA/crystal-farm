package ru.windcorp.tge2.util.grh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileResourceSupplier implements ResourceSupplier {
	
	private final File dir;

	public FileResourceSupplier(File dir) {
		if (dir == null) {
			throw new IllegalArgumentException(new NullPointerException());
		}
		
		if (!dir.exists()) {
			throw new IllegalArgumentException("File " + dir + " does not exist");
		}
		
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException("File " + dir + " does not denote a directory (file?)");
		}
		
		this.dir = dir;
	}

	public File getDir() {
		return dir;
	}

	@Override
	public InputStream getInputStream(String path) throws IOException {
		try {
			return new FileInputStream(parsePath(path));
		} catch (FileNotFoundException e) {
			throw new ResourceNotFoundException(e);
		}
	}

	@Override
	public OutputStream getOutputStream(String path) throws IOException {
		try {
			return new FileOutputStream(parsePath(path), false);
		} catch (FileNotFoundException e) {
			throw new ResourceNotFoundException(e);
		}
	}
	
	protected File parsePath(String resourcePath) {
		return new File(getDir() + File.separator + resourcePath.replace("/", File.separator));
	}
	
	@Override
	public String toString() {
		return "File Resource Supplier with work directory \"" + getDir() + "\"";
	}

	@Override
	public String canRead(String path) {
		File file = parsePath(path);
		if (!file.exists()) {
			return "File " + file + " does not exist";
		}
		
		if (!file.isFile()) {
			return "File " + file + " is not file";
		}
		
		if (!file.canRead()) {
			return "File " + file + " cannot be read";
		}
		
		return null;
	}

	@Override
	public String canWrite(String path) {
		File file = parsePath(path);
		if (!file.exists()) {
			return null;
		}
		
		if (!file.isFile()) {
			return "File " + file + " is not file";
		}
		
		if (!file.canWrite()) {
			return "File " + file + " cannot be written";
		}
		
		return null;
	}

	@Override
	public String delete(String path) {
		File file = parsePath(path);
		if (!file.exists()) {
			return "File " + file + " does not exist";
		}
		
		if (file.isDirectory()) {
			return "File " + file + " is not a file";
		}
		
		if (!file.delete()) {
			return "File " + file + " could not be deleted";
		}
		
		return null;
	}

	@Override
	public String toCanonical(String path) {
		return parsePath(path).getAbsolutePath();
	}

}
