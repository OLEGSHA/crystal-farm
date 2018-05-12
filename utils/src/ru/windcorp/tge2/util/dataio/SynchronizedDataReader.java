package ru.windcorp.tge2.util.dataio;

import java.io.IOException;
import java.nio.charset.Charset;

import ru.windcorp.tge2.util.grh.Loadable;

public class SynchronizedDataReader implements DataReader {

	private final DataReader reader;
	private final Object lock;

	public SynchronizedDataReader(DataReader reader, Object lock) {
		this.reader = reader;
		this.lock = lock;
	}

	public DataReader getReader() {
		return reader;
	}

	public Object getLock() {
		return lock;
	}

	@Override
	public byte readByte() throws IOException {
		synchronized (lock) {
			return reader.readByte();
		}
	}

	@Override
	public short readShort() throws IOException {
		synchronized (lock) {
			return reader.readShort();
		}
	}

	@Override
	public int readInt() throws IOException {
		synchronized (lock) {
			return reader.readInt();
		}
	}

	@Override
	public long readLong() throws IOException {
		synchronized (lock) {
			return reader.readLong();
		}
	}

	@Override
	public char readChar() throws IOException {
		synchronized (lock) {
			return reader.readChar();
		}
	}

	@Override
	public boolean readBoolean() throws IOException {
		synchronized (lock) {
			return reader.readBoolean();
		}
	}

	@Override
	public float readFloat() throws IOException {
		synchronized (lock) {
			return reader.readFloat();
		}
	}

	@Override
	public double readDouble() throws IOException {
		synchronized (lock) {
			return reader.readDouble();
		}
	}

	@Override
	public byte[] readArray(byte[] output) throws IOException {
		synchronized (lock) {
			return reader.readArray(output);
		}
	}

	@Override
	public byte[] readArray() throws IOException {
		synchronized (lock) {
			return reader.readArray();
		}
	}

	@Override
	public byte[] readArray(byte[] output, int maxLength) throws IOException {
		synchronized (lock) {
			return reader.readArray(output, maxLength);
		}
	}

	@Override
	public byte[] readArray(int maxLength) throws IOException {
		synchronized (lock) {
			return reader.readArray(maxLength);
		}
	}

	@Override
	public String readString(Charset ch) throws IOException {
		synchronized (lock) {
			return reader.readString(ch);
		}
	}

	@Override
	public String readString() throws IOException {
		synchronized (lock) {
			return reader.readString();
		}
	}

	@Override
	public boolean supportsSkip() {
		synchronized (lock) {
			return reader.supportsSkip();
		}
	}

	@Override
	public void skip(long bytes) throws IOException {
		synchronized (lock) {
			reader.skip(bytes);
		}
	}

	@Override
	public <T extends Loadable<DataReader>> T read(T readable) throws IOException {
		synchronized (lock) {
			return reader.read(readable);
		}
	}

	@Override
	public byte[] fillArray(byte[] array, int begin, int length) throws IOException {
		synchronized (lock) {
			return reader.fillArray(array, begin, length);
		}
	}

	@Override
	public byte[] fillArray(byte[] array) throws IOException {
		synchronized (lock) {
			return reader.fillArray(array);
		}
	}

	@Override
	public int readUnsignedByte() throws IOException {
		synchronized (lock) {
			return reader.readUnsignedByte();
		}
	}
	
}
