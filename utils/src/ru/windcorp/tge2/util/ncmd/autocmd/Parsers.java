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
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import ru.windcorp.tge2.util.FancyCharacterIterator;
import ru.windcorp.tge2.util.StringUtil;

public class Parsers {
	
	@FunctionalInterface
	public static interface ParserCreator {
		Parser create(String id, CharacterIterator data);
	}
	
	private static final Map<String, ParserCreator> CREATORS = Collections.synchronizedMap(new HashMap<>());
	
	private static final Parser[] ARRAY_TEMPLATE = new Parser[0];

	public static ParserCreator getCreator(String type) {
		return CREATORS.get(type.toLowerCase());
	}
	
	public static void registerCreator(String type, ParserCreator creator) {
		if (!isParserTypeName(type))
			throw new IllegalArgumentException("Illegal characters in type name: only [a-z][A-Z][0-9]_ allowed, \"" + type + "\" given");
		
		CREATORS.put(type.toLowerCase(), creator);
	}
	
	public static void registerCreator(String type, Function<String, Parser> creator) {
		registerCreator(type, (id, meta) -> creator.apply(id));
	}
	
	public static void registerDefaultCreators() {
		registerCreator("int", ParserInt::new);
	}
	
	public static Parser createParser(String syntax) {
		FancyCharacterIterator it = new FancyCharacterIterator(syntax.trim());
		return createContainer(it, CharacterIterator.DONE, "0", new int[] {1});
	}

	private static Parser createItem(FancyCharacterIterator it, char terminateOnChar, int[] nextId) {
		StringBuilder typeOrID = new StringBuilder();
		
		skipWhitespace(it);
		char c = it.current();
		if (c == '<' || c == '[' || c == '"') {
			return createContainer(it, terminateOnChar, null, nextId);
		}
		
		while (true) {
			if (isParserTypeCharacter(c)) {
				typeOrID.append(c);
				c = it.next();
			} else {	
				if (Character.isWhitespace(c)) {
					it.next();
					return createRegularParser(it, typeOrID.toString(), terminateOnChar, nextId);
				}
				
				if (c == ':') {
					it.next();
					return createContainer(it, terminateOnChar, typeOrID.toString(), nextId);
				}
				
				if (c == '.') {
					if (it.next() == '.' & it.next() == '.') {
						return new ParserTrailingString(typeOrID.length() == 0
								? Integer.toString(nextId[0]++)
								: typeOrID.toString());
					} else {
						it.setIndex(it.getIndex() - 2);
					}
				}
				
				unexpectedChar(it, typeOrID.length() == 0
						? "Expected parser type or container ID or ':' or '<' or '[' or '\"' or '...'"
						: "Expected parser type or container ID or ':' or '...'");
			}
		}
	}
	
	private static Parser createRegularParser(FancyCharacterIterator it, String type, char terminateOnChar, int[] nextId) {
		StringBuilder id = new StringBuilder();
		boolean trailing = false;
		StringBuilder meta = null;
		
		skipWhitespace(it);
		char c = it.current();
		
		if (c == '(') {
			meta = new StringBuilder();
			it.next();
			while (c != ')') {
				if (c == CharacterIterator.DONE) unexpectedEnd();
				meta.append(c);
			}
			
			it.next();
			skipWhitespace(it);
		}
		
		while (true) {
			c = it.current();
			
			if (Character.isWhitespace(c)) {
				skipWhitespace(it);
				c = it.current();
				if (c == terminateOnChar) break;
				if (c == '.') {
					if (trailing) unexpectedChar(it, "Expected '" + terminateOnChar + "'");
					if (it.next() == '.' && it.next() == '.') trailing = true;
					else unexpectedChar(it, "Expected '...' sequence");
				}
				unexpectedChar(it, "Expected '" + terminateOnChar
						+ (trailing ? "'" : "' or '...'"));
			}
			if (isIdCharacter(c)) {
				id.append(c);
			} else if (c == terminateOnChar) {
				break;
			} else if (c == '.') {
				if (trailing) unexpectedChar(it, "Expected parser ID or '" + terminateOnChar + "'");
				if (it.next() == '.' && it.next() == '.') trailing = true;
				else unexpectedChar(it, "Expected '...' sequence");
			} else {
				unexpectedChar(it, "Expected parser ID or '" + terminateOnChar + "' or '...'");
			}
			
			it.next();
		}
		
		ParserCreator creator = getCreator(type);
		if (creator == null)
			throw new IllegalArgumentException("Unknown parser type \"" + type + "\" at index " + it.getIndex() + ":\n" + it);
		
		Parser result;
		try {
			result = creator.create(
					id.length() == 0 ? Integer.toString(nextId[0]++) : id.toString(),
					meta == null ? null : new StringCharacterIterator(meta.toString()));
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("Parser creator " + type + " has thrown an exception. Meta: "
					+ (meta == null ? "null" : "\"" + meta + "\""), e);
		}
		
		result.setTypeAsDeclared(type);
		
		it.next();
		
		if (trailing) result = new ParserTrailingSpecific(id + "...", result);
		return result;
	}

