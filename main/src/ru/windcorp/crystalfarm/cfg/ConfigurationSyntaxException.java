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
package ru.windcorp.crystalfarm.cfg;

public class ConfigurationSyntaxException extends RuntimeException {

	private static final long serialVersionUID = -9082099905124303713L;
	
	private ConfigurationNode source;

	public ConfigurationSyntaxException(String message, Throwable cause, ConfigurationNode source) {
		super(message, cause);
		this.source = source;
	}
	
	public ConfigurationSyntaxException(String message, ConfigurationNode source) {
		super(message);
		this.source = source;
	}
	
	public ConfigurationSyntaxException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ConfigurationSyntaxException(String message) {
		super(message);
	}

	public ConfigurationNode getSource() {
		return source;
	}
	
	public void setSource(ConfigurationNode source) {
		this.source = source;
	}

}
