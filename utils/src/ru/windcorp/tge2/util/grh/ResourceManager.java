package ru.windcorp.tge2.util.grh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ru.windcorp.tge2.util.Nameable;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public class ResourceManager extends Nameable {

	private ResourceSupplier supplier;
	private final List<ResourceManager> backupManagers =
			Collections.synchronizedList(new LinkedList<ResourceManager>());
	
	public ResourceManager(String name, ResourceSupplier supplier) {
		super(name);
		this.supplier = supplier;
	}
	
	public ResourceManager(String name) {
		this(name, null);
	}

	public ResourceSupplier getSupplierSafely() {
		return supplier;
	}
	
	public ResourceSupplier getSupplier() {
		if (supplier == null) {
			throw new IllegalStateException("Resource Manager " + this + " does not have a resource supplier");
		}
		
		return supplier;
	}

	public void setSupplier(ResourceSupplier supplier) {
		this.supplier = supplier;
	}

	public List<ResourceManager> getBackupManagers() {
		return backupManagers;
	}
	
	public void addBackup(ResourceManager manager) {
		getBackupManagers().add(manager);
	}
	
	public void removeBackup(ResourceManager manager) {
		getBackupManagers().remove(manager);
	}
	
	public InputStream getInputStream(String path) throws IOException {
		try {
			return getSupplier().getInputStream(path);
		} catch (IOException e) {
			synchronized (getBackupManagers()) {
				for (ResourceManager r : getBackupManagers()) {
					try {
						return r.getInputStream(path);
					} catch (IOException e1) {
						e.addSuppressed(e1);
					}
				}
			}
			
			throw e;
		}
	}
	
	public OutputStream getOutputStream(String path) throws IOException {
		try {
			return getSupplier().getOutputStream(path);
		} catch (IOException e) {
			synchronized (getBackupManagers()) {
				for (ResourceManager r : getBackupManagers()) {
					try {
						return r.getOutputStream(path);
					} catch (IOException e1) {
						e.addSuppressed(e1);
					}
				}
			}
			
			throw e;
		}
	}
	
	public String canRead(String path) {
		String reason;
		if ((reason = getSupplier().canRead(path)) == null) {
			return null;
		}
		
		synchronized (getBackupManagers()) {
			for (ResourceManager r : getBackupManagers()) {
				if (r.canRead(path) == null) {
					return null;
				}
			}
		}
		
		return reason;
	}
	
	public String canWrite(String path) {
		String reason;
		if ((reason = getSupplier().canWrite(path)) == null) {
			return null;
		}
		
		synchronized (getBackupManagers()) {
			for (ResourceManager r : getBackupManagers()) {
				if (r.canWrite(path) == null) {
					return null;
				}
			}
		}
		
		return reason;
	}
	
	public Resource getResource(String path) {
		return new Resource(path, this);
	}
	
	public String getCanonicalPath(String path) {
		return getName() + ":" + getSupplier().toCanonical(path);
	}
	
	public <T> T read(String path, Class<T> format, Object... params)
			throws IOException, SyntaxException {
		@SuppressWarnings("unchecked")
		ResourceHandler<T> handler = (ResourceHandler<T>) ResourceHandlers.INST.get(format);
		
		if (handler == null) {
			throw new UnknownFormatException("No resource handler for format " + format);
		}
		
		T object = handler.create(getInputStream(path), getResource(path), params);
		
		assert object.getClass() == format : "Resource handler implemented incorrectly";
		
		return object;
	}
	
}
