package ru.windcorp.tge2.fileio.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import ru.windcorp.tge2.fileio.AbstractWritable;
import ru.windcorp.tge2.util.StringUtil;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.exceptions.UnexpectedException;
import ru.windcorp.tge2.util.interfaces.Supplier;
import ru.windcorp.tge2.util.interfaces.Destroyable;

public class PlainRawData extends AbstractWritable implements Destroyable, Cloneable {

	public static final String FILE_SUFFIX = ".tge2prd";
	
	public static final char SEPARATOR = '=';
	public static final char ELEMENT_SEPARATOR = '\n';
	
	public static final char ESCAPE_CHAR = '\\';
	public static final char ESCAPE_UNICODE_CHAR = 'u';
	public static final char[] ESCAPE_UNSAFES = new char[] {SEPARATOR, ELEMENT_SEPARATOR};
	public static final char[] ESCAPE_SAFES = new char[] {'e', 'n'};
	
	private LinkedList<PRDElement> data = new LinkedList<PRDElement>();

	public static String toSafeString(String str) {
		return new String(StringUtil.encodeEscapeCharacters(str.toCharArray(),
				ESCAPE_CHAR, ESCAPE_SAFES, ESCAPE_UNSAFES));
	}
	
	public static String toUnsafeString(String str) throws SyntaxException {
		return new String(StringUtil.parseEscapeCharacters(str.toCharArray(),
				ESCAPE_CHAR, ESCAPE_UNICODE_CHAR, ESCAPE_SAFES, ESCAPE_UNSAFES));
	}
	
	public static PlainRawData read(InputStream input, Supplier<OutputStream> output)
			throws IOException, SyntaxException {
		
		if (input == null) {
			throw new IllegalArgumentException(new NullPointerException());
		}
		
		LinkedList<PRDElement> elements = new LinkedList<PRDElement>();
		StringBuilder name = new StringBuilder(),
				value = new StringBuilder();
		boolean isName = true;	
		int i;
		
		while ((i = input.read()) != -1) {
			if (i == SEPARATOR) {
				if (isName) {
					isName = false;
				} else {
					value.append((char) i);
				}
				
			} else if (i == ELEMENT_SEPARATOR) {
				elements.add(new PRDElement(toUnsafeString(name.toString()),
						toUnsafeString(value.toString())));
				name.delete(0, name.length());
				value.delete(0, value.length());
				isName = true;
				
			} else {
				if (isName) {
					name.append((char) i);
				} else {
					value.append((char) i);
				}
			}
		}
		
		return new PlainRawData(elements, output);
	}
	
	public static PlainRawData read(final File file)
			throws IOException, SyntaxException {
		
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
	
	public static PlainRawData readWithoutFeedback(final File file)
			throws IOException, SyntaxException {
		
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

	public PlainRawData(LinkedList<PRDElement> data, Supplier<OutputStream> output) {
		super(output);
		this.data = data;
	}

	public PlainRawData(Supplier<OutputStream> output) {
		super(output);
		this.data = new LinkedList<PRDElement>();
	}
	
	public PlainRawData() {
		this(null);
	}
	
	public PlainRawData(Iterable<PRDElement> data, Supplier<OutputStream> output) {
		this(output);
		
		for (PRDElement e : data) {
			getData().add(e);
		}
	}

	public LinkedList<PRDElement> getData() {
		return data;
	}
	
	public PRDElement get(String key) {
		for (PRDElement e : getData()) {
			if (e.getName().equals(key)) {
				return e;
			}
		}
		
		return null;
	}
	
	public boolean contains(String key) {
		return get(key) != null;
	}
	
	public String getValue(String key, String def) {
		PRDElement element = get(key);
		if (element == null) {
			return def;
		}
		return get(key).getValue();
	}
	
	public String getValue(String key) {
		return getValue(key, null);
	}
	
	public void setValue(String key, String value) {
		PRDElement element = get(key);
		if (element != null) {
			element.setValue(value);
		}
	}
	
	public synchronized void add(PRDElement element) {
		getData().add(element);
	}
	
	public void addValue(String key, String value) {
		add(new PRDElement(key, value));
	}
	
	public void putValue(String key, String value) {
		PRDElement element = get(key);
		if (element == null) {
			addValue(key, value);
		} else {
			element.setValue(value);
		}
	}
	
	@Override
	public synchronized void save(OutputStream output) throws IOException {
		byte[] encoded;
		for (PRDElement e : getData()) {
			encoded = (e.toString() + "\n").getBytes();
			output.write(encoded, 0, encoded.length);
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		data.clear();
		data = null;
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
		PlainRawData other = (PlainRawData) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

	@Override
	public PlainRawData clone() {
		try {
			PlainRawData clone = (PlainRawData) super.clone();
			
			clone.data = new LinkedList<PRDElement>();
			for (PRDElement e : data) {
				clone.data.add(e.clone());
			}
			
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new UnexpectedException(e);
		}
	}
}
