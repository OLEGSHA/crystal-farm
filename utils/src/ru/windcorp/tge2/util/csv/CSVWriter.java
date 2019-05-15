package ru.windcorp.tge2.util.csv;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class CSVWriter {
	
	private String columnSeparator = ";";
	private String rowSeparator = "\n";
	
	private boolean shouldAddSeparator = false;
	
	private final PrintWriter parent;
	
	public CSVWriter(PrintWriter output) {
		this.parent = output;
	}
	
	public CSVWriter(Writer output) {
		this(new PrintWriter(output));
	}
	
	public CSVWriter(OutputStream output) {
		this(new PrintWriter(output));
	}
	
	public PrintWriter getParent() {
		return parent;
	}
	
	public String getColumnSeparator() {
		return columnSeparator;
	}
	
	public CSVWriter setColumnSeparator(String columnSeparator) {
		this.columnSeparator = columnSeparator;
		return this;
	}
	
	public String getRowSeparator() {
		return rowSeparator;
	}
	
	public CSVWriter setRowSeparator(String rowSeparator) {
		this.rowSeparator = rowSeparator;
		return this;
	}
	
	public void print(Object object) {
		skip();
		getParent().print(String.valueOf(object));
	}
	
	public void skip() {
		if (shouldAddSeparator) {
			getParent().print(getColumnSeparator());
		} else {
			shouldAddSeparator = true;
		}
	}
	
	public void skip(int amount) {
		for (int i = 0; i < amount; ++i) {
			skip();
		}
	}
	
	public void endRow() {
		getParent().print(getRowSeparator());
		shouldAddSeparator = false;
	}
	
	public void endRow(Object object) {
		print(object);
		endRow();
	}
	
	public void flush() {
		getParent().flush();
	}
	
	public void close() {
		getParent().close();
	}

}
