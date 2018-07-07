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

import static ru.windcorp.crystalfarm.audio.AudioInterface.*;
import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;

import org.lwjgl.Version;

import ru.windcorp.tge2.util.debug.er.ExecutionReport.DebugValueSection;

public class LWJGLValueSection extends DebugValueSection {

	public LWJGLValueSection() {
		super("LWJGL");
	}
	
	@Override
	public String[] getNames() {
		return new String[] {
				"LWJGL version",
				"Graphics ready",
				"OpenGL version",
				"Last OpenGL error code",
				"Audio ready",
				"GLFW version"
		};
	}
	
	@Override
	public Object[] getValues() {
		return new String[] {
				Version.getVersion(),
				String.valueOf(isGraphicsReady()),
				getHighestGLVersion(),
				isGraphicsReady() ? getOpenGLErrorDescription(getLastError()) : "N/A",
				String.valueOf(isAudioReady()),
				glfwGetVersionString()
		};
	}

	private String getHighestGLVersion() {
		if (getGlCapabilities() == null) {
			return "Context not created";
		} else if (getGlCapabilities().OpenGL46) {
			return "4.6";
		} else if (getGlCapabilities().OpenGL45) {
			return "4.5";
		} else if (getGlCapabilities().OpenGL44) {
			return "4.4";
		} else if (getGlCapabilities().OpenGL43) {
			return "4.3";
		} else if (getGlCapabilities().OpenGL42) {
			return "4.2";
		} else if (getGlCapabilities().OpenGL41) {
			return "4.1";
		} else if (getGlCapabilities().OpenGL40) {
			return "4.0";
		} else if (getGlCapabilities().OpenGL33) {
			return "3.3";
		} else if (getGlCapabilities().OpenGL32) {
			return "3.2";
		} else if (getGlCapabilities().OpenGL31) {
			return "3.1";
		} else if (getGlCapabilities().OpenGL30) {
			return "3.0";
		} else if (getGlCapabilities().OpenGL21) {
			return "2.1";
		} else if (getGlCapabilities().OpenGL20) {
			return "2.0";
		} else if (getGlCapabilities().OpenGL15) {
			return "1.5";
		} else if (getGlCapabilities().OpenGL14) {
			return "1.4";
		} else if (getGlCapabilities().OpenGL13) {
			return "1.3";
		} else if (getGlCapabilities().OpenGL12) {
			return "1.2";
		} else if (getGlCapabilities().OpenGL11) {
			return "1.1";
		} else {
			return "1.0";
		}
	}

}