	private static Parser createContainer(FancyCharacterIterator it, char terminateOnChar, String firstName, int[] nextId) {
		List<Parser> variableEntries = null;
		List<Parser> currentItems = new ArrayList<>();
		String currentId = firstName; // may be null
		StringBuilder currentIdBuilder = new StringBuilder();
		boolean isId = firstName == null;
		
		skipWhitespace(it);
		
		while (true) {
			// If isId, skipWhitespace() has been called already
			if (!isId) skipWhitespace(it);
			
			char c = it.current();
			
			if (c == terminateOnChar) {
				// Either we are still reading ID or container declaration is empty
				if (isId) unexpectedChar(it, currentIdBuilder.length() == 0
						? "Must be at least one element in container"
						: "Expected container ID or ':'");
				
				// We might have read the ID correctly though
				if (currentItems.isEmpty()) unexpectedChar(it, "Must be at least one element in container");
				
				Parser result = createAppropriateItem(variableEntries, currentId, currentItems);
				
				if (terminateOnChar != CharacterIterator.DONE) {
					int index = it.getIndex();
					if (it.next() == '.') {
						if (it.next() == '.' && it.next() == '.') {
							result = new ParserTrailingSpecific(result.getId() + "...", result);
						} else {
							unexpectedChar(it, "Expected  '...' sequence");
						}
					} else {
						it.setIndex(index);
					}
				}
				
				it.next();
				return result;
			}
			
			// Check DONE after terminateOnChar because terminateOnChar can be DONE
			if (c == CharacterIterator.DONE) unexpectedEnd();
			
			if (isId) {
				if (isIdCharacter(c)) {
					currentIdBuilder.append(c);
					c = it.next();
					continue;
				}
				if (c == ':') {
					isId = false;
					currentId = currentIdBuilder.toString();
					c = it.next();
					continue;
				}
				
				if (currentIdBuilder.length() != 0) {
					unexpectedChar(it, "Expected container ID or ':'");
				} else {
					currentId = Integer.toString(nextId[0]++);
					isId = false;
					// There is no ID -- investigate further
					// This cannot be whitespace because we either hit a non-whitespace already or whitespace has
					//     been skipWhitespace()'d before.
				}
			}
			
			// NOT whitespace - either skipWhitespace()'d or consumed by block above
			// NOT ID now, currentId is set
			
			switch (c) {
			case '<':
				it.next();
				currentItems.add(createItem(it, '>', nextId));
				continue; // loop
			case '[':
				it.next();
				Parser item = createItem(it, ']', nextId);
				currentItems.add(new ParserContainerOptional("[" + item.getId() + "]", item));
				continue; // loop
			case '"':
				it.next();
				currentItems.add(createParserConstant(it, nextId));
				continue; // loop
			case '|':
				if (currentItems.isEmpty()) unexpectedChar(it, "Must be at least one element in container");
				if (variableEntries == null) variableEntries = new ArrayList<>();
				
				if (currentItems.size() == 1) {
					variableEntries.add(currentItems.get(0));
				} else {
					variableEntries.add(new ParserContainerGroup(currentId, currentItems.toArray(ARRAY_TEMPLATE)));
				}
				
				currentItems.clear();
				currentId = null;
				currentIdBuilder.setLength(0);
				isId = true;
				it.next();
				skipWhitespace(it);
				continue; // loop
			default:
				unexpectedChar(it, "Expected '<' or '[' or '\"' or "
						+ (currentItems.isEmpty() ? "" : "'|' or ")
						+ (terminateOnChar == CharacterIterator.DONE ? "end" : "'" + terminateOnChar + "'")); break;
			}
		}
	}

