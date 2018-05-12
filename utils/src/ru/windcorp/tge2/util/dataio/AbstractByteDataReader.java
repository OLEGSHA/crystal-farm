package ru.windcorp.tge2.util.dataio;

import java.io.IOException;
import java.nio.charset.Charset;

public abstract class AbstractByteDataReader extends AbstractDataReader {

	@Override
	public short readShort() throws IOException {
		short result;
		result = (short) (readByte() & 0xFF);
		result |= (readByte() & 0xFF) << Byte.SIZE;
		return result;
	}

	@Override
	public int readInt() throws IOException {
		int result;
		result = readByte() & 0xFF;
		result |= (readByte() & 0xFF) << Byte.SIZE;
		result |= (readByte() & 0xFF) << Byte.SIZE * 2;
		result |= (readByte() & 0xFF) << Byte.SIZE * 3;
		return result;
	}

	@Override
	public long readLong() throws IOException {
		long result;
		result = ((long) readByte()) & 0xFF;
		result |= (((long) readByte()) & 0xFF) << Byte.SIZE * 1;
		result |= (((long) readByte()) & 0xFF) << Byte.SIZE * 2;
		result |= (((long) readByte()) & 0xFF) << Byte.SIZE * 3;
		result |= (((long) readByte()) & 0xFF) << Byte.SIZE * 4;
		result |= (((long) readByte()) & 0xFF) << Byte.SIZE * 5;
		result |= (((long) readByte()) & 0xFF) << Byte.SIZE * 6;
		result |= (((long) readByte()) & 0xFF) << Byte.SIZE * 7;
		return result;
	}

	@Override
	public char readChar() throws IOException {
		char result;
		result = (char) (readByte() & 0xFF);
		result |= (readByte() & 0xFF) << Byte.SIZE; 
		return result;
	}
	
	@Override
	public boolean readBoolean() throws IOException {
		return readByte() != 0;
	}

	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}
	
	@Override
	public byte[] readArray(byte[] output, int maxLength) throws IOException {
		int length = readInt();
		if (length < 0) {
			throw new DataIOException("Byte-encoded array has negative length; probably end of stream");
		}
		
		if (length > maxLength) {
			throw new DataIOException("Byte-encoded array exceeds maximum allowed length: " + length + " > " + maxLength);
		}
		
		if (output == null || output.length != length) {
			output = new byte[length];
		}
		
		fillArray(output);
		
		return output;
	}

	@Override
	public String readString(Charset ch) throws IOException {
		return new String(readArray(), ch);
	}

	@Override
	public String readString() throws IOException {
		return new String(readArray(), DataIOUtils.UTF_8);
	}

}
