package ru.windcorp.tge2.util.perf;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DataBuffer implements DataInput, DataOutput {
	
	private final BooleanQueue booleanQueue;
	private final ByteQueue byteQueue;
	private final ShortQueue shortQueue;
	private final CharQueue charQueue;
	private final IntegerQueue integerQueue;
	private final LongQueue longQueue;
	private final FloatQueue floatQueue;
	private final DoubleQueue doubleQueue;
	private final ByteArrayQueue byteArrayQueue;
	private final StringQueue stringQueue;

	public DataBuffer(int capacity) {
		this.booleanQueue = new BooleanQueue(capacity);
		this.byteQueue = new ByteQueue(capacity);
		this.shortQueue = new ShortQueue(capacity);
		this.charQueue = new CharQueue(capacity);
		this.integerQueue = new IntegerQueue(capacity);
		this.longQueue = new LongQueue(capacity);
		this.floatQueue = new FloatQueue(capacity);
		this.doubleQueue = new DoubleQueue(capacity);
		this.byteArrayQueue = new ByteArrayQueue(capacity);
		this.stringQueue = new StringQueue(capacity);
	}

	@Override
	public void write(int b) throws IOException {
		byteQueue.add((byte) (b & 0xFF));
	}

	@Override
	public void write(byte[] b) throws IOException {
		byteArrayQueue.add(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		byteArrayQueue.add(b, off, len);
	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		booleanQueue.add(v);
	}

	@Override
	public void writeByte(int v) throws IOException {
		byteQueue.add((byte) (v & 0xFF));
	}

	@Override
	public void writeBytes(String s) throws IOException {
		for (int i = 0; i < s.length(); ++i) {
			writeByte(s.charAt(i));
		}
	}

	@Override
	public void writeChar(int v) throws IOException {
		charQueue.add((char) (v & 0xFFFF));
	}

	@Override
	public void writeChars(String s) throws IOException {
		for (int i = 0; i < s.length(); ++i) {
			writeChar(s.charAt(i));
		}
	}

	@Override
	public void writeDouble(double v) throws IOException {
		doubleQueue.add(v);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		floatQueue.add(v);
	}

	@Override
	public void writeInt(int v) throws IOException {
		integerQueue.add(v);
	}

	@Override
	public void writeLong(long v) throws IOException {
		longQueue.add(v);
	}

	@Override
	public void writeShort(int v) throws IOException {
		shortQueue.add((short) (v & 0xFFFF));
	}

	@Override
	public void writeUTF(String s) throws IOException {
		stringQueue.add(s);
	}

	@Override
	public boolean readBoolean() throws IOException {
		return booleanQueue.get();
	}

	@Override
	public byte readByte() throws IOException {
		return byteQueue.get();
	}

	@Override
	public char readChar() throws IOException {
		return charQueue.get();
	}

	@Override
	public double readDouble() throws IOException {
		return doubleQueue.get();
	}

	@Override
	public float readFloat() throws IOException {
		return floatQueue.get();
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		System.arraycopy(byteArrayQueue.get(), 0, b, 0, b.length);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		System.arraycopy(byteArrayQueue.get(), 0, b, off, len);
	}

	@Override
	public int readInt() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String readLine() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long readLong() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short readShort() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String readUTF() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int readUnsignedByte() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int readUnsignedShort() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int skipBytes(int n) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
