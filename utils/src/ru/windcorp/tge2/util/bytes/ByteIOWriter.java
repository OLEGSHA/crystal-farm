package ru.windcorp.tge2.util.bytes;

import static ru.windcorp.tge2.util.bytes.ByteIOUtil.*;

public class ByteIOWriter {
	
	public final byte[] bytes;
	private int pos;
	
	public ByteIOWriter(byte[] bytes) {
		this.bytes = bytes;
		resetPosition();
	}
	
	public int getPosition() {
		return pos;
	}
	
	public void setPosition(int pos) {
		synchronized (this) {
			this.pos = pos;
		}
	}
	
	public void incPosition(int amount) {
		setPosition(getPosition() + amount);
	}
	
	public void resetPosition() {
		setPosition(0);
	}
	
	public void writeByte(byte b) {
		synchronized (this) {
			checkPos(BYTES_BYTE);
			this.bytes[getPosition()] = b;
			incPosition(BYTES_BYTE);
		}
	}
	
	public void writeBytes(byte[] bytes, int pos, int length) {
		synchronized (this) {
			checkPos(length);
			System.arraycopy(bytes, pos, this.bytes, getPosition(), length);
			incPosition(length);
		}
	}
	
	public void writeBytes(byte[] bytes) {
		writeBytes(bytes, 0, bytes.length);
	}
	
	public void writeBytesWithLength(byte[] bytes, int pos, int length) {
		synchronized (this) {
			writeInt(length);
			writeBytes(bytes, pos, length);
		}
	}
	
	public void writeBytesWithLength(byte[] bytes) {
		writeBytesWithLength(bytes, 0, bytes.length);
	}
	
	public void writeString(String string) {
		writeBytesWithLength(string.getBytes(UTF_8));
	}
	
	public void writeUnicodeString(String string) {
		writeBytesWithLength(string.getBytes(UTF_16));
	}
	
	public void writeShort(short s) {
		synchronized (this) {
			checkPos(BYTES_SHORT);
			ByteIOUtil.writeShort(this.bytes, getPosition(), s);
			incPosition(BYTES_SHORT);
		}
	}
	
	public void writeInt(int i) {
		synchronized (this) {
			checkPos(BYTES_INT);
			ByteIOUtil.writeInt(this.bytes, getPosition(), i);
			incPosition(BYTES_INT);
		}
	}
	
	public void writeLong(long l) {
		synchronized (this) {
			checkPos(BYTES_LONG);
			ByteIOUtil.writeLong(this.bytes, getPosition(), l);
			incPosition(BYTES_LONG);
		}
	}

	private void checkPos(int bytes) {
		if (getPosition() + bytes > this.bytes.length) {
			throw new IllegalStateException("Too little elements (" + this.bytes.length + " available; " + (getPosition() + bytes) + " required)");
		}
	}
	
}
