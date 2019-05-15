package ru.windcorp.tge2.util.tcfg;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ru.windcorp.tge2.util.StringUtil;

public class CFGParser {
	
	@FunctionalInterface
	public static interface CharPredicate {
		public static final CharPredicate WHITESPACE = Character::isWhitespace;
		public static final CharPredicate NEWLINES = forArray('\n', '\r');
		public static final CharPredicate WHITESPACE_NO_NEWLINES = c -> WHITESPACE.accept(c) && !NEWLINES.accept(c);
		public static final CharPredicate DECLINER = c -> false;
		
		public static CharPredicate forArray(char... chars) {
			if (chars.length == 0) {
				return DECLINER;
			}
			
			if (chars.length == 1) {
				return forChar(chars[0]);
			}
			
			final char[] sorted = Arrays.copyOf(chars, chars.length);
			Arrays.sort(sorted);
			return c -> Arrays.binarySearch(chars, c) >= 0;
		}
		
		public static CharPredicate forChar(final char c) {
			return given -> given == c;
		}
		
		boolean accept(char c);
	}
	
	public static interface Setter {
		void set(String  declar) throws InvalidSettingException;
		
		static int parseInt(String declar, int min, int max) throws InvalidSettingException {
			try {
				int x = Integer.parseInt(declar);
				
				if (x < min) {
					throw new InvalidSettingException("Minimum " + min + " required, " + x + " given");
				} else if (x > max) {
					throw new InvalidSettingException("Maximum " + max + " required, " + x + " given");
				}
				
				return x;
			} catch (NumberFormatException e) {
				throw new InvalidSettingException("\"" + declar + "\" is not an integer", e);
			}
		}
	}
	
	private char intraSep = '=';
	private CharPredicate interSep = CharPredicate.WHITESPACE;
	private CharPredicate trimValue = CharPredicate.WHITESPACE_NO_NEWLINES;
	private CharPredicate trimName = CharPredicate.WHITESPACE_NO_NEWLINES;
	private char commentStart = '#';
	private CharPredicate commentEnd = CharPredicate.NEWLINES;

	private class Setting {
		private final String name;
		private final boolean isOptional;
		private boolean isSet = false;
		private final Setter setter;
		
		public Setting(String name, boolean isOptional, Setter setter) {
			this.name = name;
			this.isOptional = isOptional;
			this.setter = setter;
		}

		public void set(String value) throws CFGException {
			if (!allowRepeats && isSet) {
				throw new CFGException("Duplicate declaration for \"" + name + "\"");
			}
			
			try {
				InvalidSettingException.startParsing(name);
				setter.set(value);
				InvalidSettingException.endParsing();
				isSet = true;
			} catch (InvalidSettingException e) {
				// Do nothing, rethrow
				throw e;
			} catch (RuntimeException e) {
				throw new InvalidSettingException("Unexpected runtime exception", e);
			} catch (Throwable e) {
				InvalidSettingException.endParsing();
				throw e;
			}
		}
	}
	
	private final Map<String, Setting> settings = new HashMap<>();
	
	private boolean allowRepeats = false;
	private boolean ignoreUnknown = false;
	
	public char getIntraSep() {
		return intraSep;
	}

	public CFGParser intraSeparator(char intraSep) {
		this.intraSep = intraSep;
		return this;
	}

	public CharPredicate getInterSep() {
		return interSep;
	}

	public CFGParser interSeparator(CharPredicate interSep) {
		this.interSep = interSep;
		return this;
	}

	public CharPredicate getTrimValue() {
		return trimValue;
	}

	public CFGParser removeBeforeValue(CharPredicate trimValue) {
		this.trimValue = trimValue;
		return this;
	}

	public CharPredicate getTrimName() {
		return trimName;
	}

	public CFGParser removeAfterName(CharPredicate trimName) {
		this.trimName = trimName;
		return this;
	}
	
	public CFGParser doNotTrim() {
		removeAfterName(CharPredicate.DECLINER);
		removeBeforeValue(CharPredicate.DECLINER);
		return this;
	}

	public char getCommentStart() {
		return commentStart;
	}

	public CFGParser commentStart(char commentStart) {
		this.commentStart = commentStart;
		return this;
	}

	public CharPredicate getCommentEnd() {
		return commentEnd;
	}

	public CFGParser commentEnd(CharPredicate commentEnd) {
		this.commentEnd = commentEnd;
		return this;
	}
	
