package ru.windcorp.tge2.fileio.data;

import java.io.IOException;
import java.io.InputStream;

import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.grh.OutputSupplier;
import ru.windcorp.tge2.util.grh.ResourceHandler;

public class PTDResourceHandler implements ResourceHandler<PlainTypedData> {

	@Override
	public Class<PlainTypedData> getFormat() {
		return PlainTypedData.class;
	}

	@Override
	public PlainTypedData create(InputStream input, OutputSupplier output, Object... params)
			throws IOException, SyntaxException {
		return PlainTypedData.read(input, output);
	}

}
