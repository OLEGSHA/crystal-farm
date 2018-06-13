package ru.windcorp.tge2.util.debug.er;

import ru.windcorp.tge2.util.debug.er.ExecutionReport.ProblemReport;

public class ExecutionReportBackendDefault implements ExecutionReportBackend {

	@Override
	public void requestExit(ProblemReport report) {
		System.exit(report.getProblemId().hashCode());
	}

}
