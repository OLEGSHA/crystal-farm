package ru.windcorp.tge2.util.grh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SubResourceSupplier implements ResourceSupplier {

	private final ResourceSupplier parent;
	private final String path;

	public SubResourceSupplier(ResourceSupplier parent, String path) {
		this.parent = parent;
		
		if (path == null || path.isEmpty()) {
			path = "/";
		} else {
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			
			if (!path.endsWith("/")) {
				path = path + "/";
			}
		}
		
		this.path = path;
	}
	
	public SubResourceSupplier(ResourceManager rm, String path) {
		this(rm.getSupplier(), path);
	}

	public ResourceSupplier getParent() {
		return parent;
	}

	public String getPath() {
		return path;
	}
	
	public String absolute(String relativePath) {
		return getPath() + relativePath;
	}

	@Override
	public InputStream getInputStream(String path) throws IOException {
		return getParent().getInputStream(absolute(path));
	}

	@Override
	public String canRead(String path) {
		return getParent().canRead(absolute(path));
	}

	@Override
	public OutputStream getOutputStream(String path) throws IOException {
		return getParent().getOutputStream(absolute(path));
	}

	@Override
	public String canWrite(String path) {
		return getParent().canWrite(absolute(path));
	}

	@Override
	public String delete(String path) {
		return getParent().delete(absolute(path));
	}

	@Override
	public String toCanonical(String path) {
		return getParent().toCanonical(absolute(path));
	}

}
