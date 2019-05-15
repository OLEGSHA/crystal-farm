package ru.windcorp.tge2.util.dataio;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamDataReader extends AbstractByteDataReader implements Closeable {
	
	private final InputStream inputStream;
	
	public InputStreamDataReader(InputStream input) {
		this.inputStream = input;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public byte readByte() throws IOException {
		return (byte) getInputStream().read();
	}

	@Override
	public byte[] fillArray(byte[] array, int begin, int length) throws IOException {
		/*int read;
		if ((read = getInputStream().read(array, begin, length)) != length) {
			throw new IOException("Could not fill entire array, not enough data (" + length + " bytes expected, " + read + " read)");
		}*/
		
		for (int i = 0; i < length; ++i) {
			array[i + begin] = readByte();
		}
		
		return array;
	}

	@Override
	public void close() throws IOException {
		getInputStream().close();
	}
	
	@Override
	public void skip(long bytes) throws IOException {
		getInputStream().skip(bytes);
	}

}
