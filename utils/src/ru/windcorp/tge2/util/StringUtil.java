package ru.windcorp.tge2.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;

import ru.windcorp.tge2.util.exceptions.SyntaxException;

public class StringUtil {
	
	public static char[] parseEscapeCharacters(char[] src,
			char escapeChar,
			char unicodeChar,
			char[] safe,
			char[] unsafe)
			throws SyntaxException {
		
		if (src == null || safe == null || unsafe == null) {
			throw new IllegalArgumentException(new NullPointerException());
		}
		
		if (safe.length != unsafe.length) {
			throw new IllegalArgumentException("safe.length (" + safe.length
					+ ") != unsafe.length (" + unsafe.length + ")");
		}
		
		StringBuilder sb = new StringBuilder(src.length);
		int j, codePoint, digit;
		
		for (int i = 0; i < src.length; ++i) {													// for each char
			
			if (src[i] == escapeChar) {															// if the char is the escape char
				
				if (src.length - i > 1) {														// if there are chars enough to make an escape sequence
					if (src[++i] == unicodeChar) {												// if the next char (note ++i) is unicode char
						
						if (src.length - i > 4) {												// read next four chars and make a int out of them
							codePoint = 0;
							for (j = 0; j < 4; ++j) {											// there are 4 hex digits
								++i;
								
								if ((digit = Character.digit(src[i], 0x10)) < 0) {				// non-digit char found
									throw new SyntaxException("'" + src[i] + "' is not "
											+ "a hexidecimal character (0-9, a-f)");
								}
								
								codePoint *= 0x10;
								codePoint += digit;
							}
							sb.append((char) codePoint);										// add the chars
						} else {
							throw new SyntaxException(
								"Found an incomplete unicode character "
								+ "escape at the end of src \"" + new String(src) +"\"");		// not enough chars left in src
						}
						
					} else {		
						for (j = 0; j < safe.length; ++j) {										// for each of the safe chars
							if (src[i] == safe[j]) {											// if the next char in src is a safe char with index j, add unsafe char with index j
								sb.append(unsafe[j]);
								j = -1;															// a replacement found
								break;
							}
						}
						
						if (j != -1) {															// if no replacement found, add the escapee
							sb.append(src[i]);
						}
					}
				} else {
					throw new SyntaxException("Found an escape character at the end of src \""
							+ new String(src) +"\"");											// not enough chars left in src (escape char is the last one)
				}
				
			} else {																			// if it is not an escape char, add it
				sb.append(src[i]);
			}
			
		}
		
		char[] result = new char[sb.length()];
		sb.getChars(0, result.length, result, 0);
		return result;
	}
	
	public static char[] encodeEscapeCharacters(char[] src,
			char escapeChar,
			char[] safe,
			char[] unsafe) {
		
		return encodeEscapeCharacters(src, escapeChar, '\0', safe, unsafe, false); // Argument unicodeChar is not used
	}
	
	public static char[] encodeEscapeCharacters(char[] src,
			char escapeChar,
			char unicodeChar,
			char[] safe,
			char[] unsafe,
			boolean avoidUnicode) {
		
		if (src == null || safe == null || unsafe == null) {
			throw new IllegalArgumentException(new NullPointerException());
		}
		
		if (safe.length != unsafe.length) {
			throw new IllegalArgumentException("safe.length (" + safe.length
					+ ") != unsafe.length (" + unsafe.length + ")");
		}
		
		StringBuilder sb = new StringBuilder(src.length);
		boolean append;
		int i;
		
		for (char c : src) {
			append = true;
			
			if (c == escapeChar) {
				sb.append(escapeChar);
				sb.append(escapeChar);
			} else {
				if (avoidUnicode && (c & 0xFF00) != 0) {
					sb.append(escapeChar);
					sb.append(unicodeChar);
					sb.append(Character.forDigit((c & 0xF000) >> (4 * 3), 0x10));
					sb.append(Character.forDigit((c & 0x0F00) >> (4 * 2), 0x10));
					sb.append(Character.forDigit((c & 0x00F0) >> (4 * 1), 0x10));
					sb.append(Character.forDigit((c & 0x000F) >> (4 * 0), 0x10));
					append = false;
				}
				
				for (i = 0; i < safe.length; ++i) {
					if (c == unsafe[i]) {
						sb.append(escapeChar);
						sb.append(safe[i]);
						
						append = false;
						break;
					}
				}
				
				if (append) {
					sb.append(c);
				}
			}
		}
		
		char[] result = new char[sb.length()];
		sb.getChars(0, result.length, result, 0);
		return result;
	}
	
