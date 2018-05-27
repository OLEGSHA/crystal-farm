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

import java.util.ArrayList;
import java.util.List;

import ru.windcorp.crystalfarm.struct.mod.ModRegistry;
import ru.windcorp.tge2.util.debug.er.ExecutionReport.SuggestionProvider;

public class ModlistSuggestionProvider extends SuggestionProvider {

	public ModlistSuggestionProvider() {
		super("Modlist analyzer");
	}

	@Override
	public List<String> createSuggestions() {
		ArrayList<String> list = new ArrayList<>();
		
		if (!list.isEmpty()) {
			list.add("Mods are present.");
			
			ModRegistry.getMods().forEach((name, mod) -> {
				
				if (!mod.getMetadata().isFree) {
					list.add("Mod " + mod.getMetadata().userFriendlyName + " is non-free. Non-free code is effectively impossible to debug. "
							+ "Remove non-free mods or consult your vendor in case of trouble.");
				}
				
			});
			
		}
		
		return list;
	}

}
