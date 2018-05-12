package ru.windcorp.tge2.util.dataio;

import java.io.IOException;
import java.nio.charset.Charset;

import ru.windcorp.tge2.util.grh.Saveable;

/**
 * Not tested sufficiently
 * @author OLEGSHA
 *
 */
public class ByteArrayBuilder extends AbstractByteDataWriter {

	private byte[] array;
	private int index = 0;
	
	public int growFactor = 16;
	
	public byte[] getBuffer() {
		return array;
	}
	//TODO - this seems to work fine, though
	public ByteArrayBuilder(int initialLength) {
		this.array = new byte[initialLength];
	}
	
	public ByteArrayBuilder() {
		this(64);
	}

	@Override
	public ByteArrayBuilder writeByte(byte x) {
		if (index >= array.length) grow(growFactor);
		array[index++] = x;
		return this;
	}

	@Override
	public ByteArrayBuilder writeRawArray(byte[] x, int begin, int length) {
		if (index + length >= array.length) grow(length);
		System.arraycopy(x, begin, this.array, index, length);
		index += length;
		return this;
	}
	
	protected ByteArrayBuilder grow(int length) {
		/*length = ((length - 1) / growFactor + 1) * growFactor;*/
		byte[] newArray = new byte[index + length];
		System.arraycopy(array, 0, newArray, 0, array.length);
		array = newArray;
		return this;
	}
	
	public byte[] toArray() {
		byte[] result = new byte[index];
		System.arraycopy(array, 0, result, 0, index);
		return result;
	}
	
	@Override
	public ByteArrayBuilder writeShort(short x) {
		try {
			super.writeShort(x);
		} catch (IOException e) { }
		return this;
	}
	
	@Override
	public ByteArrayBuilder writeInt(int x) {
		try {
			super.writeInt(x);
		} catch (IOException e) { }
		return this;
	}
	
	@Override
	public ByteArrayBuilder writeLong(long x) {
		try {
			super.writeLong(x);
		} catch (IOException e) { }
		return this;
	}
	@Override
	public ByteArrayBuilder writeChar(char x) {
		try {
			super.writeChar(x);
		} catch (IOException e) { }
		return this;
	}
	@Override
	public ByteArrayBuilder writeBoolean(boolean x) {
		try {
			super.writeBoolean(x);
		} catch (IOException e) { }
		return this;
	}
	@Override
	public ByteArrayBuilder writeFloat(float x) {
		try {
			super.writeFloat(x);
		} catch (IOException e) { }
		return this;
	}
	@Override
	public ByteArrayBuilder writeDouble(double x) {
		try {
			super.writeDouble(x);
		} catch (IOException e) { }
		return this;
	}
	@Override
	public ByteArrayBuilder writeString(String str, Charset ch) {
		try {
			super.writeString(str, ch);
		} catch (IOException e) { }
		return this;
	}
	@Override
	public ByteArrayBuilder writeString(String str) {
		try {
			super.writeString(str);
		} catch (IOException e) { }
		return this;
	}
	@Override
	public ByteArrayBuilder writeArray(byte[] x, int begin, int length) {
		try {
			super.writeArray(x, begin, length);
		} catch (IOException e) { }
		return this;
	}
	@Override
	public ByteArrayBuilder writeArray(byte[] x) {
		try {
			super.writeArray(x);
		} catch (IOException e) { }
		return this;
	}
	@Override
	public ByteArrayBuilder write(Saveable<DataWriter> writable) {
		try {
			super.write(writable);
		} catch (IOException e) { }
		return this;
	}
	@Override
	public ByteArrayBuilder writeRawArray(byte[] x) {
		try {
			super.writeRawArray(x);
		} catch (IOException e) { }
		return this;
	}

}
