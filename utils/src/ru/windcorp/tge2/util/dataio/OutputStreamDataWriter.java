package ru.windcorp.tge2.util.dataio;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamDataWriter extends AbstractByteDataWriter implements Closeable {
	
	private final OutputStream outputStream;
	
	public OutputStreamDataWriter(OutputStream stream) {
		this.outputStream = stream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	@Override
	public DataWriter writeByte(byte x) throws IOException {
		getOutputStream().write(x);
		return this;
	}

	@Override
	public DataWriter writeRawArray(byte[] x, int begin, int length) throws IOException {
		getOutputStream().write(x, begin, length);
		return this;
	}

	@Override
	public void close() throws IOException {
		getOutputStream().close();
	}

}
