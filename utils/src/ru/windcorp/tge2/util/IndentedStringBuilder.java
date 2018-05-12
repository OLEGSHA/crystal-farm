package ru.windcorp.tge2.util;

import java.util.Arrays;

public class IndentedStringBuilder {

	private final StringBuilder sb = new StringBuilder();
	private int indent = 0;
	private String prefix = "";
	private char indentChar = ' ';
	private int indentCharMultiplier = 1;
	
	public IndentedStringBuilder() {}
	
	public IndentedStringBuilder(char indentChar, int indentCharMultiplier) {
		this.setIndentChar(indentChar);
		this.setIndentCharMultiplier(indentCharMultiplier);
	}
	
	@Override
	public String toString() {
		return getStringBuilder().toString();
	}

	public StringBuilder getStringBuilder() {
		return sb;
	}

	public int getIndent() {
		return indent;
	}

	public void setIndent(int indent) {
		this.indent = indent;
		updatePrefix();
	}
	
	public char getIndentChar() {
		return indentChar;
	}

	public void setIndentChar(char indentChar) {
		this.indentChar = indentChar;
	}

	public int getIndentCharMultiplier() {
		return indentCharMultiplier;
	}

	public void setIndentCharMultiplier(int indentCharMultiplier) {
		this.indentCharMultiplier = indentCharMultiplier;
		updatePrefix();
	}

	private void updatePrefix() {
		char[] array = new char[getIndent() * getIndentCharMultiplier()];
		Arrays.fill(array, getIndentChar());
		this.prefix = new String(array);
	}
	
	public IndentedStringBuilder indent() {
		setIndent(getIndent() + 1);
		return this;
	}
	
	public IndentedStringBuilder unindent() {
		setIndent(getIndent() - 1);
		return this;
	}
	
	public IndentedStringBuilder append(Object x) {
		String[] lines = StringUtil.split(x.toString(), '\n');
		
		assert lines.length > 0;
		
		appendRaw(lines[0]);
		
		for (int i = 1; i < lines.length; ++i) {
			breakLine();
			appendRaw(lines[i]);
		}
		
		return this;
	}
	
	public IndentedStringBuilder appendRaw(String str) {
		getStringBuilder().append(str);
		return this;
	}
	
	public IndentedStringBuilder breakLine() {
		getStringBuilder().append('\n');
		getStringBuilder().append(prefix);
		return this;
	}
	
}
