package ru.windcorp.tge2.util.filefilters;

import java.io.File;
import java.io.FileFilter;

import ru.windcorp.tge2.util.interfaces.Filter;

public class TGE2FileFilter implements FileFilter {

	private final Filter<File> filter;
	
	public TGE2FileFilter(Filter<File> filter) {
		this.filter = filter;
	}

	@Override
	public boolean accept(File pathname) {
		return getFilter().accept(pathname);
	}

	public Filter<File> getFilter() {
		return filter;
	}

}
