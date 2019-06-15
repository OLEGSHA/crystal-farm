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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import ru.windcorp.tge2.util.ncmd.CommandSyntaxException;
import ru.windcorp.tge2.util.ncmd.Invocation;

public class ParserLiteral extends Parser {
	
	private final Function<String, String> test;
	private final String description;

	public ParserLiteral(String id, Function<String, String> test, String description) {
		super(id);
		this.test = test;
		this.description = description;
	}
	
	public ParserLiteral(String id, Predicate<String> test, String msg, String description) {
		this(id, input -> test.test(input) ? null : msg, description);
	}
	
	public ParserLiteral(String id, String constant, String msg) {
		this(id, constant::equalsIgnoreCase, msg, constant);
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#getProblem(java.text.CharacterIterator, ru.windcorp.tge2.util.ncmd.Invocation)
	 */
	@Override
	public Supplier<Exception> getProblem(CharacterIterator data, Invocation inv) {
		String token = String.valueOf(nextWord(data));
		String msg = test.apply(token);
		return () -> new CommandSyntaxException(inv, msg == null ? "" : msg); // TODO translate
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#matches(java.text.CharacterIterator)
	 */
	@Override
	public boolean matches(CharacterIterator data) {
		String token = String.valueOf(nextWord(data));
		return test.apply(token) == null;
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#parse(java.text.CharacterIterator, java.util.function.Consumer)
	 */
	@Override
	public void parse(CharacterIterator data, Consumer<Object> output) {
		skipWhitespace(data);
		while (!Character.isWhitespace(data.current()))
			data.next();
		// Output nothing
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#insertEmpty(java.util.function.Consumer)
	 */
	@Override
	public void insertEmpty(Consumer<Object> output) {
		// Output nothing
	}

	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#insertArgumentClasses(java.util.function.Consumer)
	 */
	@Override
	public void insertArgumentClasses(Consumer<Class<?>> output) {
		// Do nothing
	}
	
	/**
	 * @return the test
	 */
	public Function<String, String> getTest() {
		return test;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#toString()
	 */
	@Override
	public String toString() {
		return "Literal \"" + getDescription() + "\"";
	}
	
	/**
	 * @see ru.windcorp.tge2.util.ncmd.autocmd.Parser#toSyntax(java.lang.StringBuilder, ru.windcorp.tge2.util.ncmd.autocmd.SyntaxFormatter)
	 */
	@Override
	protected void toSyntax(StringBuilder sb, SyntaxFormatter formatter) {
		formatter.appendStructureChar(sb, '"');
		formatter.appendLiteral(sb, getDescription());
		formatter.appendStructureChar(sb, '"');
	}

}
