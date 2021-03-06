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

public class TStringConcat extends TString {
	
	private final Object part0;
	private final Object[] parts;

	public TStringConcat(Object part0, Object... parts) {
		this.part0 = part0;
		this.parts = parts;
		
		listenForUpdates(part0);
		for (Object part : parts) {
			listenForUpdates(part);
		}
	}

	@Override
	protected String compute() {
		StringBuilder sb = new StringBuilder(String.valueOf(part0));
		for (Object part : parts) {
			sb.append(String.valueOf(part));
		}
		return sb.toString();
	}

}
