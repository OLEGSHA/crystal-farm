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

import ru.windcorp.crystalfarm.graphics.notifier.Notification;
import ru.windcorp.crystalfarm.graphics.notifier.Notifier;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.debug.er.ExecutionReport.DamagedResourceReport;
import ru.windcorp.tge2.util.debug.er.ExecutionReport.ProblemReport;
import ru.windcorp.tge2.util.debug.er.ExecutionReportListener;

public class CrystalFarmExecutionReportListener implements ExecutionReportListener {

	@Override
	public void onProblem(ProblemReport report) {
		Notifier.postNotification(new Notification(
				getType(report.getType()),
				false,
				null,
				"executionReport.notification.onProblem." + report.getType().name().toLowerCase(),
				report.getProblemId(), report.getDescription()));
	}

	@Override
	public void onDamagedResource(DamagedResourceReport report) {
		Notifier.postNotification(new Notification(
				getType(report.getLevel()),
				false,
				null,
				"executionReport.notification.onDamagedResource",
				report.getResource(), report.getDescription()));
	}
	
	public static Notification.Type getType(ExecutionReport.ProblemReport.ProblemLevel level) {
		switch (level) {
		case CRITICAL_ERROR:
		case UNCAUGHT:
		case ERROR:
		default:
			return Notification.Type.ERROR;
		case DEBUG:
			return Notification.Type.DEBUG;
		case NOTIFICATION:
			return Notification.Type.INFO_NEUTRAL;
		case WARNING:
			return Notification.Type.WARNING;
		}
	}

}
