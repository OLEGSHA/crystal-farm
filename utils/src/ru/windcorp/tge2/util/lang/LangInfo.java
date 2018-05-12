package ru.windcorp.tge2.util.lang;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LangInfo {
	
	public static final List<LangInfo> REGISTERED = Collections.synchronizedList(new LinkedList<LangInfo>());
	
	public static LangInfo getLangInfo(String internalName, String fullName) {
		LangInfo result = getLangInfoOrNull(internalName);
		if (result != null) {
			return result;
		}
		
		result = new LangInfo(internalName, fullName);
		REGISTERED.add(result);
		return result;
	}
	
	public static LangInfo getLangInfo(String name) {
		return getLangInfo(name, name);
	}
	
	public static LangInfo getLangInfoOrNull(String name) {
		for (LangInfo l : REGISTERED) {
			if (l.getInternalName().equals(name)) {
				return l;
			}
		}
		
		return null;
	}
	
	private final String internalName;
	private final String fullName;
	
	public LangInfo(String internalName, String fullName) {
		this.internalName = internalName;
		this.fullName = fullName;
	}
	
	public LangInfo(String name) {
		this(name, name);
	}

	public String getInternalName() {
		return internalName;
	}

	public String getFullName() {
		return fullName;
	}
	
	public Lang getLang() {
		return Lang.getInstance(getInternalName());
	}
	
	public Lang setCurrent() {
		Lang lang = getLang();
		Lang.current = lang;
		return lang;
	}
	
	@Override
	public String toString() {
		return getFullName();
	}
	
}
