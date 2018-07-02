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

public class OutOfDynIdsException extends RuntimeException {

	private static final long serialVersionUID = -5395655307926236803L;

	public OutOfDynIdsException() {
		
	}

	public OutOfDynIdsException(String arg0) {
		super(arg0);
	}

	public OutOfDynIdsException(Throwable arg0) {
		super(arg0);
	}

	public OutOfDynIdsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public OutOfDynIdsException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
