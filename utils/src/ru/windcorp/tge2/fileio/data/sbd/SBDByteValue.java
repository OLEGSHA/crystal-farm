package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.util.bytes.ByteIOUtil;

public class SBDByteValue extends SBDValue<Byte> {
	
	private byte value;

	public SBDByteValue(String name) {
		super(name, Byte.class);
	}

	@Override
	public SBDValue<Byte> newCopy(String name) {
		return new SBDByteValue(name);
	}

	@Override
	public Byte getValue() {
		return value;
	}

	@Override
	public void setValue(Byte value) {
		if (value == null) {
			this.value = 0;
		} else {
			this.value = value;
		}
	}

	@Override
	public int read(InputStream input, String argument) throws IOException {
		value = (byte) input.read();
		return ByteIOUtil.BYTES_BYTE;
	}

	@Override
	public void write(OutputStream output) throws IOException {
		output.write(value);
	}

	@Override
	public int getCurrentLength() {
		return ByteIOUtil.BYTES_BYTE;
	}

}
