package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.util.bytes.ByteIOUtil;

public class SBDStringValue extends SBDValue<String> {

	public String value;
	
	public SBDStringValue(String name) {
		super(name, String.class);
	}

	@Override
	public SBDValue<String> newCopy(String name) {
		return new SBDStringValue(name);
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public boolean isSizeFixed() {
		return false;
	}

	@Override
	public int read(InputStream input, String argument) throws IOException {
		setValue(ByteIOUtil.readString(input));
		return getCurrentLength();
	}

	@Override
	public void write(OutputStream output) throws IOException {
		ByteIOUtil.writeString(output, getValue());
	}

	@Override
	public int getCurrentLength() {
		return ByteIOUtil.BYTES_INT + getValue().length();
	}

	@Override
	public int skip(InputStream input) throws IOException {
		int length = ByteIOUtil.readInt(input);
		input.skip(length);
		return ByteIOUtil.BYTES_INT + length;
	}

}
