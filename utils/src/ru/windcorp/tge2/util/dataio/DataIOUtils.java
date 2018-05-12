package ru.windcorp.tge2.util.dataio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class DataIOUtils {
	
	public static int defaultMaxArrayLength = 256 * 1024 * 1024; // 256 MB
	
	public static final Charset UTF_8 = Charset.forName("UTF-8"),
			UTF_16 = Charset.forName("UTF-16BE");
	
	public static InputStreamDataReader reader(InputStream stream) {
		return new InputStreamDataReader(stream);
	}
	
	public static OutputStreamDataWriter writer(OutputStream stream) {
		return new OutputStreamDataWriter(stream);
	}
	
	public static ByteArrayDataReader reader(byte[] array) {
		return new ByteArrayDataReader(array);
	}
	
	public static ByteArrayDataWriter writer(byte[] array) {
		return new ByteArrayDataWriter(array);
	}
	
	public static DataReader synch(DataReader reader, Object lock) {
		return new SynchronizedDataReader(reader, lock);
	}
	
	public static DataWriter synch(DataWriter writer, Object lock) {
		return new SynchronizedDataWriter(writer, lock);
	}
	
	public static DataRW join(DataReader reader, DataWriter writer) {
		return new SyntheticDataRW(reader, writer);
	}
	
	public static DataRW getRW(java.net.Socket socket) throws IOException {
		return join(synch(reader(socket.getInputStream()), socket.getInputStream()),
				synch(writer(socket.getOutputStream()), socket.getOutputStream()));
	}

}
