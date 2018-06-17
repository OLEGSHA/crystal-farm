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
package ru.windcorp.crystalfarm.translation;

import java.io.IOException;

import ru.windcorp.crystalfarm.CrystalFarmResourceManagers;
import ru.windcorp.crystalfarm.struct.mod.ModRegistry;
import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class JobLoadTranslation extends ModuleJob {

	public JobLoadTranslation(Module module) {
		super("LoadTranslation", "Initially loads translation", module);
		
		addDependency("Inbuilt:Configuration:LoadConfig");
		addDependency("Inbuilt:ModLoader:LoadMods");
	}

	@Override
	protected void runImpl() {
		Log.info("Loading translations for " + ModuleTranslation.getLanguageCode() + "/" + ModuleTranslation.getLanguageFallbackCode() + "...");
		ModuleTranslation.reload();
		
		readLanguageList();
		
		Log.info(new TStringTranslated("welcome").toString());
	}

	private void readLanguageList() {
		try {
			CrystalFarmResourceManagers.RM_ASSETS.getResource("tran/langList")
					.lines()
					.forEach(line -> ModuleTranslation.getAvailableLanguages().add(line));
		} catch (IOException e) {
			ExecutionReport.reportError(e,
					ExecutionReport.rscCorrupt(CrystalFarmResourceManagers.RM_ASSETS.getCanonicalPath("tran/langList"), "Could not read main language index"),
					null);
			return;
		}
		
		ModRegistry.getMods().forEach((name, mod) -> {
			try {
				CrystalFarmResourceManagers.RM_ASSETS.getResource(name + "/tran/langList")
						.lines()
						.forEach(line -> {
							if (!ModuleTranslation.getAvailableLanguages().contains(line)) {
								ModuleTranslation.getAvailableLanguages().add(line);
							}
						});
			} catch (IOException e) {
				// Do nothing
			}
		});
	}

}
