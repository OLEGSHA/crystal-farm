package ru.windcorp.tge2.util.grh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ru.windcorp.tge2.util.Nameable;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

/**
 * Resource manager is an entity that can provide read/write access to abstract entities termed resources.
 * Resource managers are the basis of the GRH system, with {@link Resource} as its front-end and {@link ResourceSupplier}
 * as its back-end.
 * <p>
 * A resource manager can only perform abstract operations and relies on resource suppliers to perform actions.
 * It can be seen as an extension of the resource supplier's interface.
 * <p>
 * Resource managers can have backup managers that are consulted if this manager cannot perform a task.
 * This can be useful when several versions of the same resource system are available, e.g. when working
 * with user-defined resource packs.
 * 
 * @author OLEGSHA
 */
public class ResourceManager extends Nameable {

	private ResourceSupplier supplier;
	private final List<ResourceManager> backupManagers =
			Collections.synchronizedList(new LinkedList<ResourceManager>());
	
	/**
	 * Creates a resource manager initialized with the given resource supplier.
	 * @param name the name of the manager
	 * @param supplier the back-end
	 */
	public ResourceManager(String name, ResourceSupplier supplier) {
		super(name);
		this.supplier = supplier;
	}
	
	/**
	 * Creates a resource manager without a resource supplier. The supplier
	 * must be set explicitly using {@link #setSupplier(ResourceSupplier)} before
	 * most methods can be invoked.
	 * @param name the name of the manager
	 */
	public ResourceManager(String name) {
		this(name, null);
	}

	/**
	 * Gets the currently set resource supplier or returns {@code null}.
	 * @return the resource supplier or {@code null} if none set
	 * @see {@link #getSupplier()}
	 */
	public ResourceSupplier getSupplierSafely() {
		return supplier;
	}
	
	/**
	 * Gets the currently set resource supplier.
	 * @throws IllegalStateException if no resource supplier has been set
	 * @return the resource supplier
	 */
	public ResourceSupplier getSupplier() {
		if (supplier == null) {
			throw new IllegalStateException("Resource Manager " + this + " does not have a resource supplier");
		}
		
		return supplier;
	}

	/**
	 * Sets this resource manager's resource supplier.
	 * @param supplier the supplier to set
	 */
	public void setSupplier(ResourceSupplier supplier) {
		this.supplier = supplier;
	}

	/**
	 * Gets a list of backup managers.
	 * @return backup managers
	 */
	public List<ResourceManager> getBackupManagers() {
		return backupManagers;
	}
	
	/**
	 * Adds a backup manager.
	 * @param manager the manager to add
	 */
	public void addBackup(ResourceManager manager) {
		getBackupManagers().add(manager);
	}
	
	/**
	 * Removes a backup manager if it is present.
	 * @param manager the manager to remove
	 */
	public void removeBackup(ResourceManager manager) {
		getBackupManagers().remove(manager);
	}
	
	/**
	 * Gets the {@link InputStream} reading from the resource denoted by the given
	 * path. This methods first attempts to get an input stream from this manager's
	 * resource supplier. If it fails, {@code getInputStream(path)} is invoked for
	 * each backup manager until one succeeds. If no backup managers returned an
	 * input stream, an exception is thrown.
	 * @param path the path of the resource
	 * @return an {@code InputStream} 
	 * @throws IOException if neither this manager's supplier nor backup managers
	 * could return an input stream. The exceptions thrown by backup managers are
	 * recorded as suppressed.
	 */
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
	
	/**
	 * Gets the {@link OutputStream} writing into the resource denoted by the given
	 * path. This methods first attempts to get an output stream from this manager's
	 * resource supplier. If it fails, {@code getOutputStream(path)} is invoked for
	 * each backup manager until one succeeds. If no backup managers returned an
	 * output stream, an exception is thrown.
	 * @param path the path of the resource
	 * @return an {@code OutputStream} 
	 * @throws IOException if neither this manager's supplier nor backup managers
	 * could return an output stream. The exceptions thrown by backup managers are
	 * recorded as suppressed.
	 */
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
	
	/**
	 * Checks whether the given resource can read from and returns a human-readable
	 * description of the problem, if any. If {@code canRead(path)} returned {@code null}
	 * for some resource path {@code path}, the immediate invocation of {@code getInputStream()}
	 * should not throw an exception.
	 * @param path the path of the resource to check
	 * @return the problem preventing this resource from being read or {@code null} if it can be read.
	 */
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
	
	/**
	 * Checks whether the given resource can written into and returns a human-readable
	 * description of the problem, if any. If {@code canWrite(path)} returned {@code null}
	 * for some resource path {@code path}, the immediate invocation of {@code getOutputStream()}
	 * should not throw an exception.
	 * @param path the path of the resource to check
	 * @return the problem preventing this resource from being written or {@code null} if it can be written.
	 */
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
	
	/**
	 * Creates a {@link Resource} with the given path.
	 * @param path the path of the resource
	 * @return an instance of {@link Resource}
	 */
	public Resource getResource(String path) {
		return new Resource(path, this);
	}
	
	/**
	 * Returns a canonical representation of the given path.
	 * @param path the path to canonize
	 * @return the canonical representation of the given path.
	 */
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