	private static final char JAVA_ESCAPE_CHAR = '\\';
	private static final char JAVA_UNICODE_CHAR = 'u';
	private static final char[] JAVA_UNSAFES = "\t\b\n\r\f\'\"".toCharArray();
	private static final char[] JAVA_SAFES = "tbnrf'\"".toCharArray();
	
	public static char[] encodeJavaEscapeCharacters(char[] src) {
		return encodeEscapeCharacters(src, JAVA_ESCAPE_CHAR, JAVA_UNICODE_CHAR, JAVA_SAFES, JAVA_UNSAFES, true);
	}
	
	public static char[] parseJavaEscapeCharacters(char[] src) throws SyntaxException {
		return parseEscapeCharacters(src, JAVA_ESCAPE_CHAR, JAVA_UNICODE_CHAR, JAVA_SAFES, JAVA_UNSAFES);
	}
	
	public static <T> String arrayToString(T[] array,
			String separator,
			String empty,
			String nullPlaceholder,
			String nullArray) {
		
		if (separator == null) {
			throw new IllegalArgumentException(new NullPointerException());
		}
		
		if (array == null) {
			return nullArray;
		}
		
		if (array.length == 0) {
			return empty;
		}
		
		StringBuilder sb = new StringBuilder(array[0] == null ? nullPlaceholder : array[0].toString());
		
		for (int i = 1; i < array.length; ++i) {
			sb.append(separator);
			sb.append(array[i] == null ? nullPlaceholder : array[i].toString());
		}
		
		return sb.toString();
	}
	
	public static <T> String arrayToString(T[] array, String separator) {
		return arrayToString(array, separator, "[empty]", "[null]", "[null array]");
	}
	
	public static <T> String arrayToString(T[] array) {
		return arrayToString(array, "; ");
	}
	
	public static String iteratorToString(Iterator<?> iterator,
			String separator,
			String empty,
			String nullPlaceholder,
			String nullIterator) {
		
		if (separator == null) {
			throw new IllegalArgumentException(new NullPointerException());
		}
		
		if (iterator == null) {
			return nullIterator;
		}
		
		if (!iterator.hasNext()) {
			return empty;
		}
		
		Object obj = iterator.next();
		StringBuilder sb = new StringBuilder(obj == null ? nullPlaceholder : obj.toString());
		
		while (iterator.hasNext()) {
			obj = iterator.next();
			sb.append(separator);
			sb.append(obj == null ? nullPlaceholder : obj.toString());
		}
		
		return sb.toString();
	}
	
	public static String iteratorToString(Iterator<?> iterator, String separator) {
		return iteratorToString(iterator, separator, "[empty]", "[null]", "[null iterator]");
	}
	
	public static String iteratorToString(Iterator<?> iterator) {
		return iteratorToString(iterator, "; ");
	}
	
	public static byte[] toJavaByteArray(String str) {
		char[] chars = str.toCharArray();
		byte[] bytes = new byte[chars.length];
		
		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = (byte) chars[i];
		}
		
