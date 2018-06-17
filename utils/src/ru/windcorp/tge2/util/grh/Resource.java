package ru.windcorp.tge2.util.grh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Stream;

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
	
	public boolean checkRead(ResourceProblemHandler ifProblem) throws RuntimeException, IOException {
		String problem = canRead();
		if (ifProblem != null && problem != null) {
			return ifProblem.onProblem(problem);
		}
		return problem != null;
	}
	
	public String canWrite() {
		return getManager().canWrite(getPath());
	}
	
	public boolean checkWrite(ResourceProblemHandler ifProblem) throws RuntimeException, IOException {
		String problem = canWrite();
		if (ifProblem != null && problem != null) {
			return ifProblem.onProblem(problem);
		}
		return problem != null;
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
	
	public Stream<String> lines() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream())).lines();
	}
	
	public Resource getChild(String childName) {
		if (childName == null) {
			throw new IllegalArgumentException("childName is null");
		}
		
		if (!childName.startsWith("/")) {
			childName = "/" + childName;
		}
		
		return getManager().getResource(getPath() + childName);
		
	}
}
