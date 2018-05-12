package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.util.exceptions.ImplementationMistakeException;

public abstract class SBDValue<T> extends SBDElement {
	
	private final Class<T> typeClass;
	
	public SBDValue(String name, Class<T> typeClass) {
		super(name);
		this.typeClass = typeClass;
	}

	public Class<T> getTypeClass() {
		return typeClass;
	}

	public boolean accepts(Object obj) {
		return getTypeClass().isInstance(obj);
	}
	
	@Override
	public int read(InputStream input, StructuredByteData meta, String argument) throws IOException {
		return read(input, argument);
	}
	
	public abstract int read(InputStream input, String argument) throws IOException;

	@Override
	public void write(OutputStream output, StructuredByteData meta) throws IOException {
		write(output);
	}
	
	public abstract void write(OutputStream output) throws IOException;

	@Override
	public int skip(InputStream input, StructuredByteData meta) throws IOException {
		return skip(input);
	}
	
	public int skip(InputStream input) throws IOException {
		if (isSizeFixed()) {
			return getCurrentLength();
		}
		
		throw new ImplementationMistakeException("Type " + getFullTypeName() + " does not have a fixed size but skip(InputStream) is not implemented");
	}

	public int readAndGet(InputStream input, StructuredByteData meta, String arg, T[] output) throws IOException {
		if (output.length < 1)
			throw new IllegalArgumentException("Output array must have at least one element");
		
		if (isTemplate()) {
			synchronized (this) {
				int result = read(input, meta, arg);
				output[0] = getValue(); // c rulez
				setValue(null);
				return result;
			}
		}
		
		return 0;
	}
	
	public void setAndWrite(OutputStream output, StructuredByteData meta, T input) throws IOException {
		if (isTemplate()) {
			synchronized (this) {
				setValue(input);
				write(output, meta);
				setValue(null);
			}
		}
	}
	
	public int getLengthOf(T input) {
		if (isTemplate()) {
			synchronized (this) {
				try {
					setValue(input);
					return getCurrentLength();
				} finally {
					setValue(null);
				}
			}
		}
		
		return 0;
	}

	@Override
	public abstract SBDValue<T> newCopy(String name);
	
	public abstract T getValue();
	public abstract void setValue(T value);
	
	@Override
	public String toString() {
		if (isTemplate()) return getTypeName();
		return getTypeName() + " " + (getName().isEmpty() ? "<name empty>" : getName()) + " = " + valueToString();
	}
	
	@Override
	public String toFancyString() {
		return getTypeName() + " (" + getClass().getSimpleName() + "<" + getTypeClass().getSimpleName() + ">) " + (getName().isEmpty() ? "<name empty>" : getName()) + " = " + valueToString();
	}
	
	public String valueToString(T value) {
		return value.toString();
	}
	
	public String valueToString() {
		return valueToString(getValue());
	}
	
}
