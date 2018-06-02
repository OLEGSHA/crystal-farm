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

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import ru.windcorp.crystalfarm.CrystalFarm;
import ru.windcorp.tge2.util.StringUtil;
import ru.windcorp.tge2.util.Version;

public class ModMeta {
	
	public static final String MF_ATTRIBUTES = CrystalFarm.INTERNAL_NAME;

	public static final String MF_NAME = "Internal-Name";
	public final String name;
	
	public static final String MF_UF_NAME = "UF-Name";
	public final String userFriendlyName;
	
	public static final String MF_VERSION = "Mod-Version";
	public final Version version;
	
	public static final String MF_AUTHORS = "Authors";
	public final List<String> authors;
	
	public static final String MF_COPYRIGHT = "Copyright";
	public final String copyright;
	
	public static final String MF_IS_FREE = "Is-Free";
	public final boolean isFree;
	
	public static final String MF_URL = "Url";
	public final String url;
	
	public static final String MF_MOD_CLASS = "Mod-Class";
	public final String modClass;

	protected ModMeta(String name, String userFriendlyName, Version version, List<String> authors, String copyright,
			boolean isFree, String url, String modClass) {
		this.name = name;
		this.userFriendlyName = userFriendlyName;
		this.version = version;
		this.authors = authors;
		this.copyright = copyright;
		this.isFree = isFree;
		this.url = url;
		this.modClass = modClass;
	}
	
	public ModMeta(String name, String userFriendlyName, Version version, List<String> authors, String copyright,
			boolean isFree, String url, Class<? extends Mod> modClass) {
		this(name, userFriendlyName, version, authors, copyright, isFree, url, modClass.getName());
	}
	
	public static ModMeta parse(Manifest manifest) {
		Attributes a = manifest.getAttributes(MF_ATTRIBUTES);
		
		String name, userFriendlyName, versionString, authorsString, copyright, isFreeString, url, modClass;
		Version version;
		List<String> authors = new ArrayList<>();
		boolean isFree;
		
		name = a.getValue(MF_NAME);
		if (name == null) {
			return null;
		}
		
		userFriendlyName = a.getValue(MF_UF_NAME);
		if (userFriendlyName == null) {
			userFriendlyName = name;
		}
		
		versionString = a.getValue(MF_VERSION);
		if (versionString == null) {
			return null;
		}
		try {
			version = new Version(versionString);
		} catch (NumberFormatException e) {
			return null;
		}
		
		authorsString = a.getValue(MF_AUTHORS);
		if (authorsString == null) {
			authors.add("Anonymous");
		}
		for (String author : StringUtil.split(authorsString, ';')) {
			authors.add(author.trim());
		}
		
		isFreeString = a.getValue(MF_IS_FREE);
		if (isFreeString == null) {
			return null;
		} else if ("true".equals(isFreeString)) {
			isFree = true;
		} else if ("false".equals(isFreeString)) {
			isFree = false;
		} else {
			return null;
		}
		
		copyright = a.getValue(MF_COPYRIGHT);
		if (copyright == null) {
			if (isFree) {
				copyright = "GNU General Public License version 3 or any later version";
			} else {
				copyright = "All rights reserved";
			}
			
			copyright = copyright + " [implicit]";
		}
		
		url = a.getValue(MF_URL);
		// URL can be null
		
		modClass = a.getValue(MF_MOD_CLASS);
		if (modClass == null) {
			return null;
		}
		
		return new ModMeta(name, userFriendlyName, version, authors, copyright, isFree, url, modClass);
	}

	@Override
	public String toString() {
		return "[name=" + name + ", userFriendlyName=" + userFriendlyName + ", version=" + version
				+ ", authors=" + authors + ", copyright=" + copyright + ", isFree=" + isFree + ", url=" + url + "]";
	}
	
}
