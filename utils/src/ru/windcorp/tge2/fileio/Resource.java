package ru.windcorp.tge2.fileio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.util.Nameable;
import ru.windcorp.tge2.util.interfaces.Supplier;

/**
 * @deprecated please use Generic Resource Handling package {@linkplain ru.windcorp.tge2.util.grh}
 * @author OLEGSHA
 */
@Deprecated
public class Resource extends Nameable {
	
	private final ResourceManager manager;
	
	public Resource(String name, ResourceManager manager) {
		super(name);
		this.manager = manager;
	}

	public ResourceManager getManager() {
		return manager;
	}
	
	public InputStream getInputStream() throws IOException {
		return getManager().getInputStream(getName());
	}
	
	public OutputStream getOutputStream() throws IOException {
		return getManager().getOutputStream(getName());
	}
	
	public Supplier<OutputStream> getOutputStreamSupplier() throws IOException {
		return getManager().createOutputStreamSupplier(getName());
	}
	
	public Supplier<OutputStream> getOutputStreamSupplierOrNull() {
		try {
			return getOutputStreamSupplier();
		} catch (IOException e) {
			return null;
		}
	}
	
	@Override
	public String toString() {
		return getName() + " in " + getManager();
	}

}
