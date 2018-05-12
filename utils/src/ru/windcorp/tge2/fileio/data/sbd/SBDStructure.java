package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ru.windcorp.tge2.util.IndentedStringBuilder;
import ru.windcorp.tge2.util.NumberUtil;
import ru.windcorp.tge2.util.StringUtil;
import static ru.windcorp.tge2.util.bytes.ByteIOUtil.*;

public class SBDStructure extends SBDElement {
	
	public static final char PATH_SEPARATOR = '.';
	private static final char[] PATH_SEPARATOR_ARRAY = new char[] { PATH_SEPARATOR };
	
	private final Map<String, SBDElement> elements = Collections.synchronizedMap(new HashMap<String, SBDElement>());

	public SBDStructure(String name) {
		super(name);
	}
	
	@Override
	public SBDElement newCopy(String name) {
		return new SBDStructure(name);
	}

	@Override
	public int getCurrentLength() {
		int length = BYTES_INT;
		for (SBDElement e : getElements()) {
			// byte type, int name length, byte... name, byte... body
			length += BYTES_BYTE + BYTES_INT + e.getName().length() + e.getCurrentLength();
		}
		return length;
	}
	
	/*
	 * Syntax:
	 *             |    |name length|        |        |
	 *  length = n |type|    = m    |  name  |  body  |
	 *  00 00 00 00  00  00 00 00 00 00 .. 00 00 .. 00 ..
	 *             |                \___m____/           |
	 *             \__________________n__________________/
	 * \___________________n_+_BYTES_INT__________________/
	 */

	@Override
	public int read(InputStream input, StructuredByteData meta, String argument) throws IOException {
		int length = readInt(input);
		int type = 0;
		String name = null;
		int readBytes = 0;
		SBDElement element = null;
		
		while (readBytes < length) {
			type = input.read();
			name = readString(input);
			element = meta.create(type, name);
			
			if (element == null) {
				throw new IOException("Element " + getName() + PATH_SEPARATOR + name +
						" has unknown type 0x" + NumberUtil.toUnsignedHexString((byte) type));
			}
			
			readBytes += BYTES_BYTE + BYTES_INT + name.length() + element.read(input, meta, null);
			put(element);
		}
		
		return length + BYTES_INT;
	}

	@Override
	public void write(OutputStream output, StructuredByteData meta) throws IOException {
		writeInt(output, getCurrentLength() - BYTES_INT);
		
		for (SBDElement e : getElements()) {
			output.write(meta.getByteByTypeOrComplain(e));
			writeString(output, e.getName());
			e.write(output, meta);
		}
	}

	@Override
	public int skip(InputStream input, StructuredByteData meta) throws IOException {
		int length = readInt(input);
		input.skip(length);
		return length + BYTES_INT;
	}
	
	public static int find(Iterator<char[]> paths,
			Iterator<String> args,
			int iteration,
			char[] ownName,
			InputStream input,
			SBDStructure newParent,
			
			StructuredByteData meta,
			int structureType)
					throws IOException {
		
		int length = readInt(input);
		int readBytes = 0;
		int recursiveResult = 0;
		
		int indexOfCurrentSeparator = -1,
				indexOfNextSeparator = -1;
		
		byte elementType;
		char[] elementName;
		char[] path;
		String arg = null;
		
		// For every element in this structure
		elementLoop:
		while (readBytes < length) {
			if (paths.hasNext()) {
				return -1;
			}
			
			elementType = (byte) input.read();
			elementName = readString(input).toCharArray();
			readBytes += BYTES_BYTE + elementName.length;
			
			// For every path
			while (paths.hasNext()) {
				path = paths.next();
				arg = args.next();
				
				// Index of the separator splitting path into ownName and relative path
				indexOfCurrentSeparator = StringUtil.indexFromBeginning(path, PATH_SEPARATOR, iteration);
				
				if (
						// Check whether the current path can potentially contain ownName
						indexOfCurrentSeparator != -1
						
						// Compare structure full name in path to ownName
						&& StringUtil.equalsPart(path, ownName, 0, indexOfCurrentSeparator)
						
						// Compare elementName to the next path element in path
						&& StringUtil.equalsPart(path, elementName, indexOfCurrentSeparator + 1, Math.max(
								
								// Index of the separator splitting path into full path of the current element and its subelements
								(indexOfNextSeparator = StringUtil.indexFromBeginning(path, PATH_SEPARATOR, iteration + 1)),
								
								elementName.length))
						) {
					
					// If current element is the final one in the path
					if (indexOfNextSeparator == -1) {
						SBDElement element = meta.create(elementType, new String(elementName));
						readBytes += element.read(input, meta, arg);
						newParent.put(element);
						paths.remove();
						args.remove();
						continue elementLoop; // the current element has been processed
					}
					
					// The current element is a structure
					if (elementType == structureType) {
						recursiveResult = find(paths,
								args,
								iteration + 1,
								StringUtil.join(ownName, PATH_SEPARATOR_ARRAY, elementName),
								input,
								newParent,
								meta,
								structureType);
						
						if (recursiveResult == -1) {
							return -1; // Job done, quitting recursion
						}
						
						readBytes += recursiveResult;
						continue elementLoop; // the current element has been processed
					}
					
					/* Something gone wrong - there are subelements to the current element
					 * but the current element is not a structure
					 * Example:
					 * "root.substruct.error is not a structure (found type 0x42, 0x00 expected)
					 * while path root.substruct.error.element implies otherwise" 
					 */ 
					throw new IOException(new String(ownName) + PATH_SEPARATOR + new String(elementName) +
							" is not a structure (found type 0x" + NumberUtil.toUnsignedHexString(elementType) +
							", 0x" + NumberUtil.toUnsignedHexString((byte) structureType) + " expected) while path " +
							new String(path) + " implies otherwise");
				}
				
				// Current path does not denote current element
			}
			
			// No paths denote this element, skipping
			SBDElement e = meta.getTypeByByte(elementType);
			if (e == null) {
				throw new IOException("Element " + new String(ownName) + PATH_SEPARATOR + new String(elementName) +
						" has unknown type 0x" + NumberUtil.toUnsignedHexString(elementType));
			}
			
			readBytes += e.skip(input, meta);
		}
		
		if (paths.hasNext()) {
			return -1;
		}
		
		return length + BYTES_INT;
	}
	
