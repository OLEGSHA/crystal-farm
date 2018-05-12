package ru.windcorp.tge2.util.grh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.windcorp.tge2.util.dataio.DataIOUtils;
import ru.windcorp.tge2.util.dataio.DataReader;
import ru.windcorp.tge2.util.dataio.DataWriter;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public class Resource implements OutputSupplier {
	
	private final String path;
	private final ResourceManager manager;

	public Resource(String path, ResourceManager manager) {
		this.path = path;
		this.manager = manager;
	}

	public String getPath() {
		return path;
	}

	public ResourceManager getManager() {
		return manager;
	}
	
	@Override
	public String toString() {
		return getManager().getCanonicalPath(getPath());
	}
	
	public InputStream getInputStream() throws IOException {
		return getManager().getInputStream(getPath());
	}
	
	public DataReader getReader() throws IOException {
		return DataIOUtils.reader(getInputStream());
	}
	
	public DataWriter getWriter() throws IOException {
		return DataIOUtils.writer(getOutputStream());
	}
	
	public OutputStream getOutputStream() throws IOException {
		return getManager().getOutputStream(getPath());
	}
	
	public String canRead() {
		return getManager().canRead(getPath());
	}
	
	public String canWrite() {
		return getManager().canWrite(getPath());
	}

	public Resource getSelfIfSuppliesOutput() {
		if (canWrite() == null) {
			return this;
		}
		
		return null;
	}
	
	@Override
	public OutputStream supply() throws IOException {
		return getOutputStream();
	}
	
	public <T> T read(Class<T> format, Object... params)
			throws IOException, SyntaxException {
		return getManager().read(getPath(), format, params);
	}

}
