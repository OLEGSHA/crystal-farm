package ru.windcorp.tge2.util.dataio;

import java.io.IOException;
import java.nio.charset.Charset;

import ru.windcorp.tge2.util.grh.Saveable;

public class SynchronizedDataWriter implements DataWriter {

	private final DataWriter writer;
	private final Object lock;
	
	public SynchronizedDataWriter(DataWriter writer, Object lock) {
		this.writer = writer;
		this.lock = lock;
	}

	@Override
	public DataWriter writeByte(byte x) throws IOException {
		synchronized (lock) {
			writer.writeByte(x);
		}
		return this;
	}

	@Override
	public DataWriter writeShort(short x) throws IOException {
		synchronized (lock) {
			writer.writeShort(x);
		}
		return this;
	}

	@Override
	public DataWriter writeInt(int x) throws IOException {
		synchronized (lock) {
			writer.writeInt(x);
		}
		return this;
	}

	@Override
	public DataWriter writeLong(long x) throws IOException {
		synchronized (lock) {
			writer.writeLong(x);
		}
		return this;
	}

	@Override
	public DataWriter writeChar(char x) throws IOException {
		synchronized (lock) {
			writer.writeChar(x);
		}
		return this;
	}

	@Override
	public DataWriter writeBoolean(boolean x) throws IOException {
		synchronized (lock) {
			writer.writeBoolean(x);
		}
		return this;
	}

	@Override
	public DataWriter writeFloat(float x) throws IOException {
		synchronized (lock) {
			writer.writeFloat(x);
		}
		return this;
	}

	@Override
	public DataWriter writeDouble(double x) throws IOException {
		synchronized (lock) {
			writer.writeDouble(x);
		}
		return this;
	}

	@Override
	public DataWriter writeArray(byte[] x, int begin, int length) throws IOException {
		synchronized (lock) {
			writer.writeArray(x, begin, length);
		}
		return this;
	}

	@Override
	public DataWriter writeArray(byte[] x) throws IOException {
		synchronized (lock) {
			writer.writeArray(x);
		}
		return this;
	}

	@Override
	public DataWriter writeString(String str, Charset ch) throws IOException {
		synchronized (lock) {
			writer.writeString(str, ch);
		}
		return this;
	}

	@Override
	public DataWriter writeString(String str) throws IOException {
		synchronized (lock) {
			writer.writeString(str);
		}
		return this;
	}

	@Override
	public DataWriter write(Saveable<DataWriter> writable) throws IOException {
		synchronized (lock) {
			writer.write(writable);
		}
		return this;
	}

	@Override
	public DataWriter writeRawArray(byte[] x, int begin, int length) throws IOException {
		synchronized (lock) {
			writer.writeRawArray(x, begin, length);
		}
		return this;
	}

	@Override
	public DataWriter writeRawArray(byte[] x) throws IOException {
		synchronized (lock) {
			writer.writeRawArray(x);
		}
		return this;
	}
	
}
