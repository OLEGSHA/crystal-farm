package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.util.bytes.ByteIOUtil;

public class SBDByteArrayValue extends SBDValue<byte[]> {
	
	private static final byte[] EMPTY = new byte[0];
	
	private byte[] value = EMPTY;

	public SBDByteArrayValue(String name) {
		super(name, byte[].class);
	}

	@Override
	public int read(InputStream input, String argument) throws IOException {
		int length = ByteIOUtil.readInt(input);
		input.read(value = new byte[length]);
		return length + ByteIOUtil.BYTES_INT;
	}

	@Override
	public void write(OutputStream output) throws IOException {
		ByteIOUtil.writeInt(output, value.length);
		output.write(value);
	}

	@Override
	public SBDValue<byte[]> newCopy(String name) {
		return new SBDByteArrayValue(name);
	}

	@Override
	public byte[] getValue() {
		return value;
	}

	@Override
	public void setValue(byte[] value) {
		this.value = value;
	}

	@Override
	public int getCurrentLength() {
		return getValue().length + ByteIOUtil.BYTES_INT;
	}
	
	@Override
	public boolean isSizeFixed() {
		return false;
	}
	
	@Override
	public int skip(InputStream input) throws IOException {
		int length = ByteIOUtil.readInt(input);
		input.skip(length);
		return length + ByteIOUtil.BYTES_INT;
	}

}
