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

import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.fillRectangle;
import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import ru.windcorp.crystalfarm.gui.listener.ComponentMouseButtonInputListener;
import ru.windcorp.crystalfarm.translation.TString;

public class ChoiceButton<T> extends ArrowedComponent {
	
	private final List<T> choices;
	private int selection;

	public ChoiceButton(String name, Consumer<?> action, int firstSelection, List<T> choices) {
		super(name, action, TString.wrap(choices.get(firstSelection)).toFont());
		
		this.choices = new CopyOnWriteArrayList<>(choices);
		this.selection = firstSelection;
		
		addInputListener((ComponentMouseButtonInputListener) (comp, input) -> {
			if (!input.isPressed()) {
				return;
			}
			
			if (input.isLeftButton()) {
				selectNext();
				input.consume();
			} else if (input.isRightButton()) {
				selectPrevious();
				input.consume();
			}
		});
	}

	public List<T> getChoices() {
		return choices;
	}
	
	public void addChoice(T choice) {
		getChoices().add(choice);
	}
	
	public void removeChoice(T choice) {
		getChoices().remove(choice);
	}
	
	public int getChoicesSize() {
		return getChoices().size();
	}
	
	public int getSelected() {
		return selection;
	}
	
	public void selectSilently(int selection) {
		this.selection = selection;
		getDisplay().getText().setText(TString.wrap(getSelection()));
	}
	
	public void select(int selection) {
		selectSilently(selection);
		super.accept(null);
	}
	
	@Override
	public void selectNext() {
		select((selection + 1) % getChoicesSize());
	}
	
	@Override
	public void selectPrevious() {
		select((getChoicesSize() + selection - 1) % getChoicesSize());
	}
	
	public T getSelection() {
		return getChoices().get(getSelected());
	}

	public void select(T choice) {
		int index = getChoices().indexOf(choice);
		select(index);
	}

	@Override
	protected void renderSelf() {
		fillRectangle(
				getX(),
				getY(),
				getWidth(),
				getHeight(),
				isHovered() ? gdGetForegroundAltColor() : gdGetForegroundColor(),
				isFocused() ? gdGetBorderFocusedColor() : gdGetBorderColor(),
				gdGetLine());
	}
	
}