		return bytes;
	}
	
	public static int count(String src, char target) {
		int i = 0;
		for (char c : src.toCharArray()) {
			if (c == target) {
				++i;
			}
		}
		
		return i;
	}
	
	public static String[] split(String src, char separator) {
		return split(src, separator, count(src, separator) + 1);
	}
	
	public static String[] split(String src, char separator, int arrayLength) {
		if (arrayLength < 0) {
			throw new IllegalArgumentException("arrayLength must be non-negative (" + arrayLength + ")");
		} else if (arrayLength == 0) {
			return new String[0];
		} else if (arrayLength == 1) {
			return new String[] { src };
		}
		
		String[] result = new String[arrayLength];
		
		int resultIndex = 0;
		StringBuilder sb = new StringBuilder();
		for (char c : src.toCharArray()) {
			if (c == separator && (resultIndex + 1) < arrayLength) {
				result[resultIndex] = resetStringBuilder(sb);
				++resultIndex;
			} else {
				sb.append(c);
			}
		}
		
		result[resultIndex] = sb.toString();
		
		return result;
	}
	
	public static int count(String src, char... target) {
		int i = 0;
		for (char c : src.toCharArray()) {
			for (char t : target) {
				if (c == t) {
					++i;
					break;
				}
			}
		}
		
		return i;
	}
	
	public static String[] split(String src, char... separator) {
		return split(src, count(src, separator) + 1, separator);
	}
	
	public static String[] split(String src, int arrayLength, char... separator) {
		if (arrayLength < 0) {
			throw new IllegalArgumentException("arrayLength must be non-negative (" + arrayLength + ")");
		} else if (arrayLength == 0) {
			return new String[0];
		} else if (arrayLength == 1) {
			return new String[] { src };
		}
		
		String[] result = new String[arrayLength];
		
		int resultIndex = 0;
		StringBuilder sb = new StringBuilder();
		
		charloop:
		for (char c : src.toCharArray()) {
			if ((resultIndex + 1) < arrayLength) {
				for (char h : separator) {
					if (c == h) {
						result[resultIndex] = resetStringBuilder(sb);
						++resultIndex;
						continue charloop;
					}
				}
			}
			
			sb.append(c);
		}
		
		result[resultIndex] = sb.toString();
		
		return result;
	}
	
	public static String remove(String src, char... remove) {
		char[] result = new char[src.length() - count(src, remove)];
		
		char current;
		int resultIndex = 0;
		
		mainLoop:
		for (int srcIndex = 0; srcIndex < src.length(); ++srcIndex) {
			current = src.charAt(srcIndex);
			
			for (char c : remove) {
				if (current == c) {
					continue mainLoop;
				}
			}
			
			result[resultIndex++] = current;
		}
		
		return new String(result);
	}
	
	public static String resetStringBuilder(StringBuilder sb) {
		String result = sb.toString();
		sb.setLength(0);
		sb.ensureCapacity(10);
		return result;
	}
	
	public static String readToString(InputStream is, Charset encoding, int bufferSize) throws IOException {
		char[] buffer = new char[bufferSize];
		StringBuilder result = new StringBuilder();
		
		Reader reader = new InputStreamReader(is, encoding);
		while (true) {
		    int readChars = reader.read(buffer, 0, buffer.length);
		    
		    if (readChars == -1) {
		    	break;
		    }
		    
		    result.append(buffer, 0, readChars);
		}
		
		return result.toString();
	}
	
	public static boolean equalsPart(char[] a, char[] b, int beginPos, int endPos) {
		if (beginPos < 0) {
			throw new IllegalArgumentException("beginPos must be non-negative (" + beginPos + ")");
		}
		
		if (endPos < beginPos) {
			throw new IllegalArgumentException("endPos must be greater than or equal to beginPos (endPos="
					+ endPos + ", beginPos=" + beginPos + ")");
		}
		
		if (endPos >= Math.min(a.length, b.length)) {
			return false; // At least one of the arrays does not contain at least one of the required elements
		}
		
		for (int i = beginPos; i < endPos; ++i) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		
		return true;
	}

	// Java 8 is for pussies
	public static char[] join(char[]... srcs) {
		int tmp = 0;
		for (int i = 0; i < srcs.length; ++i) {
			tmp += srcs[i].length;
		}
		
		char[] result = new char[tmp];
		tmp = 0;
		for (int i = 0; i < srcs.length; ++i) {
			System.arraycopy(srcs[i], 0, result, tmp, srcs[i].length);
			tmp += srcs[i].length;
		}
		
		return result;
	}
	
	/**
	 * Finds and returns the index of the specified appearance of the specified character
	 * in the given array. The search starts at index 0.<p>
	 * Examples:<p>
	 * <table border="1">
	 * <tr>
	 * <th align="center"><code>src</code></th>
	 * <th align="center"><code>target</code></th>
	 * <th align="center"><code>skip</code></th>
	 * <th align="center">returns</th>
	 * </tr>
	 * <tr align="center"><td><code>a<u>.</u>b.c</code></td><td><code>'.'</code></td><td><code>0</code></td><td><code>1</code></td></tr>
	 * <tr align="center"><td><code>a.b<u>.</u>c</code></td><td><code>'.'</code></td><td><code>1</code></td><td><code>3</code></td></tr>
	 * <tr align="center"><td><code>a.b.c</code></td><td><code>'.'</code></td><td><code>2</code></td><td><code>-1</code></td></tr>
	 * <tr align="center"><td><code>a.b.c</code></td><td><code>'d'</code></td><td><i>any</i></td><td><code>-1</code></td></tr>
	 * </table>
	 * @param src - the array to search in.
	 * @param target - the character to search for.
	 * @param skip - the amount of <code>target</code> characters to be skipped.
	 * @return The index of the <code>skip+1</code>th <code>target</code> character or -1, if none found.
	 * @see StringUtil#indexFromEnd(char[], char, int)
	 */
	public static int indexFromBeginning(char[] src, char target, int skip) {
		for (int i = 0; i < src.length; ++i) {
			if (src[i] == target) {
				if (skip == 0) {
					return i;
				}
				
				--skip;
			}
		}
		return -1;
	}
	
	/**
	 * Finds and returns the index of the specified appearance of the specified character
	 * in the given array. The search starts at index <code>src.length - 1</code>.<p>
	 * Examples:<p>
	 * <table border="1">
	 * <tr>
	 * <th align="center"><code>src</code></th>
	 * <th align="center"><code>target</code></th>
	 * <th align="center"><code>skip</code></th>
	 * <th align="center">returns</th>
	 * </tr>
	 * <tr align="center"><td><code>a.b<u>.</u>c</code></td><td><code>'.'</code></td><td><code>0</code></td><td><code>3</code></td></tr>
	 * <tr align="center"><td><code>a<u>.</u>b.c</code></td><td><code>'.'</code></td><td><code>1</code></td><td><code>1</code></td></tr>
	 * <tr align="center"><td><code>a.b.c</code></td><td><code>'.'</code></td><td><code>2</code></td><td><code>-1</code></td></tr>
	 * <tr align="center"><td><code>a.b.c</code></td><td><code>'d'</code></td><td><i>any</i></td><td><code>-1</code></td></tr>
	 * </table>
	 * @param src - the array to search in.
	 * @param target - the character to search for.
	 * @param skip - the amount of <code>target</code> characters to be skipped.
	 * @return The index of the <code>skip+1</code>th <code>target</code>character
	 * from the end of the array or -1, if none found.
	 * @see StringUtil#indexFromBeginning(char[], char, int)
	 */
	public static int indexFromEnd(char[] src, char target, int skip) {
		for (int i = src.length - 1; i >= 0; --i) {
			if (src[i] == target) {
				if (skip == 0) {
					return i;
				}
				
				--skip;
			}
		}
		
		return -1;
	}
	
	public static String padToLeft(String src, int length, char c) {
		if (length <= 0) {
			throw new IllegalArgumentException("length must be positive (" + length + ")");
		}
		
		if (length <= src.length()) {
			return src;
		}
		
		char[] result = new char[length];
		
		int i = 0;
		for (; i < src.length(); ++i) {
			result[i] = src.charAt(i);
		}
		
		for (; i < length; ++i) {
			result[i] = c;
		}
		
		return new String(result);
	}
	
	public static String padToLeft(String src, int length) {
		return padToLeft(src, length, ' ');
	}
	
	public static String padToRight(String src, int length, char c) {
		if (length <= 0) {
			throw new IllegalArgumentException("length must be positive (" + length + ")");
		}
		
		if (length <= src.length()) {
			return src;
		}
		
		char[] result = new char[length];
		
		int i = 0, srcLength = src.length();
		
		for (; i < length - srcLength; ++i) {
			result[i] = c;
		}
		
		for (; i < length; ++i) {
			result[i] = src.charAt(i - (length - srcLength));
		}
		
		return new String(result);
	}
	
	public static String padToRight(String src, int length) {
		return padToRight(src, length, ' ');
	}
	
	public static int countWords(String src) {
		int i = 0;
		boolean isWord = false;
		
		for (char c : src.toCharArray()) {
			if (Character.isWhitespace(c)) {
				if (isWord) {
					isWord = false;
					i++;
				}
			} else {
				isWord = true;
			}
		}
		
		if (isWord) {
			i++;
		}
		
		return i;
	}
	
	public static String[] splitWords(String src) {
		String[] result = new String[countWords(src)];
		
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for (char c : src.toCharArray()) {
			if (Character.isWhitespace(c)) {
				if (sb.length() != 0) {
					result[i++] = resetStringBuilder(sb);
				}
			} else {
				sb.append(c);
			}
		}
		
		if (sb.length() != 0) {
			result[i++] = resetStringBuilder(sb);
		}
		
		return result;
	}
	
	public static char[] sequence(char c, int length) {
		char[] result = new char[length];
		Arrays.fill(result, c);
		return result;
	}
	
	public static String stripPrefix(String string, String prefix) {
		if (prefix != null && string.startsWith(prefix)) {
			return string.substring(prefix.length());
		}
		
		return string;
	}
	
	public static String stripSuffix(String string, String suffix) {
		if (suffix != null && string.endsWith(suffix)) {
			return string.substring(suffix.length());
		}
		
		return string;
	}
	
}
