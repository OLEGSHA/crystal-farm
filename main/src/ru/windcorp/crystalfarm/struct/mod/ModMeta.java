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
package ru.windcorp.crystalfarm.struct.mod;

import java.util.List;

import ru.windcorp.tge2.util.Version;

public class ModMeta {

	public final String name;
	public final String userFriendlyName;
	public final Version version;
	
	public final List<String> authors;
	public final String copyright;
	public final boolean isFree;
	
	public final String url;

	public ModMeta(String name, String userFriendlyName, Version version, List<String> authors, String copyright,
			boolean isFree, String url) {
		this.name = name;
		this.userFriendlyName = userFriendlyName;
		this.version = version;
		this.authors = authors;
		this.copyright = copyright;
		this.isFree = isFree;
		this.url = url;
	}

	@Override
	public String toString() {
		return "[name=" + name + ", userFriendlyName=" + userFriendlyName + ", version=" + version
				+ ", authors=" + authors + ", copyright=" + copyright + ", isFree=" + isFree + ", url=" + url + "]";
	}
	
}
