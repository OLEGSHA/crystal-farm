package ru.windcorp.tge2.util.grh;

import java.io.IOException;
import java.io.InputStream;

import ru.windcorp.tge2.util.exceptions.SyntaxException;

public interface ResourceHandler<T> {
	
	public Class<T> getFormat();
	
	public T create(InputStream input, OutputSupplier output, Object... params) throws IOException, SyntaxException;

}
