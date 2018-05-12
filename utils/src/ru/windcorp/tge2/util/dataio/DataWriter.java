package ru.windcorp.tge2.util.dataio;

import java.io.IOException;
import java.nio.charset.Charset;

import ru.windcorp.tge2.util.grh.Saveable;

public interface DataWriter {

	public DataWriter writeByte(byte x) throws IOException;
	public DataWriter writeShort(short x) throws IOException;
	public DataWriter writeInt(int x) throws IOException;
	public DataWriter writeLong(long x) throws IOException;
	public DataWriter writeChar(char x) throws IOException;
	public DataWriter writeBoolean(boolean x) throws IOException;
	public DataWriter writeFloat(float x) throws IOException;
	public DataWriter writeDouble(double x) throws IOException;
	
	public DataWriter writeArray(byte[] x, int begin, int length) throws IOException;
	public DataWriter writeArray(byte[] x) throws IOException;
	public DataWriter writeRawArray(byte[] x, int begin, int length) throws IOException;
	public DataWriter writeRawArray(byte[] x) throws IOException;
	
	public DataWriter writeString(String str, Charset ch) throws IOException;
	public DataWriter writeString(String str) throws IOException;
	
	public DataWriter write(Saveable<DataWriter> writable) throws IOException;
	
}
