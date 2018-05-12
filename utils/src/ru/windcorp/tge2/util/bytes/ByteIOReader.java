package ru.windcorp.tge2.util.bytes;

import static ru.windcorp.tge2.util.bytes.ByteIOUtil.*;

public class ByteIOReader {
	
	public final byte[] bytes;
	private int pos;
	
	public ByteIOReader(byte[] bytes) {
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
	
	public byte readByte() {
		synchronized (this) {
			checkPos(BYTES_BYTE);
			byte result = this.bytes[getPosition()];
			incPosition(BYTES_BYTE);
			return result;
		}
	}
	
	public byte[] readBytes(byte[] dest, int pos, int length) {
		synchronized (this) {
			checkPos(length);
			System.arraycopy(bytes, getPosition(), dest, pos, length);
			incPosition(length);
			return dest;
		}
	}
	
	public byte[] readBytes(byte[] dest) {
		return readBytes(dest, 0, dest.length);
	}
	
	public byte[] readBytes(int length) {
		return readBytes(new byte[length]);
	}
	
	public byte[] readAllBytes() {
		if (bytes.length == getPosition()) {
			return new byte[0];
		}
		
		return readBytes(bytes.length - getPosition());
	}
	
	public byte[] readBytesWithLength() {
		synchronized (this) {
			byte[] result = new byte[readInt()];
			readBytes(result);
			return result;
		}
	}
	
	public String readString() {
		return new String(readBytesWithLength(), UTF_8);
	}
	
	public String readUnicodeString() {
		return new String(readBytesWithLength(), UTF_16);
	}
	
	public short readShort() {
		synchronized (this) {
			checkPos(BYTES_SHORT);
			short result = ByteIOUtil.readShort(this.bytes, getPosition());
			incPosition(BYTES_SHORT);
			return result;
		}
	}
	
	public int readInt() {
		synchronized (this) {
			checkPos(BYTES_INT);
			int result = ByteIOUtil.readInt(this.bytes, getPosition());
			incPosition(BYTES_INT);
			return result;
		}
	}
	
	public long readLong() {
		synchronized (this) {
			checkPos(BYTES_LONG);
			long result = ByteIOUtil.readLong(this.bytes, getPosition());
			incPosition(BYTES_LONG);
			return result;
		}
	}

	private void checkPos(int bytes) {
		if (getPosition() + bytes > this.bytes.length) {
			throw new IllegalStateException("Too little elements (" + this.bytes.length + " available; " + (getPosition() + bytes) + " required)");
		}
	}
}
