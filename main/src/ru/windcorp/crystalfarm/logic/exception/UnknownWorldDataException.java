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
package ru.windcorp.crystalfarm.logic.exception;

import ru.windcorp.crystalfarm.logic.server.World;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public class UnknownWorldDataException extends SyntaxException {

	private static final long serialVersionUID = 3194705989421919878L;
	
	private final String name;
	private final World world;
	
	public UnknownWorldDataException(String arg0, String name, World world) {
		super(arg0);
		this.name = name;
		this.world = world;
	}

	public String getName() {
		return name;
	}

	public World getWorld() {
		return world;
	}
	
}
