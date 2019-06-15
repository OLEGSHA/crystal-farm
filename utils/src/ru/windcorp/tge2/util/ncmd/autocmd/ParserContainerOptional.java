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

import ru.windcorp.tge2.util.IndentedStringBuilder;
import ru.windcorp.tge2.util.ncmd.Invocation;

public class ParserContainerOptional extends Parser {
	
	private final Parser contents;

	public ParserContainerOptional(String id, Parser item) {
		super(id);
		this.contents = item;
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#getProblem(java.text.CharacterIterator)
	 */
	@Override
	public Supplier<Exception> getProblem(CharacterIterator data, Invocation inv) {
		int index = data.getIndex();
		
		if (contents.getProblem(data, inv) != null) {
			data.setIndex(index);
		}
		
		return null;
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#matches(java.text.CharacterIterator)
	 */
	@Override
	public boolean matches(CharacterIterator data) {
		int index = data.getIndex();
		
		if (!contents.matches(data)) {
			data.setIndex(index);
		}
		
		return true;
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#parse(java.text.CharacterIterator, java.util.function.Consumer)
	 */
	@Override
	public void parse(CharacterIterator data, Consumer<Object> output) {
		int index = data.getIndex();
		
		boolean matches = contents.matches(data);
		data.setIndex(index);
		
		if (matches) {
			output.accept(true);
			contents.parse(data, output);
		} else {
			output.accept(false);
			contents.insertEmpty(output);
		}
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#insertEmpty(java.util.function.Consumer)
	 */
	@Override
	public void insertEmpty(Consumer<Object> output) {
		output.accept(false);
		contents.insertEmpty(output);
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#insertArgumentClasses(java.util.function.Consumer)
	 */
	@Override
	public void insertArgumentClasses(Consumer<Class<?>> output) {
		output.accept(Boolean.TYPE);
		contents.insertArgumentClasses(output);
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#toString()
	 */
	@Override
	public String toString() {
		return toDebugString();
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#toDebugString(ru.windcorp.tge2.util.IndentedStringBuilder)
	 */
	@Override
	protected void toDebugString(IndentedStringBuilder sb) {
		sb.append("Optional ");
		contents.toDebugString(sb);
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#toSyntax(java.lang.StringBuilder, ru.windcorp.tge2.util.ncmd.autocmd.SyntaxFormatter)
	 */
	@Override
	protected void toSyntax(StringBuilder sb, SyntaxFormatter formatter) {
		formatter.appendStructureChar(sb, '[');
		contents.toSyntax(sb, formatter);
		formatter.appendStructureChar(sb, ']');
	}

}
