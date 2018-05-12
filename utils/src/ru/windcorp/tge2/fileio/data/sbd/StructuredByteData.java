package ru.windcorp.tge2.fileio.data.sbd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedList;

import ru.windcorp.tge2.fileio.AbstractWritable;
import ru.windcorp.tge2.util.Registry;
import ru.windcorp.tge2.util.StringUtil;
import ru.windcorp.tge2.util.exceptions.ImplementationMistakeException;
import ru.windcorp.tge2.util.grh.OutputSupplier;
import ru.windcorp.tge2.util.interfaces.Supplier;

public class StructuredByteData extends AbstractWritable implements Cloneable {
	
	public static final String FILE_SUFFIX = ".tge2sbd";
	public static final int VERSION = 1;
	
	public static final String ROOT_NAME = "ROOT";
	public static final String META_NAME = "~META~";
	public static final SBDStructure STRUCTURE_BASE = new SBDStructure(null);
	public static final char PATH_ARGUMENT_SEPARATOR = '$';
	
	public static final Registry<String, SBDElement> REGISTRY = new Registry<String, SBDElement>() {
		@Override
		public String createKeyFor(SBDElement element) {
			return element.getFullTypeName();
		}
	};
	
	public static final SBDTypeConverter DEFAULT_CONVERTER = new SBDTypeConverter() {

		@Override
		public void read(InputStream is) throws IOException {
			throw new ImplementationMistakeException("I am " + this + " (hash " + this.hashCode() + "). I am a default, and thus I should always reflect current registry status. I cannot read.");
		}
		
	};
	
	static {
		registerElement(STRUCTURE_BASE);
		registerElement(new SBDArray<Object>(null));
		
		registerElement(new SBDByteValue(null));
		registerElement(new SBDBooleanValue(null));
		registerElement(new SBDShortValue(null));
		registerElement(new SBDIntegerValue(null));
		registerElement(new SBDLongValue(null));
		registerElement(new SBDDoubleValue(null));
		registerElement(new SBDStringValue(null));
		
		registerElement(new SBDByteArrayValue(null));
		
		updateTypeConverter();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> SBDValue<T> create(Class<T> clazz, String name) {
		for (SBDElement e : REGISTRY.getEntries()) {
			if (e instanceof SBDValue<?>
					&& ((SBDValue<?>) e).getTypeClass() == clazz) {
				return ((SBDValue<T>) e).newCopy(name);
			}
		}
		
		return null;
	}
	
	public SBDElement create(int type, String name) {
		SBDElement element = getConverter().getType(type);
		return element == null ? null : element.newCopy(name);
	}
	
	public static SBDElement getByName(String name) {
		return REGISTRY.get(name);
	}
	
	public static Collection<SBDElement> getRegisteredElements() {
		return REGISTRY.getEntries();
	}
	
	public static void registerElement(SBDElement element) {
		REGISTRY.register(element);
	}
	
	public static void updateTypeConverter() {
		DEFAULT_CONVERTER.createDefault();
	}
	
	public SBDElement getTypeByByte(int type) {
		return converter.getType(type);
	}

	public int getByteByTypeOrComplain(SBDElement element) throws IOException {
		int b = getByteByType(element);
		if (b == -1) {
			throw new IOException("Element type " + element.getFullTypeName() + " (impl. " + element.getClass() +
					", is not supported by current type conversion table. Is SBD file out-of-date?");
		}
		
		return b;
	}
	
	public int getByteByType(SBDElement element) {
		return converter.getByte(element);
	}






	private SBDStructure root = null;
	private SBDTypeConverter converter = DEFAULT_CONVERTER;
	
	public StructuredByteData(Supplier<OutputStream> output, SBDStructure root) {
		this(toOutputSupplier(output), root);
	}
	
	public StructuredByteData(OutputSupplier output, SBDStructure root) {
		super(output);
		this.root = root;
	}
	
	public StructuredByteData(OutputSupplier output) {
		this(output, new SBDStructure(ROOT_NAME));
	}
	
	public StructuredByteData(Supplier<OutputStream> output) {
		this(output, new SBDStructure(ROOT_NAME));
	}
	
	public StructuredByteData() {
		this((OutputSupplier) null, null);
	}
	
	public StructuredByteData(SBDStructure root) {
		this((OutputSupplier) null, root);
	}

	@Override
	public void save(OutputStream output) throws IOException {
		getConverter().write(output);
		getRoot().write(output, this);
	} 
	
	public static StructuredByteData read(InputStream is, Supplier<OutputStream> output)
			throws IOException {
		return read(is, toOutputSupplier(output));
	}
	
	public static StructuredByteData read(InputStream is, OutputSupplier output)
			throws IOException {
		StructuredByteData meta = new StructuredByteData(output);
		
		meta.readConverterTable(is);
		
		SBDStructure root = new SBDStructure(ROOT_NAME);
		root.read(is, meta, null);
		meta.setRoot(root);
		
		return meta;
	}
	
	public static StructuredByteData read(final File file)
			throws IOException {
		checkFile(file, true);
		InputStream is = getInputStream(file);
		StructuredByteData sbd = read(is, createSupplier(file));
		is.close();
		return sbd;
	}
	
	public static StructuredByteData readWithoutFeedback(final File file)
			throws IOException {
		checkFile(file, false);
		InputStream is = getInputStream(file);
		StructuredByteData sbd = read(is, (OutputSupplier) null);
		is.close();
		return sbd;
	}
	
	public static StructuredByteData read(byte[] bytes) throws IOException {
		return read(
				new ByteArrayInputStream(bytes),
				(Supplier<OutputStream>) null);
	}
	
	public static SBDStructure find(InputStream is, String... paths) throws IOException {
		SBDStructure struct = new SBDStructure(ROOT_NAME);
		Collection<char[]> newPaths = new LinkedList<char[]>();
		Collection<String> args = new LinkedList<String>();
		
		String[] parts;
		for (String s : paths) {
			parts = StringUtil.split(s, PATH_ARGUMENT_SEPARATOR, 2);
			newPaths.add(parts[0].toCharArray());
			
			if (parts[1] != null) {
				args.add(parts[1]);
			} else {
				args.add(null);
			}
		}
		
		StructuredByteData meta = new StructuredByteData(struct);
		meta.readConverterTable(is);
		
		SBDStructure.find(
				newPaths.iterator(),
				args.iterator(),
				0,
				new char[0],
				is,
				struct,
				meta,
				meta.getByteByTypeOrComplain(STRUCTURE_BASE));
		
		return struct;
	}
	
	public static SBDStructure readMeta(InputStream is) throws IOException {
		return find(is, META_NAME);
	}
	
	public static SBDStructure readMeta(File file) throws IOException {
		return readMeta(getInputStream(file));
	}

	public SBDStructure getRoot() {
		return root;
	}

	public void setRoot(SBDStructure root) {
		this.root = root;
	}

	public SBDTypeConverter getConverter() {
		return converter;
	}

	public void setConverter(SBDTypeConverter converter) {
		this.converter = converter;
	}
	
	public void readConverterTable(InputStream is) throws IOException {
		SBDTypeConverter converter = new SBDTypeConverter();
		converter.read(is);
		setConverter(converter);
	}

}
