package ru.windcorp.tge2.util.dataio;

import java.io.IOException;

import ru.windcorp.tge2.util.grh.Saveable;

public abstract class AbstractDataWriter implements DataWriter {

	@Override
	public DataWriter writeArray(byte[] x) throws IOException {
		writeArray(x, 0, x.length);
		return this;
	}

	@Override
	public DataWriter write(Saveable<DataWriter> writable) throws IOException {
		writable.save(this);
		return this;
	}
	
	@Override
	public DataWriter writeRawArray(byte[] x) throws IOException {
		writeRawArray(x, 0, x.length);
		return this;
	}

}
