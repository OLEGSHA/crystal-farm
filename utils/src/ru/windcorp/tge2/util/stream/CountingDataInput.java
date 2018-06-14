package ru.windcorp.tge2.util.stream;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CountingDataInput implements DataInput {
	
	private final CountingInputStream stream;
	private final DataInputStream data;
	
	public CountingDataInput(CountingInputStream stream) {
		this.stream = stream;
		this.data = new DataInputStream(stream);
	}
	
	public CountingDataInput(InputStream stream) {
		this(new CountingInputStream(stream));
	}
	
	protected CountingInputStream getStream() {
		return stream;
	}
	
	protected DataInputStream getData() {
		return data;
	}

	public long getCounter() {
		return stream.getCounter();
	}

	public void resetCounter() {
		stream.resetCounter();
	}

	public void pushCounter() {
		stream.pushCounter();
	}

	public long popCounter() {
		return stream.popCounter();
	}

	public int available() throws IOException {
		return data.available();
	}

	public void close() throws IOException {
		data.close();
	}

	public void mark(int arg0) {
		data.mark(arg0);
	}

	public boolean markSupported() {
		return data.markSupported();
	}

	public int read() throws IOException {
		return data.read();
	}

	public final int read(byte[] b, int off, int len) throws IOException {
		return data.read(b, off, len);
	}

	public final int read(byte[] b) throws IOException {
		return data.read(b);
	}

	public final boolean readBoolean() throws IOException {
		return data.readBoolean();
	}

	public final byte readByte() throws IOException {
		return data.readByte();
	}

	public final char readChar() throws IOException {
		return data.readChar();
	}

	public final double readDouble() throws IOException {
		return data.readDouble();
	}

	public final float readFloat() throws IOException {
		return data.readFloat();
	}

	public final void readFully(byte[] b, int off, int len) throws IOException {
		data.readFully(b, off, len);
	}

	public final void readFully(byte[] b) throws IOException {
		data.readFully(b);
	}

	public final int readInt() throws IOException {
		return data.readInt();
	}

	@Deprecated
	public final String readLine() throws IOException {
		return data.readLine();
	}

	public final long readLong() throws IOException {
		return data.readLong();
	}

	public final short readShort() throws IOException {
		return data.readShort();
	}

	public final String readUTF() throws IOException {
		return data.readUTF();
	}

	public final int readUnsignedByte() throws IOException {
		return data.readUnsignedByte();
	}

	public final int readUnsignedShort() throws IOException {
		return data.readUnsignedShort();
	}

	public void reset() throws IOException {
		data.reset();
	}

	public long skip(long arg0) throws IOException {
		return data.skip(arg0);
	}

	public final int skipBytes(int n) throws IOException {
		return data.skipBytes(n);
	}

}
