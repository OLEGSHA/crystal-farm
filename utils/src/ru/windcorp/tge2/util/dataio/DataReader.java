package ru.windcorp.tge2.util.dataio;

import java.io.IOException;
import java.nio.charset.Charset;

import ru.windcorp.tge2.util.grh.Loadable;

public interface DataReader {
	
	public byte readByte() throws IOException;
	public short readShort() throws IOException;
	public int readInt() throws IOException;
	public long readLong() throws IOException;
	public char readChar() throws IOException;
	public boolean readBoolean() throws IOException;
	public float readFloat() throws IOException;
	public double readDouble() throws IOException;
	
	public int readUnsignedByte() throws IOException;
	
	public byte[] fillArray(byte[] array, int begin, int length) throws IOException;
	public byte[] fillArray(byte[] array) throws IOException;
	public byte[] readArray(byte[] output, int maxLength) throws IOException;
	public byte[] readArray(int maxLength) throws IOException;
	public byte[] readArray(byte[] output) throws IOException;
	public byte[] readArray() throws IOException;
	
	public String readString(Charset ch) throws IOException;
	public String readString() throws IOException;
	
	public boolean supportsSkip();
	public void skip(long bytes) throws IOException;
	
	public <T extends Loadable<DataReader>> T read(T readable) throws IOException;

}
