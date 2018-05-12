package ru.windcorp.tge2.util.dataio;

import java.io.IOException;

public class ByteArrayDataWriter extends AbstractByteDataWriter {

	private final byte[] array;
	private int index = 0;
	
	public ByteArrayDataWriter(byte[] array) {
		this.array = array;
	}

	@Override
	public DataWriter writeByte(byte x) throws IOException {
		if (index >= array.length) throw new IOException("Array out of bounds");
		array[index++] = x;
		return this;
	}

	@Override
	public DataWriter writeRawArray(byte[] x, int begin, int length) throws IOException {
		if (index + length >= array.length) throw new IOException("Array out of bounds");
		System.arraycopy(x, begin, this.array, index, length);
		index += length;
		return this;
	}
	
	public byte[] getArray() {
		return array;
	}

}
