/**
 * Copyright (C) 2019 OLEGSHA
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
package ru.windcorp.tge2.util.ncmd;

public class CommandSyntaxException extends CommandExceptions {

	private static final long serialVersionUID = -6425598826469290735L;

	public CommandSyntaxException(Invocation inv, String message) {
		super(inv, message);
	}

	public CommandSyntaxException(Invocation inv, String message, Throwable cause) {
		super(inv, message, cause);
	}

}
