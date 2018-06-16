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

import java.util.function.Consumer;

import ru.windcorp.crystalfarm.graphics.fonts.FontString;
import ru.windcorp.crystalfarm.translation.TString;

public class SwitchButton extends Button {
	
	private final TString textTrue, textFalse;
	private boolean state;

	public SwitchButton(String name, TString textTrue, TString textFalse, boolean state, Consumer<?> action) {
		super(name, new FontString(state ? textTrue : textFalse), action);
		this.state = state;
		this.textTrue = textTrue;
		this.textFalse = textFalse;
	}
	
	public boolean getState() {
		return state;
	}
	
	public void setState(boolean state) {
		this.state = state;
		
		getText().setText(state ? textTrue : textFalse);
		super.accept(null);
	}
	
	public void switchState() {
		setState(!getState());
	}
	
	@Override
	public void accept(Object t) {
		switchState();
	}

}
