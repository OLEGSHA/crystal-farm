package ru.windcorp.tge2.util.dataio;

import java.io.IOException;

import ru.windcorp.tge2.util.grh.Loadable;

public abstract class AbstractDataReader implements DataReader {
	
	@Override
	public int readUnsignedByte() throws IOException {
		return 0xFF & readByte();
	}

	@Override
	public byte[] readArray() throws IOException {
		return readArray(null);
	}
	
	@Override
	public byte[] readArray(byte[] array) throws IOException {
		return readArray(array, DataIOUtils.defaultMaxArrayLength);
	}
	
	@Override
	public byte[] readArray(int maxlength) throws IOException {
		return readArray(null, maxlength);
	}

	@Override
	public boolean supportsSkip() {
		return false;
	}

	@Override
	public void skip(long bytes) throws IOException {
		throw new UnsupportedOperationException("This DataReader does not support skipping");
	}

	@Override
	public <T extends Loadable<DataReader>> T read(T readable) throws IOException {
		readable.load(this);
		return readable;
	}
	
	@Override
	public byte[] fillArray(byte[] array) throws IOException {
		return fillArray(array, 0, array.length);
	}
	
}
