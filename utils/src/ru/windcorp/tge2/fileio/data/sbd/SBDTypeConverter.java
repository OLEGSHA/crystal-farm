package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import static ru.windcorp.tge2.util.bytes.ByteIOUtil.*;

public class SBDTypeConverter {
	
	private SBDElement[] table;
	
	public SBDElement[] getTable() {
		return table;
	}
	
	public int getTableSize() {
		return table.length;
	}

	public SBDElement getType(int type) {
		if (type >= getTableSize()) {
			return null;
		}
		
		return getTable()[type];
	}
	
	public int getByte(SBDElement element) {
		for (int index = 0; index < getTableSize(); ++index) {
			if (getTable()[index].getFullTypeName().equals(element.getFullTypeName())) {
				return index;
			}
		}
		
		return -1;
	}
	
	public void read(InputStream is) throws IOException {
		int recordCount = is.read();
		table = new SBDElement[recordCount];
		
		String name;
		for (int i = 0; i < recordCount; ++i) {
			getTable()[i] = StructuredByteData.getByName(name = readString(is));
			if (getTable()[i] == null) {
				throw new IOException("Type name \"" + name + "\" described in type converter table was not found in type registry. Is SBD module up-to-date?");
			}
		}
	}
	
	public void write(OutputStream os) throws IOException {
		os.write(getTableSize());
		
		for (SBDElement e : getTable()) {
			writeString(os, e.getFullTypeName());
		}
	}

	public void createDefault() {
		Collection<SBDElement> elements = StructuredByteData.getRegisteredElements();
		table = new SBDElement[elements.size()];
		
		int i = 0;
		for (SBDElement e : elements) {
			getTable()[i++] = e;
		}
	}
	
	@Override
	public String toString() {
		if (getTable().length == 0) {
			return "< empty >";
		}
		
		StringBuilder sb = new StringBuilder(String.format("%8h = %s", 0, getTable()[0].getFullTypeName()));
		
		for (int i = 1; i < getTable().length; ++i) {
			sb.append(String.format("; %8h = %s", i, getTable()[i].getFullTypeName()));
		}
		
		return sb.toString();
	}

}