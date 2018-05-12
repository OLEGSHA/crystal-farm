package ru.windcorp.tge2.fileio.data.sbd;

import java.io.IOException;
import java.io.InputStream;

import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.grh.OutputSupplier;
import ru.windcorp.tge2.util.grh.ResourceHandler;

public class SBDResourceHandler implements ResourceHandler<StructuredByteData> {

	@Override
	public Class<StructuredByteData> getFormat() {
		return StructuredByteData.class;
	}

	@Override
	public StructuredByteData create(InputStream input, OutputSupplier output, Object... params)
			throws IOException, SyntaxException {
		return StructuredByteData.read(input, output);
	}

}
