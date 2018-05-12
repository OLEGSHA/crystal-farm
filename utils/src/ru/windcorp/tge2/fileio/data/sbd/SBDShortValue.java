package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.util.bytes.ByteIOUtil;

public class SBDShortValue extends SBDValue<Short> {
	
	private short value;

	public SBDShortValue(String name) {
		super(name, Short.class);
	}

	@Override
	public SBDValue<Short> newCopy(String name) {
		return new SBDShortValue(name);
	}

	@Override
	public Short getValue() {
		return value;
	}

	@Override
	public void setValue(Short value) {
		if (value == null) {
			this.value = 0;
		} else {
			this.value = value;
		}
	}

	@Override
	public int read(InputStream input, String argument) throws IOException {
		value = ByteIOUtil.readShort(input);
		return getCurrentLength();
	}

	@Override
	public void write(OutputStream output) throws IOException {
		ByteIOUtil.writeInt(output, value);
	}

	@Override
	public int getCurrentLength() {
		return ByteIOUtil.BYTES_SHORT;
	}

}