	private static Parser createParserConstant(FancyCharacterIterator it, int[] nextId) {
		boolean isRegex = true;
		StringBuilder sb = new StringBuilder();
		
		int index = it.getIndex();
		
		{
			for (int i = 0; i < "regex:".length(); ++i, it.next()) {
				if ("regex:".charAt(i) != it.current()) {
					isRegex = false;
					it.setIndex(index);
					break;
				}
			}
		}
		
		while (true) {
			char c = it.current();
			
			if (c == CharacterIterator.DONE) unexpectedEnd();
			else if (c == '\\') {
				c = it.next();
				switch (c) {
				case 'n':
					sb.append('\n');
					break;
				case '"':
				case '\\':
					sb.append(it.next());
					break;
				default:
					unexpectedChar(it, "Invalid escape sequence: only '\\\"', '\\n' and '\\\\' allowed");
				}
			} else if (c == '"') break;
			else sb.append(c);
			
			it.next();
		}
		
		it.next();
		
		String id = Integer.toString(nextId[0]++);
		
		if (isRegex) {
			try {
				return new ParserLiteral(id, Pattern.compile(sb.toString()).asPredicate(), null, "regex:" + sb);
			} catch (PatternSyntaxException e) {
				throw new IllegalArgumentException(e);
			}
		} else {
			return new ParserLiteral(id, sb.toString(), null);
		}
	}

	private static void unexpectedChar(FancyCharacterIterator it, String expected) {
		throw new IllegalArgumentException("Unexpected character '" + it.current() + "' ("
				+ Character.getName(it.current()) + ") at index "
				+ it.getIndex() + ":\n" + it + "\n" + expected);
	}
	
	private static void unexpectedEnd() {
		throw new IllegalArgumentException("Abrupt end");
	}

	private static void skipWhitespace(CharacterIterator it) {
		while (Character.isWhitespace(it.current())) it.next();
	}
	
	private static Parser createAppropriateItem(List<Parser> contents, String lastId, List<Parser> lastContents) {
		if (contents == null || contents.isEmpty()) {
			if (lastContents.size() == 1) {
				return lastContents.get(0);
			}
			return new ParserContainerGroup(lastId, lastContents.toArray(ARRAY_TEMPLATE));
		}
		
		contents.add(new ParserContainerGroup(lastId, lastContents.toArray(ARRAY_TEMPLATE)));
		
		return new ParserContainerVariable(
				"(" + StringUtil.supplierToString(i -> contents.get(i).getId(), contents.size(), " | ") + ")",
				contents.toArray(ARRAY_TEMPLATE));
	}
	
	private static boolean isParserTypeCharacter(char c) {
		return 
				(c >= 'a' && c <= 'z') ||
				(c >= 'A' && c <= 'Z') ||
				(c >= '0' && c <= '9') ||
				c == '_';
	}
	
	private static boolean isParserTypeName(String name) {
		for (int i = 0; i < name.length(); ++i)
			if (!isParserTypeCharacter(name.charAt(i))) return false;
		return true;
	}

	private static boolean isIdCharacter(char c) {
		return "<>[]|\"()".indexOf(c) < 0 && !Character.isWhitespace(c);
	}

}
