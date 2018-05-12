package ru.windcorp.tge2.fileio.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import ru.windcorp.tge2.fileio.AbstractWritable;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.grh.OutputSupplier;
import ru.windcorp.tge2.util.interfaces.Generator;
import ru.windcorp.tge2.util.interfaces.Supplier;
import ru.windcorp.tge2.util.interfaces.Destroyable;
import ru.windcorp.tge2.util.SafeParser;
import ru.windcorp.tge2.util.StringUtil;

public class PlainTypedData extends AbstractWritable implements Destroyable, Cloneable {
	
	public static final String FILE_SUFFIX = ".tge2ptd";
	
	public static final char SEPARATOR = '=';
	public static final char ELEMENT_SEPARATOR = '\n';
	
	public static final char ESCAPE_CHAR = '\\';
	public static final char ESCAPE_UNICODE_CHAR = 'u';
	public static final char[] ESCAPE_UNSAFES = new char[] {SEPARATOR, ELEMENT_SEPARATOR};
	public static final char[] ESCAPE_SAFES = new char[] {'e', 'n'};
	
	private LinkedList<PTDElement> data = new LinkedList<PTDElement>();
	
	public static enum DataType {
		BYTE_SEQUENCE ('q', byte[].class, new Generator<String, Object>() {

			@Override
			public Object generate(String input) {
				return input.getBytes();
			}
			
		}),
			
		BOOLEAN ('b', Boolean.class, new Generator<String, Object>() {

			@Override
			public Object generate(String input) {
				return "1".equals(input);
			}
		
		}),
		
		BYTE ('B', Byte.class, new Generator<String, Object>() {

			@Override
			public Object generate(String input) {
				return SafeParser.toByte(input, (byte) 0);
			}
		
		}),
		
		SHORT ('s', Short.class, new Generator<String, Object>() {

			@Override
			public Object generate(String input) {
				return SafeParser.toShort(input, (short) 0);
			}
		
		}),
		
		INT ('i', Integer.class, new Generator<String, Object>() {

			@Override
			public Object generate(String input) {
				return SafeParser.toInt(input, 0);
			}
		
		}),
		
		LONG ('l', Long.class, new Generator<String, Object>() {

			@Override
			public Object generate(String input) {
				return SafeParser.toLong(input, 0);
			}
		
		}),
		
		FLOAT ('f', Float.class, new Generator<String, Object>() {

			@Override
			public Object generate(String input) {
				return SafeParser.toFloat(input, 0);
			}
			
		}),
		
		DOUBLE ('d', Double.class, new Generator<String, Object>() {

			@Override
			public Object generate(String input) {
				return SafeParser.toDouble(input, 0);
			}
			
		});
		
		private final char prefix;
		private final Class<?> clazz;
		private final Generator<String, Object> parser;
		
		private DataType(char prefix, Class<?> clazz, Generator<String, Object> parser) {
			this.prefix = prefix;
			this.clazz = clazz;
			this.parser = parser;
		}

		public char getPrefix() {
			return prefix;
		}

		public Class<?> getDataClass() {
			return clazz;
		}
		
		public Object parse(String input) {
			return parser.generate(input);
		}
		
		public static DataType getByPrefix(char prefix) {
			for (DataType t : DataType.values()) {
				if (t.getPrefix() == prefix) {
					return t;
				}
			}
			
			return BYTE_SEQUENCE;
		}
	}

	public static String toSafeString(String str) {
		return new String(StringUtil.encodeEscapeCharacters(str.toCharArray(),
				ESCAPE_CHAR, ESCAPE_SAFES, ESCAPE_UNSAFES));
	}
	
	public static String toUnsafeString(String str) throws SyntaxException {
		return new String(StringUtil.parseEscapeCharacters(str.toCharArray(),
				ESCAPE_CHAR, ESCAPE_UNICODE_CHAR, ESCAPE_SAFES, ESCAPE_UNSAFES));
	}
	
	public static PlainTypedData read(InputStream input, Supplier<OutputStream> output)
			throws IOException, SyntaxException {
		return read(input, toOutputSupplier(output));
	}
	
	public static PlainTypedData read(InputStream input, OutputSupplier output)
			throws IOException, SyntaxException {
		
		if (input == null) {
			throw new IllegalArgumentException(new NullPointerException());
		}
		
		LinkedList<PTDElement> elements = new LinkedList<PTDElement>();
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
				DataType type = DataType.getByPrefix(toUnsafeString(name.toString()).charAt(0));
				elements.add(new PTDElement(toUnsafeString(name.toString()),
						type.parse(toUnsafeString(value.toString()))));
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
		
		return new PlainTypedData(elements, output);
	}
	
