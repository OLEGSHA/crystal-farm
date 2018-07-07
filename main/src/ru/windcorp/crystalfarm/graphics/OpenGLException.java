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
package ru.windcorp.crystalfarm.graphics;

public class OpenGLException extends RuntimeException {

	private static final long serialVersionUID = -2075910827911613055L;
	
	private final int errorCode;

	public OpenGLException(int errorCode) {
		super("OpenGL error detected: " + GraphicsInterface.getOpenGLErrorDescription(errorCode));
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

}
