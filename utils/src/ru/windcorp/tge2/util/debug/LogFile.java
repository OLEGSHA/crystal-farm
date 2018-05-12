package ru.windcorp.tge2.util.debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class LogFile {
	
	private final PrintStream printStream;
	
	public LogFile(PrintStream ps) {
		this.printStream = ps;
	}
	
	public LogFile(OutputStream outputStream) {
		this(new PrintStream(outputStream));
	}
	
	public LogFile(File file) throws FileNotFoundException, SecurityException {
		this(new FileOutputStream(file, false));
	}

	public PrintStream getPrintStream() {
		return printStream;
	}

}
