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

import java.io.PrintWriter;
import java.io.StringWriter;

public interface CommandRunner {
	
	String getName();
	void respond(String msg);
	void complain(String msg);
	
	default void respond(Object obj) {
		respond(String.valueOf(obj));
	}
	
	default void respond(String format, Object... args) {
		respond(String.format(format, args));
	}
	
	default void complain(Object obj) {
		complain(String.valueOf(obj));
	}
	
	default void complain(String format, Object... args) {
		complain(String.format(format, args));
	}
	
	default void reportException(Exception e) {
		StringWriter buffer = new StringWriter();
		PrintWriter writer = new PrintWriter(buffer);
		e.printStackTrace(writer);
		complain(buffer.toString());
	}

}
