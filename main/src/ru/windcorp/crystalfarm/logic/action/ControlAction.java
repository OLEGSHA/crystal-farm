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
package ru.windcorp.crystalfarm.logic.action;

import ru.windcorp.crystalfarm.cfg.Setting;
import ru.windcorp.crystalfarm.input.KeyStroke;
import ru.windcorp.crystalfarm.struct.mod.Mod;

public abstract class ControlAction extends KeyAction {

	private final Setting<KeyStroke> setting;

	public ControlAction(Mod mod, String name, boolean isLocal, Setting<KeyStroke> setting) {
		super(mod, name, isLocal);
		this.setting = setting;
	}
	
	public ControlAction(Mod mod, String name, String description, KeyStroke defaultKey, boolean isLocal) {
		this(mod, name, isLocal, new Setting<KeyStroke>(name, description, KeyStroke.class, defaultKey));
	}
	
	public ControlAction(Mod mod, String name, String description, String key, boolean isLocal) {
		this(mod, name, description, KeyStroke.fromString(key), isLocal);
	}

	public Setting<KeyStroke> getSetting() {
		return setting;
	}
	
	@Override
	public KeyStroke getKeyStroke() {
		return getSetting().get();
	}
	
}
