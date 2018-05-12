package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.util.bytes.ByteIOUtil;

public class SBDIntegerValue extends SBDValue<Integer> {
	
	private int value;

	public SBDIntegerValue(String name) {
		super(name, Integer.class);
	}

	@Override
	public SBDValue<Integer> newCopy(String name) {
		return new SBDIntegerValue(name);
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public void setValue(Integer value) {
		if (value == null) {
			this.value = 0;
		} else {
			this.value = value;
		}
	}

	@Override
	public int read(InputStream input, String argument) throws IOException {
		value = ByteIOUtil.readInt(input);
		return getCurrentLength();
	}

	@Override
	public void write(OutputStream output) throws IOException {
		ByteIOUtil.writeInt(output, value);
	}

	@Override
	public int getCurrentLength() {
		return ByteIOUtil.BYTES_INT;
	}

}
