package ru.windcorp.tge2.fileio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.util.exceptions.UnexpectedException;
import ru.windcorp.tge2.util.grh.OutputSupplier;
import ru.windcorp.tge2.util.interfaces.Supplier;
import ru.windcorp.tge2.util.interfaces.Destroyable;

public abstract class AbstractWritable implements Destroyable {
	
	public static Supplier<OutputStream> createSupplier(final File file) {
		return new Supplier<OutputStream>() {

			@Override
			public OutputStream supply() {
				try {
					return new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					throw new UnexpectedException(e);
				}
			}
			
		};
	}
	
	public static void checkFile(File file, boolean write) {
		if (file == null) {
			throw new IllegalArgumentException(new NullPointerException());
		}
		
		if (!file.exists()) {
			throw new IllegalArgumentException("File " + file + " does not exist");
		}
		
		if (!file.isFile()) {
			throw new IllegalArgumentException("File " + file + " does not denote a file (directory?)");
		}
		
		if (write) {
			if (!file.canWrite()) {
				throw new IllegalArgumentException("File " + file + " cannot be written to");
			}
		} else {
			if (!file.canRead()) {
				throw new IllegalArgumentException("File " + file + " cannot be read");
			}
		}
	}

	public static InputStream getInputStream(File file) {
		checkFile(file, false);
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new UnexpectedException(e);
		}
	}
	
	public static OutputStream getOutputStream(File file) {
		checkFile(file, true);
		try {
			return new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			throw new UnexpectedException(e);
		}
	}
	
	public static OutputSupplier toOutputSupplier(final Supplier<OutputStream> oldSupplier) { 
		return new OutputSupplier() {

			@Override
			public OutputStream supply() throws IOException {
				try {
					return oldSupplier.supply();
				} catch (Exception e) {
					throw new IOException(e);
				}
			}
			
		};
	}
	
	private OutputSupplier output;
	
	public AbstractWritable(Supplier<OutputStream> output) {
		this(toOutputSupplier(output));
	}
	
	public AbstractWritable(OutputSupplier supplier) {
		this.output = supplier;
	}
	
	public AbstractWritable() {
		this((OutputSupplier) null);
	}

	public OutputSupplier getOutput() {
		return output;
	}

	public void setOutput(OutputSupplier output) {
		this.output = output;
	}
	
	public void setOutput(Supplier<OutputStream> output) {
		setOutput(toOutputSupplier(output));
	}
	
	public abstract void save(OutputStream output) throws IOException;
	
	public void save() throws IOException {
		if (getOutput() != null) {
			OutputStream os = getOutput().supply();
			save(os);
			os.close();
		}
	}
	
	public void save(File file) throws IOException {
		OutputStream os = getOutputStream(file);
		save(os);
		os.close();
	}
	
	public byte[] saveToByteArray() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		save(baos);
		return baos.toByteArray();
	}
	
	@Override
	public void destroy() {
		output = null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((output == null) ? 0 : output.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractWritable other = (AbstractWritable) obj;
		if (output == null) {
			if (other.output != null)
				return false;
		} else if (!output.equals(other.output))
			return false;
		return true;
	}

}
