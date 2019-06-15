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

public abstract class Parser {
	
	private final String id;
	private String typeAsDeclared = null;
	
	public Parser(String id) {
		this.id = id;
	}
	
	public abstract Supplier<Exception> getProblem(CharacterIterator data, Invocation inv);
	public abstract boolean matches(CharacterIterator data);
	public abstract void parse(CharacterIterator data, Consumer<Object> output);
	public abstract void insertArgumentClasses(Consumer<Class<?>> output);
	
	public void insertEmpty(Consumer<Object> output) {
		output.accept(null);
	}
	
	protected void skipWhitespace(CharacterIterator data) {
		while (Character.isWhitespace(data.current()))
			data.next();
	}
	
	protected char[] readWord(CharacterIterator data) {
		int chars = 0;
		int start = data.getIndex();
		
		char c = data.current();
		while (!Character.isWhitespace(c)) {
			if (c == CharacterIterator.DONE) {
				break;
			}
			chars++;
			c = data.next();
		}
		
		data.setIndex(start);
		
		char[] result = new char[chars];
		for (int i = 0; i < chars; ++i) {
			result[i] = data.current();
			data.next();
		}
		
		return result;
	}
	
	protected char[] nextWord(CharacterIterator data) {
		skipWhitespace(data);
		return readWord(data);
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return the typeAsDeclared
	 */
	public String getTypeAsDeclared() {
		return typeAsDeclared;
	}
	
	/**
	 * @param typeAsDeclared the typeAsDeclared to set
	 */
	void setTypeAsDeclared(String typeAsDeclared) {
		this.typeAsDeclared = typeAsDeclared;
	}
	
	@Override
	public String toString() {
		return (getTypeAsDeclared() == null ? getClass().getSimpleName() : getTypeAsDeclared()) + " " + getId();
	}
	
	public final String toSyntax(SyntaxFormatter formatter) {
		StringBuilder sb = new StringBuilder();
		toSyntax(sb, formatter == null ? SyntaxFormatter.PLAIN : formatter);
		return sb.toString();
	}
	
	public final String toSyntax() {
		return toSyntax(null);
	}
	
	protected void toSyntax(StringBuilder sb, SyntaxFormatter formatter) {
		formatter.appendType(sb, getTypeAsDeclared());
		sb.append(" ");
		formatter.appendId(sb, getId());
	}
	
	public final String toDebugString() {
		IndentedStringBuilder sb = new IndentedStringBuilder();
		toDebugString(sb);
		return sb.toString();
	}
	
	protected void toDebugString(IndentedStringBuilder sb) {
		sb.append(toString());
	}

}
