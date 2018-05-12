package ru.windcorp.tge2.util.commands;

import ru.windcorp.tge2.util.debug.ConsoleIO;
import ru.windcorp.tge2.util.debug.Log;

public class LogCommandIO implements CommandIO {

	@Override
	public void write(String text) {
		Log.info(text);
	}

	@Override
	public void writeError(String text) {
		Log.error(text);
	}

	@Override
	public String readCommand() {
		return ConsoleIO.readLine();
	}

}
