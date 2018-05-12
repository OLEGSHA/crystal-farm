package ru.windcorp.tge2.util.debug;

import java.io.IOException;
import java.io.OutputStream;

public class DebugOutputStream extends OutputStream {

	@Override
	public void write(int b) throws IOException {
		if (Debug.allowDebug) {
			Debug.getOutput().write(b);
		}
	}

	@Override
	public void close() throws IOException {
		// Do nothing: stream is not ours
	}

	@Override
	public void flush() throws IOException {
		if (Debug.allowDebug) {
			Debug.getOutput().flush();
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if (Debug.allowDebug) {
			Debug.getOutput().write(b, off, len);
		}
	}

	@Override
	public void write(byte[] b) throws IOException {
		if (Debug.allowDebug) {
			Debug.getOutput().write(b);
		}
	}

}
