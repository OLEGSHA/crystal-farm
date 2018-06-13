package ru.windcorp.tge2.util.debug.er;

import ru.windcorp.tge2.util.debug.er.ExecutionReport.DamagedResourceReport;
import ru.windcorp.tge2.util.debug.er.ExecutionReport.ProblemReport;

public interface ExecutionReportListener {

	public void onProblem(ProblemReport report);
	public void onDamagedResource(DamagedResourceReport report);
	
}
