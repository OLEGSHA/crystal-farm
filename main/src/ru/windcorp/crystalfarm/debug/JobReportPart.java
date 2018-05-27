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
package ru.windcorp.crystalfarm.debug;

import ru.windcorp.crystalfarm.CrystalFarmLauncher;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.debug.er.ExecutionReport.ReportPart;
import ru.windcorp.tge2.util.jobs.Job;
import ru.windcorp.tge2.util.textui.TUITable;

public class JobReportPart extends ReportPart {
	
	public JobReportPart() {
		super("LOAD JOBS");
	}

	@Override
	protected void printBody(StringBuilder b) {
		if (!CrystalFarmLauncher.doesJobManagerExist()) {
			b.append("\n  No JobManager accessible");
			
			return;
		}
		
		TUITable table = new TUITable("Status", "Name", "Can run?", "Dependencies");
		
		CrystalFarmLauncher.getJobManager().getJobs().forEach(job -> {
			
			table.addRow(
					getStatus(job),
					job.toString(),
					getCanRun(job),
					getDependencies(job));
			
		});
		
		b.append('\n');
		b.append(table);
	}

	private String getCanRun(ModuleJob job) {
		if (job.getState() != Job.STATE_SCHEDULED) {
			return "-";
		}
		
		return job.debugCanRun(CrystalFarmLauncher.getJobManager(), "Yes");
	}

	private String getDependencies(ModuleJob job) {
		StringBuilder sb = new StringBuilder();
		job.getDependencies().forEach(dep -> sb.append(" " + dep));
		return sb.toString().trim();
	}

	private String getStatus(ModuleJob job) {
		switch (job.getState()) {
		case Job.STATE_NOT_SCHEDULED: return "NOT SCHEDULED";
		case Job.STATE_SCHEDULED: return "scheduled";
		case Job.STATE_EXECUTING: return "EXECUTING";
		case Job.STATE_EXECUTED: return "executed";
		case Job.STATE_ERRORED: return "ERRORED";
		default: return "UNKNOWN: " + job.getState();
		}
	}

}
