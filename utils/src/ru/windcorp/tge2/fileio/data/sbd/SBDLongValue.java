package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.util.bytes.ByteIOUtil;

public class SBDLongValue extends SBDValue<Long> {
	
	private long value;

	public SBDLongValue(String name) {
		super(name, Long.class);
	}

	@Override
	public SBDValue<Long> newCopy(String name) {
		return new SBDLongValue(name);
	}

	@Override
	public Long getValue() {
		return value;
	}

	@Override
	public void setValue(Long value) {
		if (value == null) {
			this.value = 0;
		} else {
			this.value = value;
		}
	}

	@Override
	public int read(InputStream input, String argument) throws IOException {
		value = ByteIOUtil.readLong(input);
		return getCurrentLength();
	}

	@Override
	public void write(OutputStream output) throws IOException {
		ByteIOUtil.writeLong(output, value);
	}

	@Override
	public int getCurrentLength() {
		return ByteIOUtil.BYTES_LONG;
	}

}
