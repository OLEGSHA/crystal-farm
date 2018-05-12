package ru.windcorp.tge2.util.bytes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class ByteIOUtil {
	
	public static final int BITS_BYTE = 8;
	
	public static final int BYTES_BYTE = Byte.SIZE / BITS_BYTE;
	public static final int BYTES_SHORT = Short.SIZE / BITS_BYTE;
	public static final int BYTES_INT = Integer.SIZE / BITS_BYTE;
	public static final int BYTES_LONG = Long.SIZE / BITS_BYTE;
	
	public static final int BYTES_FLOAT = Float.SIZE / BITS_BYTE;
	public static final int BYTES_DOUBLE = Double.SIZE / BITS_BYTE;

	public static final Charset UTF_8 = Charset.forName("UTF-8"),
			UTF_16 = Charset.forName("UTF-16BE");
	
	public static void writeShort(byte[] dest, int off, short data) {
		dest[off] = (byte) data;					// 00FF
		dest[++off] = (byte) (data >>> Byte.SIZE);	// FF00
	}
	
	public static void writeShort(OutputStream dest, short data) throws IOException {
		dest.write((byte) data);					// 00FF
		dest.write((byte) (data >>> Byte.SIZE));	// FF00
	}
	
	public static void writeInt(byte[] dest, int off, int data) {
		dest[off] = (byte) data;					// 0000 00FF
		dest[++off] = (byte) (data >>>= Byte.SIZE);	// 0000 FF00
		dest[++off] = (byte) (data >>>= Byte.SIZE);	// 00FF 0000
		dest[++off] = (byte) (data >>> Byte.SIZE);	// FF00 0000
	}
	
	public static void writeInt(OutputStream dest, int data) throws IOException {
		dest.write((byte) data);					// 0000 00FF
		dest.write((byte) (data >>>= Byte.SIZE));	// 0000 FF00
		dest.write((byte) (data >>>= Byte.SIZE));	// 00FF 0000
		dest.write((byte) (data >>> Byte.SIZE));	// FF00 0000
	}
	
	public static void writeLong(byte[] dest, int off, long data) {
		dest[off] = (byte) data;					// 0000 0000 0000 00FF
		dest[++off] = (byte) (data >>>= Byte.SIZE);	// 0000 0000 0000 FF00
		dest[++off] = (byte) (data >>>= Byte.SIZE);	// 0000 0000 00FF 0000
		dest[++off] = (byte) (data >>>= Byte.SIZE);	// 0000 0000 FF00 0000
		dest[++off] = (byte) (data >>>= Byte.SIZE);	// 0000 00FF 0000 0000
		dest[++off] = (byte) (data >>>= Byte.SIZE);	// 0000 FF00 0000 0000
		dest[++off] = (byte) (data >>>= Byte.SIZE);	// 00FF 0000 0000 0000
		dest[++off] = (byte) (data >>> Byte.SIZE);	// FF00 0000 0000 0000
	}
	
	public static void writeLong(OutputStream dest, long data) throws IOException {
		dest.write((byte) data);					// 0000 0000 0000 00FF
		dest.write((byte) (data >>>= Byte.SIZE));	// 0000 0000 0000 FF00
		dest.write((byte) (data >>>= Byte.SIZE));	// 0000 0000 00FF 0000
		dest.write((byte) (data >>>= Byte.SIZE));	// 0000 0000 FF00 0000
		dest.write((byte) (data >>>= Byte.SIZE));	// 0000 00FF 0000 0000
		dest.write((byte) (data >>>= Byte.SIZE));	// 0000 FF00 0000 0000
		dest.write((byte) (data >>>= Byte.SIZE));	// 00FF 0000 0000 0000
		dest.write((byte) (data >>> Byte.SIZE));	// FF00 0000 0000 0000
	}
	
	public static void writeFloat(byte[] dest, int off, float data) {
		writeInt(dest, off, Float.floatToRawIntBits(data));
	}
	
	public static void writeFloat(OutputStream dest, float data) throws IOException {
		writeInt(dest, Float.floatToRawIntBits(data));
	}
	
	public static void writeDouble(byte[] dest, int off, double data) {
		writeLong(dest, off, Double.doubleToRawLongBits(data));
	}
	
	public static void writeDouble(OutputStream dest, double data) throws IOException {
		writeLong(dest, Double.doubleToRawLongBits(data));
	}
	
	public static short readShort(byte[] src, int off) {
		short result;
		result = (short) (src[off] & 0xFF);
		result |= (src[++off] & 0xFF) << Byte.SIZE;
		return result;
	}
	
	public static short readShort(InputStream src) throws IOException {
		short result;
		result = (short) (src.read() & 0xFF);
		result |= (src.read() & 0xFF) << Byte.SIZE;
		return result;
	}
	
	public static int readInt(byte[] src, int off) {
		int result;
		result = src[off] & 0xFF;
		result |= (src[++off] & 0xFF) << Byte.SIZE;
		result |= (src[++off] & 0xFF) << Byte.SIZE * 2;
		result |= (src[++off] & 0xFF) << Byte.SIZE * 3;
		return result;
	}
	
	public static int readInt(InputStream src) throws IOException {
		int result;
		result = src.read() & 0xFF;
		result |= (src.read() & 0xFF) << Byte.SIZE;
		result |= (src.read() & 0xFF) << Byte.SIZE * 2;
		result |= (src.read() & 0xFF) << Byte.SIZE * 3;
		return result;
	}
	
	public static long readLong(byte[] src, int off) {
		long result;
		result = ((long) src[off]) & 0xFF;
		result |= (((long) src[++off]) & 0xFF) << Byte.SIZE * 1;
		result |= (((long) src[++off]) & 0xFF) << Byte.SIZE * 2;
		result |= (((long) src[++off]) & 0xFF) << Byte.SIZE * 3;
		result |= (((long) src[++off]) & 0xFF) << Byte.SIZE * 4;
		result |= (((long) src[++off]) & 0xFF) << Byte.SIZE * 5;
		result |= (((long) src[++off]) & 0xFF) << Byte.SIZE * 6;
		result |= (((long) src[++off]) & 0xFF) << Byte.SIZE * 7;
		return result;
	}
	
	public static long readLong(InputStream src) throws IOException {
		long result;
		result = ((long) src.read()) & 0xFF;
		result |= (((long) src.read()) & 0xFF) << Byte.SIZE * 1;
		result |= (((long) src.read()) & 0xFF) << Byte.SIZE * 2;
		result |= (((long) src.read()) & 0xFF) << Byte.SIZE * 3;
		result |= (((long) src.read()) & 0xFF) << Byte.SIZE * 4;
		result |= (((long) src.read()) & 0xFF) << Byte.SIZE * 5;
		result |= (((long) src.read()) & 0xFF) << Byte.SIZE * 6;
		result |= (((long) src.read()) & 0xFF) << Byte.SIZE * 7;
		return result;
	}
	
	public static float readFloat(byte[] src, int off) {
		return Float.intBitsToFloat(readInt(src, off));
	}
	
	public static float readFloat(InputStream src) throws IOException {
		return Float.intBitsToFloat(readInt(src));
	}
	
	public static double readDouble(byte[] src, int off) {
		return Double.longBitsToDouble(readLong(src, off));
	}
	
	public static double readDouble(InputStream src) throws IOException {
		return Double.longBitsToDouble(readLong(src));
	}
	
	public static void writeBytesWithLength(OutputStream dest, byte[] data) throws IOException {
		writeInt(dest, data.length);
		dest.write(data);
	}
	
	public static void writeBytesWithLength(byte[] dest, int off, byte[] data) {
		writeInt(dest, off, data.length);
		System.arraycopy(data, 0, dest, off + BYTES_INT, data.length);
	}
	
	public static byte[] readBytesWithLength(InputStream src) throws IOException {
		int length = readInt(src);
		byte[] result = new byte[length];
		src.read(result);
		return result;
	}
	
	public static byte[] readBytesWithLength(byte[] src, int off) {
		int length = readInt(src, off);
		byte[] result = new byte[length];
		System.arraycopy(src, off, result, 0, length);
		return result;
	}
	
	public static void writeString(OutputStream dest, String data, Charset encoding) throws IOException {
		writeBytesWithLength(dest, data.getBytes(encoding));
	}
	
	public static void writeString(byte[] dest, int off, String data, Charset encoding) {
		writeBytesWithLength(dest, off, data.getBytes(encoding));
	}
	
	public static String readString(InputStream src, Charset encoding) throws IOException {
		return new String(readBytesWithLength(src), encoding);
	}
	
	public static String readString(byte[] src, int off, Charset encoding) {
		return new String(readBytesWithLength(src, off), encoding);
	}
	
	public static void writeString(OutputStream dest, String data) throws IOException {
		writeBytesWithLength(dest, data.getBytes(UTF_8));
	}
	
	public static void writeString(byte[] dest, int off, String data) {
		writeBytesWithLength(dest, off, data.getBytes(UTF_8));
	}
	
	public static String readString(InputStream src) throws IOException {
		return new String(readBytesWithLength(src), UTF_8);
	}
	
	public static String readString(byte[] src, int off) {
		return new String(readBytesWithLength(src, off), UTF_8);
	}
	
	public static void writeUnicodeString(OutputStream dest, String data) throws IOException {
		writeBytesWithLength(dest, data.getBytes(UTF_16));
	}
	
	public static void writeUnicodeString(byte[] dest, int off, String data) {
		writeBytesWithLength(dest, off, data.getBytes(UTF_16));
	}
	
	public static String readUnicodeString(InputStream src) throws IOException {
		return new String(readBytesWithLength(src), UTF_16);
	}
	
	public static String readUnicodeString(byte[] src, int off) {
		return new String(readBytesWithLength(src, off), UTF_16);
	}

}
