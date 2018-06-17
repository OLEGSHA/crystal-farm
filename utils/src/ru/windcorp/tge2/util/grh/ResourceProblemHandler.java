package ru.windcorp.tge2.util.grh;

import java.io.IOException;

@FunctionalInterface
public interface ResourceProblemHandler {

	public boolean onProblem(String problem) throws RuntimeException, IOException;
	
}
