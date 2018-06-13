package ru.windcorp.tge2.util.debug.er;

import ru.windcorp.tge2.util.debug.er.ExecutionReport.ProblemReport;

public interface ExecutionReportBackend {

	public void requestExit(ProblemReport report);

}
