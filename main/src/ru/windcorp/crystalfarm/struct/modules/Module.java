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
package ru.windcorp.crystalfarm.struct.modules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import ru.windcorp.crystalfarm.cfg.ConfigurationNode;
import ru.windcorp.crystalfarm.cfg.Section;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.tge2.util.Nameable;
import ru.windcorp.tge2.util.jobs.JobManager;

public abstract class Module extends Nameable {
	
	private final Mod mod;
	
	private final Collection<ConfigurationNode> configurationNodes = Collections.synchronizedCollection(new ArrayList<>());
	
	public Module(String name, Mod mod) {
		super(name);
		this.mod = mod;
	}

	public Mod getMod() {
		return mod;
	}

	protected <T extends ConfigurationNode> T addConfig(T node) {
		configurationNodes.add(node);
		return node;
	}
	
	public void registerConfiguration(Section section) {
		configurationNodes.forEach(node -> section.add(node));
	}
	
	public abstract void registerJobs(JobManager<ModuleJob> manager);
	
	@Override
	public String toString() {
		return getMod().getName() + ":" + getName();
	}

}
