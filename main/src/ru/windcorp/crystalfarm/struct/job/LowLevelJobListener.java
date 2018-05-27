/**
 * Crystal Farm the game
 * Copyright (C) 2018  Crystal Farm Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ru.windcorp.crystalfarm.struct.job;

import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.jobs.JobListener;
import ru.windcorp.tge2.util.jobs.JobManager;

public class LowLevelJobListener implements JobListener<ModuleJob> {

	@Override
	public void onJobAdded(JobManager<? extends ModuleJob> manager, ModuleJob job) {
		Log.debug("Job " + job + " registered");
	}

	@Override
	public void onJobStarted(JobManager<? extends ModuleJob> manager, ModuleJob job, Thread worker) {
		Log.debug("Job " + job + " started");
	}

	@Override
	public void onJobDone(JobManager<? extends ModuleJob> manager, ModuleJob job, Thread worker) {
		Log.debug("Job " + job + " completed normally");
	}

	@Override
	public void onJobsBegin(JobManager<? extends ModuleJob> manager, int threads) {
		Log.info("Began execution of jobs with " + threads + " workers");
	}

	@Override
	public void onJobsEnd(JobManager<? extends ModuleJob> manager) {
		Log.info("Ended execution of jobs");
	}

	@Override
	public void onJobDependencyProblem(JobManager<? extends ModuleJob> manager, Thread worker) {
		ExecutionReport.reportCriticalError(null, null,
				"Dependency problem in load jobs (encountered by worker %s)", worker.getName());
	}

	@Override
	public void onJobErrored(JobManager<? extends ModuleJob> manager, Thread worker, ModuleJob job,
			Exception exception) {
		ExecutionReport.reportCriticalError(exception,
				ExecutionReport.rscCorrupt("Job " + job.toString(), "Job errored (encountered by worker %s)", worker.getName()),
				null);
	}

}
