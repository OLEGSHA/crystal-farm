package ru.windcorp.tge2.util.dataio;

import java.io.IOException;
import java.nio.charset.Charset;

public abstract class AbstractByteDataWriter extends AbstractDataWriter {
	
	public static final int ARRAY_LENGTH_BYTES = Integer.BYTES;
	public static final int BOOLEAN_BYTES = Byte.BYTES;
	
	public static class LengthCalculator {
		
		private int length;
		
		public int get() {
			return length;
		}
		
		public LengthCalculator writeArray(int length) {
			this.length += ARRAY_LENGTH_BYTES + length;
			return this;
		}
		
		public LengthCalculator writeBoolean() {
			length += BOOLEAN_BYTES;
			return this;
		}
		
		public LengthCalculator writeByte() {
			length += Byte.BYTES;
			return this;
		}
		
		public LengthCalculator writeChar() {
			length += Character.BYTES;
			return this;
		}
		
		public LengthCalculator writeDouble() {
			length += Double.BYTES;
			return this;
		}
		
		public LengthCalculator writeFloat() {
			length += Float.BYTES;
			return this;
		}
		
		public LengthCalculator writeInt() {
			length += Integer.BYTES;
			return this;
		}
		
		public LengthCalculator writeLong() {
			length += Long.BYTES;
			return this;
		}
		
		public LengthCalculator writeRawArray(int length) {
			this.length += length;
			return this;
		}
		
		public LengthCalculator writeShort() {
			length += Short.BYTES;
			return this;
		}
		
		public LengthCalculator writeString(int length) {
			return writeArray(length);
		}
		
		public LengthCalculator writeString(String str, Charset charset) {
			return writeArray(str.getBytes(charset).length);
		}

	}

	@Override
	public DataWriter writeShort(short x) throws IOException {
		writeByte((byte) x);					// 00FF
		writeByte((byte) (x >>> Byte.SIZE));	// FF00
		return this;
	}

	@Override
	public DataWriter writeInt(int x) throws IOException {
		writeByte((byte) x);					// 0000 00FF
		writeByte((byte) (x >>>= Byte.SIZE));	// 0000 FF00
		writeByte((byte) (x >>>= Byte.SIZE));	// 00FF 0000
		writeByte((byte) (x >>> Byte.SIZE));	// FF00 0000
		return this;
	}

	@Override
	public DataWriter writeLong(long x) throws IOException {
		writeByte((byte) x);					// 0000 0000 0000 00FF
		writeByte((byte) (x >>>= Byte.SIZE));	// 0000 0000 0000 FF00
		writeByte((byte) (x >>>= Byte.SIZE));	// 0000 0000 00FF 0000
		writeByte((byte) (x >>>= Byte.SIZE));	// 0000 0000 FF00 0000
		writeByte((byte) (x >>>= Byte.SIZE));	// 0000 00FF 0000 0000
		writeByte((byte) (x >>>= Byte.SIZE));	// 0000 FF00 0000 0000
		writeByte((byte) (x >>>= Byte.SIZE));	// 00FF 0000 0000 0000
		writeByte((byte) (x >>> Byte.SIZE));	// FF00 0000 0000 0000
		return this;
	}

	@Override
	public DataWriter writeChar(char x) throws IOException {
		writeByte((byte) x);					// 00FF
		writeByte((byte) (x >>> Byte.SIZE));	// FF00
		return this;
	}
	
	@Override
	public DataWriter writeBoolean(boolean x) throws IOException {
		writeByte(x ? (byte) 0x00 : (byte) 0x01);
		return this;
	}

	@Override
	public DataWriter writeFloat(float x) throws IOException {
		writeInt(Float.floatToIntBits(x));
		return this;
	}

	@Override
	public DataWriter writeDouble(double x) throws IOException {
		writeLong(Double.doubleToLongBits(x));
		return this;
	}

	@Override
	public DataWriter writeString(String str, Charset ch) throws IOException {
		writeArray(str.getBytes(ch));
		return this;
	}

	@Override
	public DataWriter writeString(String str) throws IOException {
		writeArray(str.getBytes(DataIOUtils.UTF_8));
		return this;
	}
	
	@Override
	public DataWriter writeArray(byte[] x, int begin, int length) throws IOException {
		writeInt(length);
		writeRawArray(x, begin, length);
		return this;
	}
	
}
