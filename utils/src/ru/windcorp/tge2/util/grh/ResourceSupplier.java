package ru.windcorp.tge2.util.grh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provides access to various resources; base for GRH operations.
 * @author OLEGSHA
 */
public interface ResourceSupplier {
	
	/**
	 * Returns an InputStream containing the contents of the specified resource.
	 * @param path GRH path to the resource
	 * @return an InputStream
	 * @throws IOException if the specified resource cannot be read
	 */
	public InputStream getInputStream(String path) throws IOException;
	
	/**
	 * Checks whether the specified resource can be read.
	 * @param path GRH path to the resource 
	 * @return null or String containing the reason of denial
	 */
	public String canRead(String path);
	
	/**
	 * Returns an OutputStream to the specified resource. The resource
	 * will be created if it is not present.
	 * @param path GRH path to the resource
	 * @return an OutputStream
	 * @throws IOException if the specified resource cannot be written
	 * or this supplier does not support writing
	 */
	public OutputStream getOutputStream(String path) throws IOException;

	/**
	 * Checks whether the specified resource can be written.
	 * @param path GRH path to the resource 
	 * @return null or String containing the reason of denial
	 */
	public String canWrite(String path);
	
	/**
	 * Deletes the specified resource.
	 * @param path GRH path to the resource
	 * @return null or String containing the reason of denial
	 */
	public String delete(String path);
	
	/**
	 * Transforms the provided GRH path to a path canonical to this resource supplier,
	 * e.g. "dir/res" to "C:\base\dir\res".
	 * @param path General Resource Handler path to transform
	 * @return the resource identificator represented by the provided GRH path
	 */
	public String toCanonical(String path);
	
}
