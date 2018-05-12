package ru.windcorp.tge2.util.filefilters;

import java.io.File;
import java.io.FileFilter;

public class DirectoryFilter implements FileFilter {

	private final boolean acceptDirectories;
	
	public DirectoryFilter(boolean acceptDirectories) {
		this.acceptDirectories = acceptDirectories;
	}
	
	public boolean acceptsDirectories() {
		return acceptDirectories;
	}
	
	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory() == acceptsDirectories();
	}

}
