package ru.windcorp.tge2.fileio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @deprecated please use Generic Resource Handling package {@linkplain ru.windcorp.tge2.util.grh}
 * @author OLEGSHA
 */
@Deprecated
public class JarResourceSupplier implements ResourceSupplier {
	
	private final ClassLoader loader;
	private final String internalPath;

	public JarResourceSupplier(ClassLoader loader, String internalPath) {
		this.loader = loader;
		
		if (internalPath == null) {
			this.internalPath = "/";
			
		} else {
			if (!internalPath.startsWith("/")) {
				internalPath = '/' + internalPath;
			}
			
			if (!internalPath.endsWith("/")) {
				internalPath = internalPath + '/';
			}
			
			this.internalPath = internalPath;
		}
	}
	
	public JarResourceSupplier(ClassLoader loader) {
		this(loader, null);
	}
	
	public JarResourceSupplier(Class<?> parent, String internalPath) {
		this(parent.getClassLoader(), internalPath);
	}
	
	public JarResourceSupplier(Class<?> parent) {
		this(parent.getClassLoader(), null);
	}
	
	public JarResourceSupplier(String internalPath) {
		this(JarResourceSupplier.class, internalPath);
	}
	
	public JarResourceSupplier() {
		this(JarResourceSupplier.class);
	}

	public ClassLoader getLoader() {
		return loader;
	}
	
	public String getInternalPath() {
		return internalPath;
	}

	@Override
	public void destroy() {}

	@Override
	public InputStream getInputStream(String path) {
		return getLoader().getResourceAsStream(getInternalPath() + path);
	}

	@Override
	public boolean canGetInputStream(String path) {
		return getInputStream(path) != null;
	}

	@Override
	public OutputStream getOutputStream(String path) {
		return null;
	}

	@Override
	public boolean canGetOutputStream(String path) {
		return false;
	}
	
	@Override
	public String toString() {
		return "JAR Resource Loader";
	}

	@Override
	public boolean canCreate(String path) {
		return false;
	}

	@Override
	public void create(String path) throws IOException {
		throw new IOException(getClass() + " cannot create resources");
	}

}
