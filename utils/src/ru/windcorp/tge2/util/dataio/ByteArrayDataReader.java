package ru.windcorp.tge2.util.dataio;

import java.io.IOException;

public class ByteArrayDataReader extends AbstractByteDataReader {
	
	private final byte[] array;
	private int index = 0;

	public ByteArrayDataReader(byte[] array) {
		this.array = array;
	}

	@Override
	public byte readByte() throws IOException {
		try {
			return array[index++];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IOException("Array out of bounds", e);
		}
	}

	@Override
	public byte[] fillArray(byte[] array, int begin, int length) throws IOException {
		try {
			System.arraycopy(this.array, index, array, begin, length);
			index += array.length;
			return array;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public boolean hasMore() {
		return index < array.length;
	}
	
	@Override
	public void skip(long bytes) throws IOException {
		if (index + bytes > Integer.MAX_VALUE) {
			index = Integer.MAX_VALUE;
		} else {
			index += bytes;
		}
	}

}
