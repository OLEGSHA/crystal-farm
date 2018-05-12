package ru.windcorp.tge2.util.grh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JarResourceSupplier implements ResourceSupplier {

	private final ClassLoader loader;
	private final String localPath;
	
	public JarResourceSupplier(ClassLoader loader, String localPath) {
		this.loader = loader;
		
		if (localPath == null || localPath.isEmpty() || localPath.equals("/")) {
			this.localPath = "/";
		} else {
			if (localPath.startsWith("/")) {
				localPath = localPath.substring("/".length());
			}
			
			if (!localPath.endsWith("/")) {
				localPath = localPath + "/";
			}
			
			this.localPath = localPath;
		}
	}
	
	public JarResourceSupplier(ClassLoader loader) {
		this(loader, null);
	}
	
	public JarResourceSupplier(Class<?> clazz, String localPath) {
		this(clazz.getClassLoader(), localPath);
	}
	
	public JarResourceSupplier(Class<?> clazz) {
		this(clazz, null);
	}
	
	public JarResourceSupplier(String localPath) {
		this(ResourceManager.class, localPath);
	}
	
	public JarResourceSupplier() {
		this((String) null);
	}

	public ClassLoader getLoader() {
		return loader;
	}

	public String getLocalPath() {
		return localPath;
	}

	@Override
	public InputStream getInputStream(String path) throws IOException {
		InputStream is = getLoader().getResourceAsStream(getLocalPath() + path);
		if (is == null) {
			throw new ResourceNotFoundException(path);
		}
		
		return is;
	}

	@Override
	public String canRead(String path) {
		InputStream is = getLoader().getResourceAsStream(getLocalPath() + path);
		if (is == null) {
			return "resource " + path + "not found";
		}
		
		return null;
	}

	@Override
	public OutputStream getOutputStream(String path) throws IOException {
		throw new WritingNotSupportedException("JarResourceSupplier does not support writing");
	}

	@Override
	public String canWrite(String path) {
		return "JarResourceSupplier does not support writing";
	}

	@Override
	public String delete(String path) {
		return "JarResourceSupplier does not support deleting";
	}

	@Override
	public String toCanonical(String path) {
		return getLocalPath() + path;
	}

}
