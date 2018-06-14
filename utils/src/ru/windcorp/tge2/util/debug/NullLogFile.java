package ru.windcorp.tge2.util.debug;

import java.io.PrintStream;

import ru.windcorp.tge2.util.stream.NullOutputStream;

public class NullLogFile extends LogFile {

	public NullLogFile() {
		super(new PrintStream(new NullOutputStream()));
	}

}
