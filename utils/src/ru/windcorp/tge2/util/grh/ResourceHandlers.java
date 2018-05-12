package ru.windcorp.tge2.util.grh;

import ru.windcorp.tge2.fileio.data.PTDResourceHandler;
import ru.windcorp.tge2.fileio.data.sbd.SBDResourceHandler;
import ru.windcorp.tge2.util.Registry;

public class ResourceHandlers extends Registry<Class<?>, ResourceHandler<?>> {

	public static final ResourceHandlers INST = new ResourceHandlers();
	
	static {
		INST.register(new PTDResourceHandler());
		INST.register(new SBDResourceHandler());
	}
	
	@Override
	public Class<?> createKeyFor(ResourceHandler<?> value) {
		return value.getFormat();
	}
	
}
