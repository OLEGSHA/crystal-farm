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
package ru.windcorp.crystalfarm.audio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.windcorp.crystalfarm.CrystalFarmResourceManagers;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.grh.Resource;

public class Music implements Runnable {
	
	public static final List<Sound> PLAYLIST = Collections.synchronizedList(new ArrayList<>());

	@Override
	public void run() {
		if (load()) {
			return;
		}
		
		switch (PLAYLIST.size()) {
		case 0:
			ExecutionReport.reportNotification(null, null,
					"No music has been found");
			return;
		case 1:
			while (true) AudioInterface.playCompletely(PLAYLIST.get(0), 1);
		default:
			Random random = new Random();
			int pos = random.nextInt(PLAYLIST.size());
			int newPos;
			
			while (true) {
				AudioInterface.playCompletely(PLAYLIST.get(pos), 1);
				
				newPos = random.nextInt(PLAYLIST.size() - 1);
				if (newPos >= pos) {
					pos = newPos + 1;
				} else {
					pos = newPos;
				}
			}
		}
	}

	private boolean load() {
		Resource musicNames = CrystalFarmResourceManagers.RM_ASSETS.getResource("audio/music/musiclist");
		String problem = musicNames.canRead();
		if (problem != null) {
			ExecutionReport.reportError(null,
					ExecutionReport.rscCorrupt(musicNames.toString(), "Could not read musiclist: %s", problem),
					null);
			return true;
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(musicNames.getInputStream()));
			
			reader.lines().forEachOrdered(line -> {
				PLAYLIST.add(SoundManager.get("music/" + line));
			});
		} catch (IOException e) {
			ExecutionReport.reportCriticalError(e,
					ExecutionReport.rscCorrupt(musicNames.toString(), "Could not read musiclist"),
					null);
		}
		
		return false;
		
	}
	
}
