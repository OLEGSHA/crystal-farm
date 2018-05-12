package ru.windcorp.tge2.fileio.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import ru.windcorp.tge2.fileio.AbstractWritable;
import ru.windcorp.tge2.util.bytes.ByteIOUtil;
import ru.windcorp.tge2.util.exceptions.UnexpectedException;
import ru.windcorp.tge2.util.interfaces.Supplier;

public class ByteData extends AbstractWritable implements Cloneable {
	
	public static final String FILE_SUFFIX = ".tge2bd";
	
	private LinkedList<BDElement> data = new LinkedList<BDElement>();
	
	public static ByteData read(InputStream input, Supplier<OutputStream> output)
			throws IOException {
		
		if (input == null) {
			throw new IllegalArgumentException(new NullPointerException());
		}
		
		LinkedList<BDElement> elements = new LinkedList<BDElement>();
		
		int name;
		byte[] value;
		
		while (input.available() != 0) {
			name = ByteIOUtil.readInt(input);
			value = new byte[ByteIOUtil.readInt(input)];
			
			input.read(value);
			
			elements.add(new BDElement(name, value));
		}
		
		return new ByteData(elements, output);
	}

	public static ByteData read(final File file)
			throws IOException {
		
		if (file == null) {
			throw new IllegalArgumentException(new NullPointerException());
		}
		
		if (!file.exists()) {
			throw new IllegalArgumentException("File " + file + " does not exist");
		}
		
		if (!file.isFile()) {
			throw new IllegalArgumentException("File " + file + " does not denote a file (directory?)");
		}
		
		if (!file.canRead()) {
			throw new IllegalArgumentException("File " + file + " cannot be read");
		}
		
		return read(new FileInputStream(file), createSupplier(file));
	}
	
	public static ByteData readWithoutFeedback(final File file)
			throws IOException {
		
		if (file == null) {
			throw new IllegalArgumentException(new NullPointerException());
		}
		
		if (!file.exists()) {
			throw new IllegalArgumentException("File " + file + " does not exist");
		}
		
		if (!file.isFile()) {
			throw new IllegalArgumentException("File " + file + " does not denote a file (directory?)");
		}
		
		if (!file.canRead()) {
			throw new IllegalArgumentException("File " + file + " cannot be read");
		}
		
		return read(new FileInputStream(file), null);
	}
	
	public ByteData(LinkedList<BDElement> data, Supplier<OutputStream> output) {
		super(output);
		this.data = data;
	}

	public ByteData(Supplier<OutputStream> output) {
		super(output);
		this.data = new LinkedList<BDElement>();
	}
	
	public ByteData() {
		this(null);
	}
	
	public ByteData(Iterable<BDElement> data, Supplier<OutputStream> output) {
		this(output);
		
		for (BDElement e : data) {
			getData().add(e);
		}
	}

	public LinkedList<BDElement> getData() {
		return data;
	}
	
	public BDElement get(int name) {
		for (BDElement e : getData()) {
			if (e.getName() == name) {
				return e;
			}
		}
		
		return null;
	}
	
	public boolean contains(int name) {
		return get(name) != null;
	}
	
	public byte[] getValue(int name, byte[] def) {
		BDElement element = get(name);
		if (element == null) {
			return def;
		}
		return get(name).getValue();
	}
	
	public byte[] getValue(int name) {
		return getValue(name, null);
	}
	
	public void setValue(int name, byte[] value) {
		BDElement element = get(name);
		if (element != null) {
			element.setValue(value);
		}
	}
	
	public synchronized void add(BDElement element) {
		getData().add(element);
	}
	
	public void addValue(int name, byte[] value) {
		add(new BDElement(name, value));
	}
	
	public void putValue(int name, byte[] value) {
		BDElement element = get(name);
		if (element == null) {
			addValue(name, value);
		} else {
			element.setValue(value);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ByteData other = (ByteData) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}
	
	@Override
	protected ByteData clone() {
		try {
			ByteData clone = (ByteData) super.clone();
			
			clone.data = new LinkedList<BDElement>();
			for (BDElement e : data) {
				clone.data.add(e.clone());
			}
			
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		data.clear();
		data = null;
	}

	@Override
	public void save(OutputStream output) throws IOException {
		for (BDElement e : getData()) {
			output.write(e.toBytes());
		}
	}

}
