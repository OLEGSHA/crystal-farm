package ru.windcorp.tge2.util.debug.er;

import java.io.IOException;

import ru.windcorp.tge2.util.debug.er.ExecutionReport.DebugValueSection;
import static ru.windcorp.tge2.util.debug.er.ExecutionReport.*;

public class ExecutionReportProblemAnalysisSection extends DebugValueSection {

	public ExecutionReportProblemAnalysisSection() {
		super("PROBLEM ANALYSIS");
	}

	@Override
	public String[] getNames() {
		return new String[] {
				"Problems reported",
				"Exceptions",
				"Uncaught exceptions",
				"JVM security issues",
				"JVM security manager",
				"Input/Output issues"
		};
	}

	@Override
	public Object[] getValues() {
		int problems = getProblems().size(),
				exceptions = 0,
				securityExceptions = 0,
				ioExceptions = 0,
				
				uncaught = 0;
		
		for (ProblemReport e : getProblems()) {
			switch (e.getType()) {
			case UNCAUGHT:
				uncaught++;
				break;
				
			default:
				// Do nothing
				break;
			}
			
			if (e.getThrowable() != null) {
				exceptions++;
				
				if (e.getThrowable() instanceof SecurityException) {
					securityExceptions++;
				} else if (e.getThrowable() instanceof IOException) {
					ioExceptions++;
				}
			}
		}
		
		return new Object[] {
				numOrNone(problems, "no problems reported"),
				
				problems == 0 ? " --- " : numOrNone(exceptions, "no exceptions reported"),
						
				problems == 0 ? " --- " : numOrNone(uncaught, "no uncaught exceptions") +
						" (uncaught exceptions indicate rare problems or glitches)",
						
				problems == 0 ? " --- " : numOrNone(securityExceptions, "no security exceptions reported"),
						
				System.getSecurityManager() == null ? "not present" : "present; class " +
						System.getSecurityManager().getClass() + "; object " + System.getSecurityManager(),
						
				problems == 0 ? " --- " : numOrNone(ioExceptions, "no IO exceptions reported"),
		};
	}
	
	private static String numOrNone(int number, String text) {
		if (number == 0) {
			return text;
		}
		
		return Integer.toString(number);
	}

}
