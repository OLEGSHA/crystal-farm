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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.graphics.fonts.FontString;
import ru.windcorp.crystalfarm.gui.listener.ComponentKeyInputListener;
import ru.windcorp.crystalfarm.gui.listener.ComponentMouseButtonInputListener;
import ru.windcorp.crystalfarm.translation.TString;

public class ChoiceButton extends Button {
	
	private final List<TString> choices;
	private int selection;

	public ChoiceButton(String name, Consumer<?> action, int firstSelection, TString... choices) {
		super(name, new FontString(choices[firstSelection]), action);
		this.choices = new CopyOnWriteArrayList<>(choices);
		this.selection = firstSelection;
		
		addInputListener((ComponentMouseButtonInputListener) (comp, input) -> {
			if (input.isRightButton() && input.isPressed()) {
				selectPrevious();
				input.consume();
			}
		});
		
		addInputListener((ComponentKeyInputListener) (comp, input) -> {
			if (input.isReleased()) {
				return;
			}
			
			switch (input.getKey()) {
			case GLFW.GLFW_KEY_DOWN:
			case GLFW.GLFW_KEY_RIGHT:
				selectNext();
				input.consume();
				break;
				
			case GLFW.GLFW_KEY_UP:
			case GLFW.GLFW_KEY_LEFT:
				selectPrevious();
				input.consume();
				break;
			}
		});
	}

	public List<TString> getChoices() {
		return choices;
	}
	
	public void addChoice(TString choice) {
		getChoices().add(choice);
	}
	
	public void removeChoice(TString choice) {
		getChoices().remove(choice);
	}
	
	public int getChoicesSize() {
		return getChoices().size();
	}
	
	public int getSelected() {
		return selection;
	}
	
	public void select(int selection) {
		this.selection = selection;
		
		getText().setText(getSelection());
		super.accept(null);
	}
	
	public void selectNext() {
		select((selection + 1) % getChoicesSize());
	}
	
	public void selectPrevious() {
		select((getChoicesSize() + selection - 1) % getChoicesSize());
	}
	
	public TString getSelection() {
		return getChoices().get(getSelected());
	}
	
	public void select(TString choice) {
		int index = getChoices().indexOf(choice);
		if (index == -1) {
			return;
		}
		select(index);
	}
	
	@Override
	public void accept(Object t) {
		selectNext();
	}

}
