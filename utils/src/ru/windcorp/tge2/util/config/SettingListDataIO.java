package ru.windcorp.tge2.util.config;

import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ru.windcorp.tge2.fileio.data.sbd.SBDStructure;
import ru.windcorp.tge2.util.dataio.ByteArrayBuilder;
import ru.windcorp.tge2.util.dataio.ByteArrayDataReader;
import ru.windcorp.tge2.util.dataio.Data;
import ru.windcorp.tge2.util.interfaces.Supplier;

public class SettingListDataIO<T extends Data> extends Setting<byte[]> {
	
	private class UpdatingList extends AbstractList<T> {
		private final ArrayList<T> implementor = new ArrayList<T>();

		@Override
		public T get(int arg0) {
			return implementor.get(arg0);
		}

		@Override
		public int size() {
			return implementor.size();
		}
		
		@Override
		public T set(int arg0, T arg1) {
			T result = implementor.set(arg0, arg1);
			if (result != arg1) update();
			return result;
		}
		
		@Override
		public boolean add(T arg0) {
			if (implementor.add(arg0)) {
				update();
				return true;
			}
			return false;
		}
		
		@Override
		public boolean remove(Object arg0) {
			if (implementor.remove(arg0)) {
				update();
				return true;
			}
			return false;
		}
		
		@Override
		public void clear() {
			implementor.clear();
			update();
		}
		
		public synchronized void reset() {
			implementor.clear();
		}
		
		@Override
		public String toString() {
			return implementor.toString();
		}
	}
	
	private final UpdatingList rawList = new UpdatingList();
	
	private final List<T> list = Collections.synchronizedList(rawList);
	
	private final Supplier<T> generator;

	public SettingListDataIO(String name, Supplier<T> generator, Collection<? extends T> defaultValue) {
		super(name, byte[].class, createDefaultValue(defaultValue));
		this.generator = generator;
	}
	
	private static <T extends Data> byte[] createDefaultValue(Collection<? extends T> defaultValue) {
		if (defaultValue == null) {
			return new byte[0];
		}
		
		ByteArrayBuilder bab = new ByteArrayBuilder();
		
		for (T v : defaultValue) {
			bab.write(v);
		}
		
		return bab.toArray();
	}

	protected void update() {
		ByteArrayBuilder bab = new ByteArrayBuilder(getValue().length);
		
		synchronized (getList()) {
			for (T obj : getList()) {
				bab.write(obj);
			}
		}
		
		setValue(bab.toArray());
	}
	
	public List<T> getList() {
		return list;
	}
	
	@Override
	public synchronized void load(SBDStructure structure) throws IOException {
		synchronized (getList()) {
			super.load(structure);
			
			rawList.reset();
			ByteArrayDataReader reader = new ByteArrayDataReader(getValue());
			
			try {
				while (reader.hasMore()) {
					getList().add(reader.read(generator.supply()));
				}
			} catch (IOException e) {
				throw new IOException("Could not read element of list " + getName(), e);
			}
		}
	}

}
