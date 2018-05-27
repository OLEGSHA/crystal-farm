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

import java.util.stream.Collectors;

import ru.windcorp.crystalfarm.struct.mod.ModMeta;
import ru.windcorp.crystalfarm.struct.mod.ModRegistry;
import ru.windcorp.crystalfarm.struct.modules.ModuleRegistry;
import ru.windcorp.tge2.util.debug.er.ExecutionReport.ReportPart;
import ru.windcorp.tge2.util.textui.TUITable;

public class ModReportPart extends ReportPart {

	public ModReportPart() {
		super("MODS");
	}

	@Override
	protected void printBody(StringBuilder b) {
		synchronized (ModRegistry.getMods()) {
			
			TUITable table = new TUITable("Name", "UF Name", "Version", "Modules", "URL");
			
			ModRegistry.getMods().forEach((name, mod) -> {
				
				ModMeta meta = mod.getMetadata();
				
				table.addRow(
						name,
						meta.userFriendlyName,
						meta.version,
						ModuleRegistry.getModulesByMod(mod)
								.map(m -> m.toString())
								.collect(Collectors.joining(" ")),
						meta.url == null ? "-" : meta.url);
				
			});

			b.append('\n');
			b.append(table.toString());
			
		}
	}

}
