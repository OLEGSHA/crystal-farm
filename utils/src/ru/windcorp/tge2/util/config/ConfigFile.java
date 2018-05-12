package ru.windcorp.tge2.util.config;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import ru.windcorp.tge2.util.grh.Resource;
import ru.windcorp.tge2.fileio.data.sbd.StructuredByteData;

public class ConfigFile extends Config {
	
	private final Resource resource;
	private final Resource defaultResource;
	
	private StructuredByteData structure = null;
	
	public ConfigFile(String rootName, Resource resource, Resource defaultResource) {
		super(rootName);
		this.resource = resource;
		this.defaultResource = defaultResource;
	}

	public Resource getResource() {
		return resource;
	}

	public Resource getDefaultResource() {
		return defaultResource;
	}

	public StructuredByteData getStructure() {
		return structure;
	}

	protected void setStructure(StructuredByteData structure) {
		this.structure = structure;
	}
	
	/**
	 * Loads the configuration data from the disk discarding any local data.
	 * @return 0 - loaded remote file, 1 - loaded local file, 2 - loaded blank.
	 */
	public int load() {
		try {
			load(getResource(), getResource());
			return 0;
		} catch (Exception e) {
			try {
				reset();
				return 1;
			} catch (Exception e1) {
				resetToBlank();
				return 2;
			}
		}
	}
	
	public void resetToBlank() {
		setStructure(new StructuredByteData(getResource().getSelfIfSuppliesOutput()));
		try {
			load(getStructure().getRoot());
		} catch (IOException e) {
			throw new UncheckedIOException("Could not load blank configuration", e);
		}
	}
	
	public void load(Resource resource, Resource save) throws IOException {
		setStructure(StructuredByteData.read(resource.getInputStream(), save.getSelfIfSuppliesOutput()));
		load(getStructure().getRoot());
	}
	
	public void save() throws IOException {
		getStructure().save();
	}

	public void save(File file) throws IOException {
		getStructure().save(file);
	}

	public void save(OutputStream output) throws IOException {
		getStructure().save(output);
	}
	
	public void reset() throws IOException {
		load(getDefaultResource(), getResource());
	}
	
}
