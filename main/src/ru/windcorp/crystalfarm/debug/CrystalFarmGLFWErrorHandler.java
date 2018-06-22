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
package ru.windcorp.crystalfarm.debug;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;

import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class CrystalFarmGLFWErrorHandler implements GLFWErrorCallbackI {

	@Override
	public void invoke(int error, long descriptionPointer) {
		ExecutionReport.reportCriticalError(null, null,
				"GLFW error encountered. Error code: %d, error name: %s, error name explanation: %s, provided description: %s",
				error,
				getErrorName(error),
				getErrorDescription(error),
				GLFWErrorCallback.getDescription(descriptionPointer));
	}

	private String getErrorName(int error) {
		switch (error) {
		case GLFW_NO_ERROR:				return "NO_ERROR";
		case GLFW_NOT_INITIALIZED:		return "NOT_INITIALIZED";
		case GLFW_NO_CURRENT_CONTEXT:	return "NO_CURRENT_CONTEXT";
		case GLFW_INVALID_ENUM:			return "INVALID_ENUM";
		case GLFW_INVALID_VALUE:		return "INVALID_VALUE";
		case GLFW_OUT_OF_MEMORY:		return "OUT_OF_MEMORY";
		case GLFW_API_UNAVAILABLE:		return "API_UNAVAILABLE";
		case GLFW_VERSION_UNAVAILABLE:	return "VERSION_UNAVAILABLE";
		case GLFW_PLATFORM_ERROR:		return "PLATFORM_ERROR";
		case GLFW_FORMAT_UNAVAILABLE:	return "FORMAT_UNAVAILABLE";
		case GLFW_NO_WINDOW_CONTEXT:	return "NO_WINDOW_CONTEXT";
		default:						return "unknown error code " + error;
		}
	}

	private String getErrorDescription(int error) {
		switch (error) {
		case GLFW_NO_ERROR:				return "No errors have occurred.";
		case GLFW_NOT_INITIALIZED:		return "GLFW has not been initialized. This is a bug.";
		case GLFW_NO_CURRENT_CONTEXT:	return "GLFW could not find an OpenGL context. OpenGL is OK, this is a bug.";
		case GLFW_INVALID_ENUM:			return "An invalid setting has been used. This is a bug.";
		case GLFW_INVALID_VALUE:		return "An invalid value has been used. This is a bug.";
		case GLFW_OUT_OF_MEMORY:		return "A bug in GLFW or the underlying operating system. Or you're low on RAM.";
		case GLFW_API_UNAVAILABLE:		return "OpenGL is unavailable. Is your machine capable of using it?";
		case GLFW_VERSION_UNAVAILABLE:	return "OpenGL is available but too old.";
		case GLFW_PLATFORM_ERROR:		return "A platform-specific error occurred that does not match any of the more specific categories. Probably a bug.";
		case GLFW_FORMAT_UNAVAILABLE:	return "A feature is not available.";
		case GLFW_NO_WINDOW_CONTEXT:	return "The window does not have an OpenGL context.";
		default:						return "?";
		}
	}

}
