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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Music implements Runnable {
	
	public static final List<Sound> PLAYLIST = Collections.synchronizedList(new ArrayList<>());

	@Override
	public void run() {
		for (String name : new String[] {"rooster", "Rondo_Alla_Turka"}) {
			PLAYLIST.add(SoundManager.get(name));
		}
		AudioInterface.play(PLAYLIST.get(0), 1.0f);
		
		AudioInterface.play(PLAYLIST.get(1), 1.0f);
	}
	
}