	@Override
	public boolean isSizeFixed() {
		return false;
	}
	
	public Map<String, SBDElement> getElementMap() {
		return elements;
	}
	
	public Collection<SBDElement> getElements() {
		return getElementMap().values();
	}
	
	public int getElementCount() {
		return getElementMap().size();
	}
	
	public boolean isEmpty() {
		return getElementMap().isEmpty();
	}

	public SBDStructure put(SBDElement element) {
		getElementMap().put(element.getName(), element);
		return this;
	}
	
	public boolean contains(String name) {
		return getElementMap().containsKey(name);
	}
	
	public boolean containsAll(String... names) {
		for (String n : names) {
			if (!contains(n)) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean contains(SBDElement element) {
		return contains(element.getName());
	}
	
	public SBDStructure remove(String name) {
		getElementMap().remove(name);
		return this;
	}
	
	public SBDStructure remove(SBDElement element) {
		return remove(element.getName());
	}
	
	public SBDElement getElement(String name) {
		return getElementMap().get(name);
	}
	
	public SBDElement getElement(String name, SBDElement def) {
		SBDElement found = getElement(name);
		return found == null ? def : found;
	}
	
	public SBDStructure getStructure(String name, SBDStructure def) {
		SBDElement found = getElement(name);
		return found instanceof SBDStructure ? (SBDStructure) found : def;
	}
	
	public SBDStructure getStructure(String name) {
		return getStructure(name, null);
	}
	
	public SBDStructure getStructureOrNew(String name) {
		return getStructure(name, new SBDStructure(name));
	}
	
	public SBDStructure getStructureOrAdd(String name, boolean replaceOthers) {
		SBDElement found = getElement(name);
		if (found instanceof SBDStructure) {
			return (SBDStructure) found;
		}
		
		if (found != null && !replaceOthers) {
			throw new IllegalStateException("Element " + name + " already exists but is not a structure");
		}
		
		found = new SBDStructure(name);
		put(found);
		
		return (SBDStructure) found;
	}
	
	@SuppressWarnings("unchecked")
	public <T> SBDValue<T> getValue(String name, Class<T> clazz) {
		SBDElement found = getElement(name);
		if (found instanceof SBDValue<?>) { // implicit null-check
			if (clazz != null
					&& ((SBDValue<?>) found).getTypeClass() == clazz) {
				return (SBDValue<T>) found; // (Unchecked conversion) just checked that
			}
		}
		
		return null;
	}
	
	public <T> SBDValue<T> getValue(String name, SBDValue<T> def) {
		SBDValue<T> found = getValue(name, def.getTypeClass());
		return found == null ? def : found;
	}
	
	public SBDValue<?> getValue(String name) {
		return getValue(name, (Class<?>) null);
	}
	
	public <T> T get(String name, Class<T> clazz, T def) {
		SBDValue<T> value = getValue(name, clazz);
		return value == null ? def : value.getValue();
	}

	public <T> T get(String name, Class<T> clazz) {
		return get(name, clazz, null);
	}
	
	public Object get(String name) {
		return get(name, null, null);
	}

	@SuppressWarnings("unchecked")
	public <T> SBDStructure put(String name, T value) {
		SBDValue<?> found = getValue(name);
		if (found == null) {
			found = StructuredByteData.create(value.getClass(), name);
			put(found);
		} else if (found.getTypeClass() != value.getClass()) {
			return this;
		}
		
		((SBDValue<T>) found).setValue(value);
		return this;
	}
	
	@Override
	public String toString() {
		Iterator<SBDElement> elements = getElements().iterator();
		if (elements.hasNext()) {
			StringBuilder sb = new StringBuilder();
			
			sb.append(getName());
			sb.append(" { ");
			
			sb.append(elements.next());
			
			while (elements.hasNext()) {
				sb.append("; ");
				sb.append(elements.next());
			}
			
			sb.append(" }");
			
			return sb.toString();
		}
		return getName() + " {}";
	}
	
	@Override
	public String toFancyString() {
		IndentedStringBuilder sb = new IndentedStringBuilder('\t', 1);
		toFancyString(sb);
		return sb.toString();
	}
	
	public void toFancyString(IndentedStringBuilder sb) {
		if (isEmpty()) {
			sb.appendRaw("structure ").append(getName()).appendRaw(" {}");
			return;
		}
		
		sb.appendRaw("structure ").append(getName()).appendRaw(" {").indent();
		
		for (SBDElement e : getElements()) {
			appendElement(sb, e);
		}
		
		sb.unindent().breakLine().appendRaw("}");
	}
	
	private static void appendElement(IndentedStringBuilder sb, SBDElement e) {
		sb.breakLine();
		if (e instanceof SBDStructure) {
			((SBDStructure) e).toFancyString(sb);
		} else {
			sb.append(e.toFancyString());
		}
	}
	
	@Override
	public SBDElement getPathElement(String pathname) {
		return getElement(pathname);
	}
	
}