	public static PlainTypedData read(final File file)
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
	
	public static PlainTypedData readWithoutFeedback(final File file)
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
		
		return read(new FileInputStream(file), (OutputSupplier) null);
	}
	
	public PlainTypedData(LinkedList<PTDElement> data, OutputSupplier output) {
		super(output);
		this.data = data;
	}

	public PlainTypedData(Supplier<OutputStream> output) {
		super(output);
		this.data = new LinkedList<PTDElement>();
	}
	
	public PlainTypedData() {
		this(null);
	}
	
	public PlainTypedData(Iterable<PTDElement> data, Supplier<OutputStream> output) {
		this(output);
		
		for (PTDElement e : data) {
			getData().add(e);
		}
	}
	
	public LinkedList<PTDElement> getData() {
		return data;
	}

	public void setData(LinkedList<PTDElement> data) {
		this.data = data;
	}
	
	public PTDElement get(String key) {
		for (PTDElement e : getData()) {
			if (e.getName().equals(key)) {
				return e;
			}
		}
		
		return null;
	}
	
	public boolean contains(String key) {
		return get(key) != null;
	}
	
	public Object getValue(String key, String def) {
		PTDElement element = get(key);
		if (element == null) {
			return def;
		}
		return get(key).getValue();
	}
	
	public Object getValue(String key) {
		return getValue(key, null);
	}
	
	public byte[] getByteSequence(String key, byte[] def) {
		PTDElement element = get(key);
		if (element == null || element.getType() != DataType.BYTE_SEQUENCE) {
			return def;
		}
		return (byte[]) get(key).getValue();
	}
	
	public byte[] getByteSequence(String key) {
		return getByteSequence(key, null);
	}
	
	public Boolean getBoolean(String key, Boolean def) {
		PTDElement element = get(key);
		if (element == null || element.getType() != DataType.BOOLEAN) {
			return def;
		}
		return (Boolean) get(key).getValue();
	}
	
	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}
	
	public Byte getByte(String key, Byte def) {
		PTDElement element = get(key);
		if (element == null || element.getType() != DataType.BYTE) {
			return def;
		}
		return (Byte) get(key).getValue();
	}
	
	public Byte getByte(String key) {
		return getByte(key, null);
	}
	
	public Short getShort(String key, Short def) {
		PTDElement element = get(key);
		if (element == null || element.getType() != DataType.SHORT) {
			return def;
		}
		return (Short) get(key).getValue();
	}
	
	public Short getShort(String key) {
		return getShort(key, null);
	}
	
	public Integer getInteger(String key, Integer def) {
		PTDElement element = get(key);
		if (element == null || element.getType() != DataType.INT) {
			return def;
		}
		return (Integer) get(key).getValue();
	}
	
	public Integer getInteger(String key) {
		return getInteger(key, null);
	}
	
	public Long getLong(String key, Long def) {
		PTDElement element = get(key);
		if (element == null || element.getType() != DataType.LONG) {
			return def;
		}
		return (Long) get(key).getValue();
	}
	
	public Long getLong(String key) {
		return getLong(key, null);
	}
	
	public Float getFloat(String key, Float def) {
		PTDElement element = get(key);
		if (element == null || element.getType() != DataType.FLOAT) {
			return def;
		}
		return (Float) get(key).getValue();
	}
	
	public Float getFloat(String key) {
		return getFloat(key, null);
	}
	
	public Double getDouble(String key, Double def) {
		PTDElement element = get(key);
		if (element == null || element.getType() != DataType.DOUBLE) {
			return def;
		}
		return (Double) get(key).getValue();
	}
	
	public Double getDouble(String key) {
		return getDouble(key, null);
	}
	
	public DataType getType(String key) {
		PTDElement element = get(key);
		if (element == null) {
			return null;
		}
		return get(key).getType();
	}
	
	public Class<?> getJavaType(String key) {
		PTDElement element = get(key);
		if (element == null) {
			return null;
		}
		return get(key).getType().getDataClass();
	}
	
	public void setValue(String key, Object value) {
		PTDElement element = get(key);
		if (element != null && element.getType().getDataClass() == value.getClass()) {
			element.setValue(value);
		}
	}
	
	public synchronized void add(PTDElement element) {
		getData().add(element);
	}
	
	public void addValue(String key, Object value) {
		add(new PTDElement(key, value));
	}
	
	public void putValue(String key, Object value) {
		PTDElement element = get(key);
		if (element == null) {
			addValue(key, value);
		} else {
			element.setValue(value);
		}
	}

	@Override
	public void save(OutputStream output) throws IOException {
		byte[] encoded;
		for (PTDElement e : getData()) {
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
		PlainTypedData other = (PlainTypedData) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

}
