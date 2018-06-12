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
package ru.windcorp.crystalfarm.graphics.notifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import ru.windcorp.crystalfarm.graphics.Layer;

public class NotifierLayer extends Layer {

	private final Collection<Notification> notesOnScreen = Collections.synchronizedCollection(new ArrayList<>());
	
	int nextY = 0;

	public NotifierLayer() {
		super("Notifier");
	}
	
	public void show(Notification note) {
		note.show(this);
		getNotesOnScreen().add(note);
	}
	
	public void hide(Notification note) {
		getNotesOnScreen().remove(note);
	}

	public Collection<Notification> getNotesOnScreen() {
		return notesOnScreen;
	}

	@Override
	public void render() {
		getNotesOnScreen().forEach(note -> note.render());
	}

}
