package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.util.bytes.ByteIOUtil;

public class SBDBooleanValue extends SBDValue<Boolean> {
	
	private boolean value;

	public SBDBooleanValue(String name) {
		super(name, Boolean.class);
	}

	@Override
	public SBDValue<Boolean> newCopy(String name) {
		return new SBDBooleanValue(name);
	}

	@Override
	public Boolean getValue() {
		return value;
	}

	@Override
	public void setValue(Boolean value) {
		if (value == null) {
			this.value = false;
		} else {
			this.value = value;
		}
	}

	@Override
	public int read(InputStream input, String argument) throws IOException {
		value = input.read() == 1 ? true : false;
		return ByteIOUtil.BYTES_BYTE;
	}

	@Override
	public void write(OutputStream output) throws IOException {
		output.write(value ? 1 : 0);
	}

	@Override
	public int getCurrentLength() {
		return ByteIOUtil.BYTES_BYTE;
	}

}
