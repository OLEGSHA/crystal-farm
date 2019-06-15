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

public class ParserContainerGroup extends Parser {
	
	private final Parser[] items;

	public ParserContainerGroup(String id, Parser[] items) {
		super(id);
		this.items = items;
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#getProblem(java.text.CharacterIterator)
	 */
	@Override
	public Supplier<Exception> getProblem(CharacterIterator data, Invocation inv) {
		Supplier<Exception> problem = null;
		
		for (Parser item : items) {
			problem = item.getProblem(data, inv);
			if (problem != null) {
				return problem;
			}
		}
		
		return null;
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#matches(java.text.CharacterIterator)
	 */
	@Override
	public boolean matches(CharacterIterator data) {
		for (Parser item : items) {
			if (!item.matches(data))
				return false;
		}
		return true;
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#parse(java.text.CharacterIterator, java.util.function.Consumer)
	 */
	@Override
	public void parse(CharacterIterator data, Consumer<Object> output) {
		for (Parser item : items) {
			item.parse(data, output);
		}
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#insertEmpty(java.util.function.Consumer)
	 */
	@Override
	public void insertEmpty(Consumer<Object> output) {
		for (Parser item : items) {
			item.insertEmpty(output);
		}
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#insertArgumentClasses(java.util.function.Consumer)
	 */
	@Override
	public void insertArgumentClasses(Consumer<Class<?>> output) {
		for (Parser item : items) {
			item.insertArgumentClasses(output);
		}
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
		sb.append("Group " + getId() + " {").indent();
		for (Parser item : items) {
			sb.breakLine();
			item.toDebugString(sb);
		}
		sb.unindent().breakLine().append("}");
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#toSyntax(java.lang.StringBuilder, ru.windcorp.tge2.util.ncmd.autocmd.SyntaxFormatter)
	 */
	@Override
	protected void toSyntax(StringBuilder sb, SyntaxFormatter formatter) {
		for (int i = 0; i < items.length; ++i) {
			Parser item = items[i];
			
			if (
					item instanceof ParserContainerOptional ||
					item instanceof ParserLiteral ||
					item instanceof ParserTrailingSpecific ||
					item instanceof ParserTrailingString) {
				item.toSyntax(sb, formatter);
			} else {
				formatter.appendStructureChar(sb, '<');
				item.toSyntax(sb, formatter);
				formatter.appendStructureChar(sb, '>');
			}
			
			if (i != items.length - 1) {
				sb.append(' ');
			}
		}
	}
	
}
