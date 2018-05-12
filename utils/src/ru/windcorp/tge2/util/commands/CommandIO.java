package ru.windcorp.tge2.util.commands;

public interface CommandIO {
	
	public void write(String text);
	public void writeError(String text);
	public String readCommand();

}