	public boolean getAllowRepeats() {
		return allowRepeats;
	}

	public CFGParser setAllowRepeats(boolean allowRepeats) {
		this.allowRepeats = allowRepeats;
		return this;
	}
	
	public CFGParser allowRepeats() {
		setAllowRepeats(true);
		return this;
	}

	public boolean getIgnoreUnknown() {
		return ignoreUnknown;
	}

	public CFGParser setIgnoreUnknown(boolean ignoreUnknown) {
		this.ignoreUnknown = ignoreUnknown;
		return this;
	}
	
	public CFGParser ignoreUnknown() {
		setIgnoreUnknown(true);
		return this;
	}

	public CFGParser require(String name, Setter output) {
		settings.put(name, new Setting(name, false, output));
		return this;
	}
	
	public CFGParser optional(String name, Setter output) {
		settings.put(name, new Setting(name, true, output));
		return this;
	}
	
	/*
	 * Modes
	 */
	private static final int
			INTERSTATEMENT = 0,
			READING_NAME = 1,
			TRIMMING_NAME = 2,
			TRIMMING_VALUE = 3,
			READING_VALUE = 4,
			COMMENT = 5;
	
	public void parse(Reader reader) throws IOException, CFGException, InvalidSettingException {
		LineNumberReader lineReader = new LineNumberReader(reader);
		char current;
		
		int mode = INTERSTATEMENT;
		StringBuilder sb = new StringBuilder();
		
		String name = null;

		int read = lineReader.read();
		if (read != -1) {
			current = (char) read;
			
			processing:
			while (true) {
				switch (mode) {
				case INTERSTATEMENT:
					if (getCommentStart() == current) {
						mode = COMMENT;
						break;
					}
					if (!getInterSep().accept(current)) {
						mode = READING_NAME;
						continue processing;
					}
					break;
					
				case READING_NAME:
					if (getTrimName().accept(current)) {
						if (sb.length() == 0) {
							throw new CFGException("[Line " + lineReader.getLineNumber() + "] Empty name");
						}
						name = StringUtil.resetStringBuilder(sb);
						mode = TRIMMING_NAME;
						break;
					}
					if (getIntraSep() == current) {
						if (sb.length() == 0) {
							throw new CFGException("[Line " + lineReader.getLineNumber() + "] Empty name");
						}
						name = StringUtil.resetStringBuilder(sb);
						mode = TRIMMING_VALUE;
						break;
					}
					sb.append(current);
					break;
					
				case TRIMMING_NAME:
					if (getIntraSep() == current) {
						mode = TRIMMING_VALUE;
						break;
					}
					if (!getTrimName().accept(current)) {
						throw new CFGException("[Line " + lineReader.getLineNumber() + "] Illegal characters after name " + name);
					}
					break;
					
				case TRIMMING_VALUE:
					if (!getTrimValue().accept(current)) {
						mode = READING_VALUE;
						continue processing;
					}
					break;
					
				case READING_VALUE:
					if (getInterSep().accept(current)) {
						Setting setting = settings.get(name);
						if (!ignoreUnknown && setting == null) {
							throw new CFGException("[Line " + lineReader.getLineNumber() + "] Unknown setting \"" + name + "\"");
						}
						
						setting.set(StringUtil.resetStringBuilder(sb));
						
						mode = INTERSTATEMENT;
						break;
					}
					sb.append(current);
					break;
					
				case COMMENT:
					if (getCommentEnd().accept(current)) {
						mode = INTERSTATEMENT;
						break;
					}
					break;
				}
				
				read = lineReader.read();
				if (read == -1) {
					break;
				}
				current = (char) read;
			}
			
			if (mode == READING_NAME || mode == TRIMMING_NAME) {
				throw new CFGException("[Line " + lineReader.getLineNumber() + "] Last statement is incomplete: value missing");
			} else if (mode == TRIMMING_VALUE || mode == READING_VALUE) {
				Setting setting = settings.get(name);
				if (!ignoreUnknown && setting == null) {
					throw new CFGException("[Line " + lineReader.getLineNumber() + "] Unknown setting \"" + name + "\"");
				}
				
				setting.set(sb.toString());
			}
		}
		
		for (Setting setting : settings.values()) {
			if (!setting.isSet && !setting.isOptional) {
				throw new CFGException("Setting \"" + setting.name + "\" not set");
			}
		}
	}

}
