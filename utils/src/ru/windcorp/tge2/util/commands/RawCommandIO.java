package ru.windcorp.tge2.util.commands;

import ru.windcorp.tge2.util.debug.ConsoleIO;

public class RawCommandIO implements CommandIO {

	@Override
	public void write(String text) {
		System.out.println(text);
	}

	@Override
	public void writeError(String text) {
		System.err.println(text);
	}

	@Override
	public String readCommand() {
		return ConsoleIO.readLine();
	}

}
