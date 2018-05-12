package ru.windcorp.tge2.util.dataio;

import java.io.IOException;
import java.nio.charset.Charset;

import ru.windcorp.tge2.util.grh.Loadable;
import ru.windcorp.tge2.util.grh.Saveable;

public class SyntheticDataRW implements DataRW {

	private final DataReader reader;
	private final DataWriter writer;
	
	public SyntheticDataRW(DataReader reader, DataWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	public byte readByte() throws IOException {
		return reader.readByte();
	}

	@Override
	public short readShort() throws IOException {
		return reader.readShort();
	}

	@Override
	public int readInt() throws IOException {
		return reader.readInt();
	}

	@Override
	public long readLong() throws IOException {
		return reader.readLong();
	}

	@Override
	public char readChar() throws IOException {
		return reader.readChar();
	}

	@Override
	public boolean readBoolean() throws IOException {
		return reader.readBoolean();
	}

	@Override
	public float readFloat() throws IOException {
		return reader.readFloat();
	}

	@Override
	public double readDouble() throws IOException {
		return reader.readDouble();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return reader.readUnsignedByte();
	}

	@Override
	public byte[] readArray(byte[] output) throws IOException {
		return reader.readArray(output);
	}

	@Override
	public byte[] readArray() throws IOException {
		return reader.readArray();
	}
	
	@Override
	public byte[] readArray(byte[] output, int maxLength) throws IOException {
		return reader.readArray(output, maxLength);
	}

	@Override
	public byte[] readArray(int maxLength) throws IOException {
		return reader.readArray(maxLength);
	}

	@Override
	public String readString(Charset ch) throws IOException {
		return reader.readString(ch);
	}

	@Override
	public String readString() throws IOException {
		return reader.readString();
	}

	@Override
	public boolean supportsSkip() {
		return reader.supportsSkip();
	}

	@Override
	public void skip(long bytes) throws IOException {
		reader.skip(bytes);
	}

	@Override
	public <T extends Loadable<DataReader>> T read(T readable) throws IOException {
		return reader.read(readable);
	}

	@Override
	public byte[] fillArray(byte[] array, int begin, int length) throws IOException {
		return reader.fillArray(array, begin, length);
	}

	@Override
	public byte[] fillArray(byte[] array) throws IOException {
		return reader.fillArray(array);
	}
	
	@Override
	public DataWriter writeByte(byte x) throws IOException {
		writer.writeByte(x);
		return this;
	}

	@Override
	public DataWriter writeShort(short x) throws IOException {
		writer.writeShort(x);
		return this;
	}

	@Override
	public DataWriter writeInt(int x) throws IOException {
		writer.writeInt(x);
		return this;
	}

	@Override
	public DataWriter writeLong(long x) throws IOException {
		writer.writeLong(x);
		return this;
	}

	@Override
	public DataWriter writeChar(char x) throws IOException {
		writer.writeChar(x);
		return this;
	}

	@Override
	public DataWriter writeBoolean(boolean x) throws IOException {
		writer.writeBoolean(x);
		return this;
	}

	@Override
	public DataWriter writeFloat(float x) throws IOException {
		writer.writeFloat(x);
		return this;
	}

	@Override
	public DataWriter writeDouble(double x) throws IOException {
		writer.writeDouble(x);
		return this;
	}

	@Override
	public DataWriter writeArray(byte[] x, int begin, int length) throws IOException {
		writer.writeArray(x, begin, length);
		return this;
	}

	@Override
	public DataWriter writeArray(byte[] x) throws IOException {
		writer.writeArray(x);
		return this;
	}

	@Override
	public DataWriter writeString(String str, Charset ch) throws IOException {
		writer.writeString(str, ch);
		return this;
	}

	@Override
	public DataWriter writeString(String str) throws IOException {
		writer.writeString(str);
		return this;
	}

	@Override
	public DataWriter write(Saveable<DataWriter> writable) throws IOException {
		writer.write(writable);
		return this;
	}

	@Override
	public DataWriter writeRawArray(byte[] x, int begin, int length) throws IOException {
		writer.writeRawArray(x, begin, length);
		return this;
	}

	@Override
	public DataWriter writeRawArray(byte[] x) throws IOException {
		writer.writeRawArray(x);
		return this;
	}
	
}
