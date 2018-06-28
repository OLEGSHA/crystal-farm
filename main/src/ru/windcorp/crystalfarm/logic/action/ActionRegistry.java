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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ru.windcorp.crystalfarm.cfg.Section;
import ru.windcorp.crystalfarm.input.KeyInput;
import ru.windcorp.tge2.util.Nameable;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public class ActionRegistry extends Nameable {
	
	public static final Section ROOT_SECTION = new Section("Controls", "Keyboard mappings for all controls");
	
	public static final ActionRegistry IN_GAME = new ActionRegistry("InGame");

	private final Map<String, Action> actions = Collections.synchronizedMap(new HashMap<>());
	private final Section section;
	
	public ActionRegistry(String name) {
		super(name);
		this.section = new Section(name, "Keyboard mappings for controls " + name);
		ROOT_SECTION.add(getSection());
	}
	
	public Map<String, Action> getActionMap() {
		return actions;
	}
	
	public Section getSection() {
		return section;
	}
	
	public void register(Action action) {
		Action previous = getActionMap().put(action.getName(), action);
		
		if (previous != null) {
			ExecutionReport.reportError(null, null, "Action with name %s has already been registered in %s", action.getName(), this.toString());
			// Continue
		}
		
		if (action instanceof ControlAction) {
			getSection().add(((ControlAction) action).getSetting());
		}
	}
	
	public void register(Action... actions) {
		for (Action a : actions) {
			register(a);
		}
	}
	
	public Action getAction(String name) {
		return getActionMap().get(name);
	}
	
	public KeyAction getAction(KeyInput input) {
		synchronized (getActionMap()) {
			for (Entry<String, Action> entry : getActionMap().entrySet()) {
				if (entry.getValue() instanceof KeyAction) {
					KeyAction action = (KeyAction) entry.getValue();
					
					if (action.isEnabled() && action.matches(input)) {
						return action;
					}
				}
			}
		}
		
		return null;
	}

}
