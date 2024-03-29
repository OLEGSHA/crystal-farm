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
package ru.windcorp.crystalfarm.input;

import ru.windcorp.tge2.util.NumberUtil;

public class CharInput extends Input {

	private final int character;
	
	public CharInput(int character) {
		this.character = character;
	}
	
	public InputTarget getTarget() {
		return InputTarget.FOCUSED;
	}

	public int getCharacter() {
		return character;
	}
	
	@Override
	public String toString() {
		return "Input[Char, '" + ((char) getCharacter()) + "' (" + new String(NumberUtil.toFullHex(getCharacter())) + ")]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + character;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CharInput other = (CharInput) obj;
		if (character != other.character)
			return false;
		return true;
	}

}
