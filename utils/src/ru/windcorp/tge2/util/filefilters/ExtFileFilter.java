package ru.windcorp.tge2.util.filefilters;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;

public class ExtFileFilter implements FileFilter {
	
	private final String[] exts;

	public ExtFileFilter(String... exts) {
		this.exts = exts;
		
		for (int i = 0; i < exts.length; ++i) {
			if (!exts[i].startsWith(".")) {
				exts[i] = "." + exts[i];
			}
		}
	}
	
	public ExtFileFilter(Collection<String> exts) {
		this(exts.toArray(new String[0]));
	}
	
	public String[] getExtensions() {
		return exts;
	}

	@Override
	public boolean accept(File pathname) {
		if (!pathname.isFile())
			return false;
		
		String name = pathname.getName();
		for (String s : getExtensions()) {
			if (name.endsWith(s)) {
				return true;
			}
		}
		
		return false;
	}

}
