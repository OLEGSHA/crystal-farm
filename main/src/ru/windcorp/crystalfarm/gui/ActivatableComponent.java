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
package ru.windcorp.crystalfarm.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public abstract class ActivatableComponent extends Component implements Consumer<Object> {
	
	public ActivatableComponent(String name, Consumer<?> action) {
		super(name);
		addAction(action);
	}

	private final Collection<Consumer<?>> actions = Collections.synchronizedCollection(new ArrayList<>());
	
	public ActivatableComponent addAction(Consumer<?> action) {
		if (action != null) getActions().add(action);
		return this;
	}
	
	public ActivatableComponent removeAction(Consumer<?> action) {
		getActions().remove(action);
		return this;
	}
	
	public Collection<Consumer<?>> getActions() {
		return actions;
	}
	
	/**
	 * Activates this component and dispatches event to action listeners.
	 * @param t igonred
	 */
	@Override
	public void accept(Object t) {
		getActions().forEach(consumer -> consumer.accept(null));
	}
	
}
