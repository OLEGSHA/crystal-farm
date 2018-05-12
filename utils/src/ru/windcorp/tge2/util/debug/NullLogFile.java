package ru.windcorp.tge2.util.debug;

import java.io.PrintStream;

import ru.windcorp.tge2.util.NullOutputStream;

public class NullLogFile extends LogFile {

	public NullLogFile() {
		super(new PrintStream(new NullOutputStream()));
	}

}
