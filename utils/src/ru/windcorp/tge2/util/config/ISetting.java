package ru.windcorp.tge2.util.config;

import java.io.IOException;

import ru.windcorp.tge2.fileio.data.sbd.SBDStructure;

public interface ISetting<T> {

	public String getName();
	
	public Class<T> getContentType();
	public T getValue();
	public T getValueOr(T def);
	
	public void setValue(T value);
	
	public T getDefaultValue();
	public void setDefaultValue(T def);
	
	public void load(SBDStructure struct) throws IOException;
	public String getLastProblem();

}
