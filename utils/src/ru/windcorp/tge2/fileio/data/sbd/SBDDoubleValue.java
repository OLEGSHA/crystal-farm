package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.util.bytes.ByteIOUtil;

public class SBDDoubleValue extends SBDValue<Double> {
	
	private double value;

	public SBDDoubleValue(String name) {
		super(name, Double.class);
	}
	
	@Override
	public SBDValue<Double> newCopy(String name) {
		return new SBDDoubleValue(name);
	}

	@Override
	public Double getValue() {
		return value;
	}

	@Override
	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public int read(InputStream input, String argument) throws IOException {
		setValue(ByteIOUtil.readDouble(input));
		return getCurrentLength();
	}

	@Override
	public void write(OutputStream output) throws IOException {
		ByteIOUtil.writeDouble(output, getValue());
	}

	@Override
	public int getCurrentLength() {
		return ByteIOUtil.BYTES_DOUBLE;
	}

}
