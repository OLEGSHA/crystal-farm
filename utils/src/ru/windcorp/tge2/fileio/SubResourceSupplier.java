package ru.windcorp.tge2.fileio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @deprecated please use Generic Resource Handling package {@linkplain ru.windcorp.tge2.util.grh}
 * @author OLEGSHA
 */
@Deprecated
public class SubResourceSupplier implements ResourceSupplier {

	private ResourceSupplier parent;
	private final String pathPrefix;
	
	public SubResourceSupplier(ResourceSupplier parent, String pathPrefix) {
		this.parent = parent;
		this.pathPrefix = pathPrefix;
		
		if (!pathPrefix.endsWith("/")) {
			pathPrefix += "/";
		}
	}
	
	public SubResourceSupplier(ResourceManager parent, String pathPrefix) {
		this(parent.getSupplier(), pathPrefix);
	}

	public ResourceSupplier getParent() {
		return parent;
	}

	public String getPathPrefix() {
		return pathPrefix;
	}

	@Override
	public void destroy() {
		parent.destroy();
		parent = null;
	}

	@Override
	public InputStream getInputStream(String path) {
		return parent.getInputStream(getPathPrefix() + path);
	}

	@Override
	public boolean canGetInputStream(String path) {
		return parent.canGetInputStream(getPathPrefix() + path);
	}

	@Override
	public OutputStream getOutputStream(String path) {
		return parent.getOutputStream(getPathPrefix() + path);
	}

	@Override
	public boolean canGetOutputStream(String path) {
		return parent.canGetOutputStream(getPathPrefix() + path);
	}

	@Override
	public boolean canCreate(String path) {
		return parent.canCreate(getPathPrefix() + path);
	}

	@Override
	public void create(String path) throws IOException {
		parent.create(getPathPrefix() + path);
	}

}
