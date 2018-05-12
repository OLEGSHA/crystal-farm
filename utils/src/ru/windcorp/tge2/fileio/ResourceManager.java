package ru.windcorp.tge2.fileio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.fileio.data.ByteData;
import ru.windcorp.tge2.fileio.data.PlainRawData;
import ru.windcorp.tge2.fileio.data.PlainTypedData;
import ru.windcorp.tge2.fileio.data.sbd.StructuredByteData;
import ru.windcorp.tge2.util.dataio.DataReader;
import ru.windcorp.tge2.util.dataio.DataWriter;
import ru.windcorp.tge2.util.dataio.InputStreamDataReader;
import ru.windcorp.tge2.util.dataio.OutputStreamDataWriter;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.exceptions.UnexpectedException;
import ru.windcorp.tge2.util.grh.Loadable;
import ru.windcorp.tge2.util.grh.OutputSupplier;
import ru.windcorp.tge2.util.grh.Saveable;
import ru.windcorp.tge2.util.interfaces.Supplier;
import ru.windcorp.tge2.util.interfaces.Destroyable;

/**
 * @deprecated please use Generic Resource Handling package {@linkplain ru.windcorp.tge2.util.grh}
 * @author OLEGSHA
 */
@Deprecated
public class ResourceManager implements Destroyable {
	
	private ResourceManager backup = null;
	private ResourceSupplier supplier;

	public ResourceManager(ResourceSupplier supplier) {
		this.supplier = supplier;
	}

	public ResourceSupplier getSupplier() {
		return supplier;
	}

	public ResourceManager getBackup() {
		return backup;
	}

	public void setBackup(ResourceManager backup) {
		this.backup = backup;
	}

	@Override
	public void destroy() {
		supplier.destroy();
		supplier = null;
		backup = null;
	}
	
	public boolean canRead(String path) {
		return getSupplier().canGetInputStream(path) ||
				(getBackup() != null && getBackup().canRead(path));
	}
	
	public InputStream getInputStream(String path) throws IOException {
		if (!canRead(path)) {
			throw new IOException("Resource " + path + " is not available for reading");
		}
		
		if (getSupplier().canGetInputStream(path)) {
			return getSupplier().getInputStream(path);
		}
		
		return getBackup().getInputStream(path);
	}
	
	public boolean canWrite(String path) {
		return getSupplier().canGetOutputStream(path) ||
				(getBackup() != null && getBackup().canWrite(path));
	}
	
	public OutputStream getOutputStream(String path) throws IOException {
		if (!canWrite(path)) {
			throw new IOException("Resource " + path + " is not available for writing");
		}
		
		if (getSupplier().canGetOutputStream(path)) {
			return getSupplier().getOutputStream(path);
		}
		
		return getBackup().getOutputStream(path);
	}
	
	public boolean create(String path) throws IOException {
		if (canRead(path)) {
			return false;
		}
		
		if (getSupplier().canCreate(path)) {
			getSupplier().create(path);
			return true;
		}
		
		if (getBackup() != null) {
			getBackup().create(path);
			return true;
		}
		
		throw new IOException("Could not create " + path);
	}
	
	public DataReader getDataReader(String path) throws IOException {
		return new InputStreamDataReader(getInputStream(path));
	}
	
	public DataWriter getDataWriter(String path) throws IOException {
		return new OutputStreamDataWriter(getOutputStream(path));
	}
	
	public void read(Loadable<DataReader> dwriterLoadable, String srcPath) throws IOException {
		dwriterLoadable.load(getDataReader(srcPath));
	}
	
	public void write(Saveable<DataWriter> dreaderSaveable, String destPath) throws IOException {
		dreaderSaveable.save(getDataWriter(destPath));
	}
	
	public Supplier<OutputStream> createOutputStreamSupplier(final String path) {
		return new Supplier<OutputStream>() {

			@Override
			public OutputStream supply() {
				try {
					return getOutputStream(path);
				} catch (IOException e) {
					throw new UnexpectedException(e);
				}
			}
			
		};
	}
	
	public PlainRawData readPRD(String path)
			throws IOException, SyntaxException {
		
		if (!getSupplier().canGetOutputStream(path)) {
			throw new IOException("Resource " + path + " is not available for writing"); 
		}
		
		return PlainRawData.read(getInputStream(path), createOutputStreamSupplier(path));
	}
	
	public PlainRawData readPRDNoFeedback(String path)
			throws IOException, SyntaxException {
		
		return PlainRawData.read(getInputStream(path), null);
	}
	
	public ByteData readBD(String path)
			throws IOException, SyntaxException {
		
		if (!getSupplier().canGetOutputStream(path)) {
			throw new IOException("Resource " + path + " is not available for writing"); 
		}
		
		return ByteData.read(getInputStream(path), createOutputStreamSupplier(path));
	}
	
	public ByteData readBDNoFeedback(String path)
			throws IOException, SyntaxException {
		
		return ByteData.read(getInputStream(path), null);
	}
	
	public PlainTypedData readPTD(String path)
			throws IOException, SyntaxException {
		
		if (!getSupplier().canGetOutputStream(path)) {
			throw new IOException("Resource " + path + " is not available for writing"); 
		}
		
		return PlainTypedData.read(getInputStream(path), createOutputStreamSupplier(path));
	}
	
	public PlainTypedData readPTDNoFeedback(String path)
			throws IOException, SyntaxException {
		
		return PlainTypedData.read(getInputStream(path), (OutputSupplier) null);
	}
	
	public StructuredByteData readSBD(String path)
			throws IOException, SyntaxException {
		return StructuredByteData.read(getInputStream(path), createOutputStreamSupplier(path));
	}
	
	public StructuredByteData readSBDNoFeedback(String path)
			throws IOException, SyntaxException {
		return StructuredByteData.read(getInputStream(path), (OutputSupplier) null);
	}
	
	@Override
	public String toString() {
		return "Resource Manager with supplier " + getSupplier() +
				(getBackup() == null ? "" : " backed by " + getBackup());
	}

}
