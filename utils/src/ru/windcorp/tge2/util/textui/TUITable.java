package ru.windcorp.tge2.util.textui;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ru.windcorp.tge2.util.StringUtil;

public class TUITable {
	
	private Object[] headers;
	private final List<Object[]> data = Collections.synchronizedList(new LinkedList<Object[]>());
	
	private boolean drawGrid;
	
	public TUITable(boolean drawGrid, Object... headers) {
		this.setDrawGrid(drawGrid);
		this.headers = headers;
	}

	public TUITable(Object... headers) {
		this(true, headers);
	}
	
	public Object[] getHeaders() {
		return headers;
	}
	
	protected void setHeaders(Object[] headers) {
		this.headers = headers;
	}
	
	public synchronized void addHeaders(Object... headers) {
		int oldLength = getHeaders().length;
		resizeTable(oldLength + headers.length);
		System.arraycopy(headers, 0, getHeaders(), oldLength, headers.length);
	}
	
	protected synchronized void resizeTable(int columns) {
		if (columns < 0) {
			throw new IllegalArgumentException("Negative new columns (columns: " + columns + ")");
		}
		
		setHeaders(resize(getHeaders(), columns));
		
		for (int row = 0; row < getRows(); ++row) {
			getData().set(row, resize(getData().get(row), columns));
		}
	}
	
	protected static Object[] resize(Object[] src, int newLength) {
		Object[] result = new Object[newLength];
		System.arraycopy(src, 0, result, 0, Math.min(newLength, src.length));
		
		if (newLength > src.length) {
			Arrays.fill(result, src.length, newLength, "");
		}
		
		return result;
	}
	
	public int getRows() {
		return getData().size();
	}
	
	public int getColumns() {
		return getHeaders().length;
	}
	
	public List<Object[]> getData() {
		return data;
	}
	
	public synchronized void addRow(Object... data) {
		if (data.length != getColumns()) {
			resize(data, getColumns());
		}
		
		getData().add(data);
	}
	
	public synchronized void put(int column, int row, Object obj) {
		if (row < 0) {
			throw new IllegalArgumentException("Row cannot be negative (given " + column + ")");
		}
		
		if (column < 0 || column >= getColumns()) {
			throw new IllegalArgumentException("Column is illegal: given " + column + "; present: " + row);
		}
		
		while (getRows() <= row) {
			getData().add(new Object[getRows()]);
		}
		
		getData().get(row)[column] = obj;
	}
	
	@Override
	public synchronized String toString() {
		synchronized (getData()) {
			if (getColumns() == 0) {
				return "";
			}
			
			String[] headers = new String[getColumns()];
			String[][] data = new String[getRows()][getColumns()];
			int[] widths = new int[getColumns()];
			
			for (int i = 0; i < getColumns(); ++i) {
				headers[i] = String.valueOf(getHeaders()[i]);
				widths[i] = headers[i].length();
			}
			
			for (int row = 0; row < getRows(); ++row) {
				Object[] rowObj = getData().get(row);
				
				for (int column = 0; column < getColumns(); ++column) {
					data[row][column] = String.valueOf(rowObj[column]);
					widths[column] = Math.max(widths[column], data[row][column].length());
				}
			}
			
			StringBuilder sb = new StringBuilder(StringUtil.padToLeft(headers[0], widths[0]));
			
			for (int column = 1; column < getColumns(); ++column) {
				if (getDrawGrid()) {
					sb.append(" | ");
				} else {
					sb.append(" ");
				}
				sb.append(StringUtil.padToLeft(headers[column], widths[column]));
			}
			
			sb.append('\n');
			sb.append(StringUtil.sequence('-', widths[0]));
			
			if (getDrawGrid()) {
				for (int column = 1; column < getColumns(); ++column) {
					sb.append("-+-");
					sb.append(StringUtil.sequence('-', widths[column]));
				}
			}
			
			for (int row = 0; row < data.length; ++row) {
				sb.append('\n');
				for (int column = 0; column < data[row].length; ++column) {
					sb.append(StringUtil.padToLeft(data[row][column], widths[column]));
					
					if (column != data[0].length - 1) {
						if (getDrawGrid()) {
							sb.append(" | ");
						} else {
							sb.append(" ");
						}
					}
				}
			}
			
			return sb.toString();
		}
	}
	
	public boolean getDrawGrid() {
		return drawGrid;
	}

	public void setDrawGrid(boolean drawGrid) {
		this.drawGrid = drawGrid;
	}

}
