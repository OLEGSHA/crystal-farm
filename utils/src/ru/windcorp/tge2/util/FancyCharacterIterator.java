/**
 * Copyright (C) 2019 OLEGSHA
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
package ru.windcorp.tge2.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class FancyCharacterIterator implements CharacterIterator {
	private final StringCharacterIterator obj;
	private final String data;

	public FancyCharacterIterator(String data) {
		this.obj = new StringCharacterIterator(data);
		this.data = data;
	}

	public char first() {
		return obj.first();
	}

	public char last() {
		return obj.last();
	}

	public char setIndex(int p) {
		return obj.setIndex(p);
	}

	public char current() {
		return obj.current();
	}

	public char next() {
		return obj.next();
	}

	public char previous() {
		return obj.previous();
	}

	public int getBeginIndex() {
		return obj.getBeginIndex();
	}

	public int getEndIndex() {
		return obj.getEndIndex();
	}

	public int getIndex() {
		return obj.getIndex();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\"");
		sb.append(data);
		sb.append("\"\n ");
		for (int i = 0; i < obj.getIndex(); ++i) sb.append(' ');
		sb.append("^ Here.");
		return sb.toString();
	}
	
	@Override
	public Object clone() {
		return null;
	}
}