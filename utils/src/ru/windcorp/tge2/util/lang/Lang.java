package ru.windcorp.tge2.util.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.windcorp.tge2.util.StringUtil;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.interfaces.Generator;

public class Lang {
	
	public static Lang current = null;
	public static Generator<String, InputStream> langGenerator = null;
	
	private static final List<Lang> LANGS = Collections.synchronizedList(new ArrayList<Lang>());
	
	private final Map<String, String> map = new HashMap<String, String>();
	private final String name;
	
	private static final char ESCAPE_CHAR = '\\';
	private static final char UNICODE_CHAR = 'u';
	private static final char[] UNSAFE_CHARS = new char[] { '\n', '\r' };
	private static final char[] SAFE_CHARS = new char[]   { 'n',  'r'  };

	public synchronized static Lang getInstance(String name) {
		for (Lang l : LANGS) {
			if (l.getName().equals(name)) {
				return l;
			}
		}
		
		return langGenerator == null ? null : create(langGenerator.generate(name), name);
	}
	
	public synchronized static Lang create(InputStream input, String name) {
		Lang lang = new Lang(name);
		
		if (input == null) {
			ru.windcorp.tge2.util.ExceptionHandler.handlef(new NullPointerException(),
					"Failed to read lang %s",
					name);
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line;
			String[] parts;
			
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("#")) {
					parts = StringUtil.split(line, '=', 2);
					
					if (parts[1] == null) {
						lang.getMap().put(parts[0], "null");
					} else {
						parts[1] = new String(StringUtil.parseEscapeCharacters(parts[1].toCharArray(),
										ESCAPE_CHAR,
										UNICODE_CHAR,
										SAFE_CHARS,
										UNSAFE_CHARS
										));
						lang.getMap().put(parts[0], parts[1]);
					}
				}
			}
		} catch (IOException e) {
			ru.windcorp.tge2.util.ExceptionHandler.handlef(e,
					"Failed to read lang %s",
					name);
		} catch (SyntaxException e) {
			ru.windcorp.tge2.util.ExceptionHandler.handlef(e,
					"Failed to parse lang %s",
					name);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				ru.windcorp.tge2.util.ExceptionHandler.handlef(e,
						"Failed to close InputStream %s for lang %s",
						input.toString(), name);
			}
		}
		
		LANGS.add(lang);
		return lang;
	}
	
	public Lang(String name) {
		this.name = name;
	}

	public Map<String, String> getMap() {
		return map;
	}
	
	public String getName() {
		return name;
	}
	
	public String instGet(String key) {
		return instGet(key, key);
	}
	
	public String instGet(String key, String def) {
		String result = getMap().get(key);
		return result == null ? def : result;
	}
	
	public String instGetf(String key, Object... args) {
		return String.format(instGet(key), args);
	}
	
	public String instGetfd(String key, String def, Object... args) {
		return String.format(instGet(key, def), args);
	}
	
	public static String get(String key) {
		return current == null ? key : current.instGet(key);
	}
	
	public static String get(String key, String def) {
		return current == null ? def : current.instGet(key, def);
	}
	
	public static String getf(String key, Object... args) {
		return current == null ? String.format(key, args) : current.instGetf(key, args);
	}
	
	public static String getfd(String key, String def, Object... args) {
		return current == null ? String.format(def, args) : current.instGetfd(key, def, args);
	}

}
