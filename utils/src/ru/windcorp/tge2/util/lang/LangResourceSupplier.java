package ru.windcorp.tge2.util.lang;

import java.io.IOException;
import java.io.InputStream;

import ru.windcorp.tge2.util.grh.ResourceSupplier;
import ru.windcorp.tge2.util.interfaces.Generator;

public class LangResourceSupplier implements Generator<String, InputStream> {

	private final ResourceSupplier supplier;
	
	public LangResourceSupplier(ResourceSupplier supplier) {
		this.supplier = supplier;
	}

	public ResourceSupplier getSupplier() {
		return supplier;
	}

	@Override
	public InputStream generate(String input) {
		try {
			return getSupplier().getInputStream(input);
		} catch (IOException e) {
			return null;
		}
	}

}
