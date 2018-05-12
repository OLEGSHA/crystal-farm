package ru.windcorp.tge2.fileio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.util.interfaces.Destroyable;

/**
 * @deprecated please use Generic Resource Handling package {@linkplain ru.windcorp.tge2.util.grh}
 * @author OLEGSHA
 */
@Deprecated
public interface ResourceSupplier extends Destroyable {
	
	public InputStream getInputStream(String path);
	public boolean canGetInputStream(String path);
	
	public OutputStream getOutputStream(String path);
	public boolean canGetOutputStream(String path);
	
	public boolean canCreate(String path);
	public void create(String path) throws IOException;
	
}
