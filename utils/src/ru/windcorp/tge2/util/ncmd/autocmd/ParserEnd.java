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

import ru.windcorp.tge2.util.ncmd.CommandSyntaxException;
import ru.windcorp.tge2.util.ncmd.Invocation;

public class ParserEnd extends Parser {

	public ParserEnd() {
		super("End");
	}

	@Override
	public Supplier<Exception> getProblem(CharacterIterator data, Invocation inv) {
		skipWhitespace(data);
		if (data.getIndex() < data.getEndIndex()) {
			char[] chars = new char[data.getEndIndex() - data.getIndex()];
			int length = 0;
			
			int i = 0;
			while (data.getIndex() < data.getEndIndex()) {
				chars[i++] = data.current();
				if (!Character.isWhitespace(data.current())) {
					length = i; // Incremented
				}
				data.next();
			}
			
			String excessiveArgs = String.valueOf(chars, 0, length);
			return () -> new CommandSyntaxException(inv, "Excessive arguments \"" + excessiveArgs + "\""); // TODO translate
		}
		return null;
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#matches(java.text.CharacterIterator)
	 */
	@Override
	public boolean matches(CharacterIterator data) {
		skipWhitespace(data);
		return data.getIndex() >= data.getEndIndex();
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#parse(java.text.CharacterIterator, java.util.function.Consumer)
	 */
	@Override
	public void parse(CharacterIterator data, Consumer<Object> output) {
		// Do nothing
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#insertArgumentClasses(java.util.function.Consumer)
	 */
	@Override
	public void insertArgumentClasses(Consumer<Class<?>> output) {
		// Do nothing
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#toString()
	 */
	@Override
	public String toString() {
		return "End";
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#toSyntax(java.lang.StringBuilder, ru.windcorp.tge2.util.ncmd.autocmd.SyntaxFormatter)
	 */
	@Override
	protected void toSyntax(StringBuilder sb, SyntaxFormatter formatter) {
		formatter.appendStructureChar(sb, '.');
	}

}
