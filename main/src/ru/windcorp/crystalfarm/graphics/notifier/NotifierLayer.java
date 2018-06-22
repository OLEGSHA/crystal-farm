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

import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.*;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import ru.windcorp.crystalfarm.graphics.InputListener;
import ru.windcorp.crystalfarm.graphics.Layer;
import ru.windcorp.crystalfarm.input.Input;
import ru.windcorp.crystalfarm.input.MouseButtonInput;

public class NotifierLayer extends Layer implements InputListener {

	private final Collection<Notification> notesOnScreen = new ConcurrentLinkedQueue<>();
	
	int nextY = gdGetLine();

	public NotifierLayer() {
		super("Notifier");
	}
	
	public void show(Notification note) {
		note.show(this);
		getNotesOnScreen().add(note);
	}
	
	public void hide(Notification note) {
		getNotesOnScreen().remove(note);
		nextY -= note.height;
	}

	public Collection<Notification> getNotesOnScreen() {
		return notesOnScreen;
	}

	@Override
	public void render() {
		int targetY = gdGetLine();
		for (Notification n : getNotesOnScreen()) {
			targetY += n.render(targetY);
		}
	}

	@Override
	public void onInput(Input input) {
		if (!(input instanceof MouseButtonInput)) {
			return;
		}
		
		MouseButtonInput mbi = (MouseButtonInput) input;
		
		if (!mbi.isPressed()) {
			return;
		}
		
		for (Notification n : getNotesOnScreen()) {
			if (n.hasCursor()) {
				if (mbi.isLeftButton()) {
					n.onClicked();
				} else if (mbi.isRightButton()) {
					n.onKicked();
				}
				input.consume();
				return;
			}
		}
	}

}
