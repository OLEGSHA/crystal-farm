package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ru.windcorp.tge2.util.NumberUtil;
import static ru.windcorp.tge2.util.bytes.ByteIOUtil.*;

@SuppressWarnings("unchecked")
public class SBDArray<T> extends SBDElement implements Iterable<T> {

	private final List<Object> list = Collections.synchronizedList(new ArrayList<Object>());
	private SBDValue<T> discriminator = null;
	
	public SBDArray(String name) {
		super(name);
	}

	@Override
	public SBDElement newCopy(String name) {
		return new SBDArray<Object>(name);
	}

	@Override
	public int read(InputStream input, StructuredByteData meta, String argument) throws IOException {
		int length = readInt(input) + BYTES_INT;
		
		int type = input.read();
		SBDElement element = meta.getTypeByByte(type);
		
		if (element == null) {
			throw new IOException("Array element type 0x" + NumberUtil.toUnsignedHexString((byte) type)
					+ " does not exist");
		}
		
		if (!(element instanceof SBDValue<?>)) {
			throw new IOException("Array element type 0x" + NumberUtil.toUnsignedHexString((byte) type)
					+ "(" + element + ") is not a value");
		}
		
		setDiscriminator((SBDValue<T>) element);
		
		int elements;
		if (getDiscriminator().isSizeFixed()) {
			elements = length / getDiscriminator().getCurrentLength();
		} else {
			elements = readInt(input);
		}
		
		T[] array = (T[]) new Object[1];
		for (int i = elements - 1; i >= 0; --i) {
			getDiscriminator().readAndGet(input, meta, null, array);
			set(i, array[0]);
		}
		
		return length;
	}

	@Override
	public void write(OutputStream output, StructuredByteData meta) throws IOException {
		writeInt(output, getCurrentLength());
		output.write(meta.getByteByTypeOrComplain(getDiscriminator()));
		
		if (!getDiscriminator().isSizeFixed()) {
			writeInt(output, getRawList().size());
		}
		
		for (T t : this) {
			getDiscriminator().setAndWrite(output, meta, t);
		}
	}
	
	@Override
	public boolean isSizeFixed() {
		return false;
	}

	@Override
	public int getCurrentLength() {
		if (getDiscriminator().isSizeFixed()) {
			return BYTES_INT + BYTES_BYTE + getDiscriminator().getCurrentLength() * size();
		}
		
		int length = BYTES_INT + BYTES_BYTE + BYTES_INT;
		
		for (T t : this) {
			length += getDiscriminator().getLengthOf(t);
		}
		
		return length;
	}

	@Override
	public int skip(InputStream input, StructuredByteData meta) throws IOException {
		int length = readInt(input);
		input.skip(length);
		return BYTES_INT + length;
	}

	public SBDValue<T> getDiscriminator() {
		return discriminator;
	}

	protected void setDiscriminator(SBDValue<T> discriminator) {
		this.discriminator = discriminator;
	}

	protected List<Object> getRawList() {
		return list;
	}
	
	public List<T> getList() {
		return (List<T>) getRawList();
	}

	public void add(int index, T value) {
		typeCheck(value);
		list.add(index, value);
	}

	public boolean add(T value) {
		typeCheck(value);
		return list.add(value);
	}

	public void clear() {
		list.clear();
	}

	public T get(int index) {
		return (T) list.get(index);
	}
	
	@Override
	public Iterator<T> iterator() {
		return (Iterator<T>) list.iterator();
	}

	public Object remove(int index) {
		return list.remove(index);
	}

	public boolean remove(Object value) {
		return list.remove(value);
	}

	public T set(int index, T value) {
		typeCheck(value);
		return (T) list.set(index, value);
	}

	public int size() {
		return list.size();
	}
	
	protected void typeCheck(Object value) {
		if (!getDiscriminator().accepts(value)) {
			throw new IllegalArgumentException(value.getClass() + " cannot be accepted by type "
					+ getDiscriminator().getFullTypeName());
		}
	}
	
	@Override
	public String toString() {
		if (isTemplate()) return getTypeName();
		
		if (size() == 0) {
			return getDiscriminator().getTypeName() + "[] = (0) {}";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(getDiscriminator().getTypeName());
		sb.append("[] = (");
		sb.append(size());
		sb.append(") { ");
		
		sb.append(get(0));
		
		for (int i = 1; i < size(); ++i) {
			sb.append(", ");
			sb.append(getDiscriminator().valueToString(get(i)));
		}
		
		sb.append(" }");
		
		return sb.toString();
	}

	@Override
	public SBDElement getPathElement(String pathname) {
		int index;
		try {
			index = Integer.parseInt(pathname);
		} catch (NumberFormatException e) {
			return null;
		}
		
		if (index <= size()) return null;
		
		T value;
		try {
			value = get(index);
		} catch (Exception e) {
			return null;
		}
		
		if (value == null) return null;
		
		SBDValue<T> element = getDiscriminator().newCopy(pathname);
		element.setValue(value);
		
		return element;
	}

}
