package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class SBDElement {
	
	private String name;
	private transient String typeName = null;
	
	public SBDElement(String name) {
		this.name = name;
	}
	
	public String getTypeName() {
		if (typeName == null) {
			typeName = getClass().getSimpleName();
			if (typeName.startsWith("SBD")) {
				typeName = typeName.substring("SBD".length(), typeName.length());
			}
			if (typeName.endsWith("Value")) {
				typeName = typeName.substring(0, typeName.length() - "Value".length());
			}
		}
		
		return typeName;
	}
	
	public String getFullTypeName() {
		return getClass().getSimpleName();
	}

	@SuppressWarnings("static-method") // To be overridden
	public boolean isSizeFixed() {
		return true;
	}

	public abstract SBDElement newCopy(String name);

	public String getName() {
		return name;
	}
	
	public boolean isTemplate() {
		return getName() == null;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toFancyString() {
		return toString();
	}
	
	public abstract int read(InputStream input, StructuredByteData meta, String argument) throws IOException;
	public abstract void write(OutputStream output, StructuredByteData meta) throws IOException;
	public abstract int getCurrentLength();
	public abstract int skip(InputStream input, StructuredByteData meta) throws IOException;
	
	@SuppressWarnings("static-method") // To be overridden
	public SBDElement getPathElement(String pathname) {
		return null;
	}

}
