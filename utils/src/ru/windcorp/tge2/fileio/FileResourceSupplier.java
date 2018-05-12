package ru.windcorp.tge2.fileio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @deprecated please use Generic Resource Handling package {@linkplain ru.windcorp.tge2.util.grh}
 * @author OLEGSHA
 */
@Deprecated
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
	public void destroy() {}

	@Override
	public InputStream getInputStream(String path) {
		try {
			return new FileInputStream(parsePath(path));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	@Override
	public boolean canGetInputStream(String path) {
		return parsePath(path).canRead();
	}

	@Override
	public OutputStream getOutputStream(String path) {
		try {
			return new FileOutputStream(parsePath(path), false);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	@Override
	public boolean canGetOutputStream(String path) {
		File file = parsePath(path);
		return !file.exists() || file.canWrite();
	}
	
	protected File parsePath(String resourcePath) {
		return new File(getDir() + File.separator + resourcePath.replace("/", File.separator));
	}
	
	@Override
	public String toString() {
		return "File Resource Supplier with work directory \"" + getDir() + "\"";
	}

	@Override
	public boolean canCreate(String path) {
		return !parsePath(path).exists();
	}

	@Override
	public void create(String path) throws IOException {
		parsePath(path).createNewFile();
	}

}
