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
package ru.windcorp.tge2.util.ncmd.autocmd;

import java.text.CharacterIterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ru.windcorp.tge2.util.ncmd.Invocation;

public class ParserInt extends Parser {

	public ParserInt(String id) {
		super(id);
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#getProblem(java.text.CharacterIterator, ru.windcorp.tge2.util.ncmd.Invocation)
	 */
	@Override
	public Supplier<Exception> getProblem(CharacterIterator data, Invocation inv) {
		char[] chars = nextWord(data);
		if (!isInt(chars)) {
			return () -> {
				return new NumberFormatException("\"" + new String(chars) + "\" is not an int"); // TODO translate
			};
		} else return null;
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#matches(java.text.CharacterIterator)
	 */
	@Override
	public boolean matches(CharacterIterator data) {
		return isInt(nextWord(data));
	}
	
	private boolean isInt(char[] chars) {
		long result = 0;
		boolean isPositive = true;
		
		if (chars.length == 0) {
			return false;
		}
		
		int i = 0;
		if (chars[0] == '-') {
			isPositive = false;
			i++;
		} else if (chars[0] == '+') {
			i++;
		}
		
		for (; i < chars.length; ++i) {
			if (chars[i] < '0' || chars[i] > '9') {
				return false;
			}
			result = result * 10 + (chars[i] - '0');
			if (isPositive) {
				if (result > Integer.MAX_VALUE) return false;
			} else {
				if (-result < Integer.MIN_VALUE) return false;
			}
		}
		
		return true;
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#parse(java.text.CharacterIterator, java.util.function.Consumer)
	 */
	@Override
	public void parse(CharacterIterator data, Consumer<Object> output) {
		char[] chars = nextWord(data);
		
		int result = 0;
		int i = 0;
		
		if (chars[0] == '-') {
			for (i = 1; i < chars.length; ++i) result = result * 10 - (chars[i] - '0');
		} else {
			if (chars[0] == '+') i++;
			for (; i < chars.length; ++i) result = result * 10 + (chars[i] - '0');
		}
		
		
		output.accept(result);
		return;
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#insertArgumentClasses(java.util.function.Consumer)
	 */
	@Override
	public void insertArgumentClasses(Consumer<Class<?>> output) {
		output.accept(Integer.TYPE);
	}

}
