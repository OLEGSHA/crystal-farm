package ru.windcorp.tge2.util.perf;

import java.util.NoSuchElementException;

public class StringQueue {
	
	private String[] array;
	private int head = 0;
	private int tail = 0;

	public StringQueue(int capacity) {
		this.array = new String[capacity];
	}
	
	public String get() {
		if (head == tail) {
			throw new NoSuchElementException();
		}
		
		String result = array[head];
		head = (head + 1) % array.length;
		return result;
	}
	
	public void add(String x) {
		if (head == (tail + 1) % array.length) {
			throw new IllegalStateException("Queue is full (" + array.length + " elements present)");
		}
		
		array[tail] = x;
		tail = (tail + 1) % array.length;
	}
	
	public int getLength() {
		if (tail > head) {
			return tail - head;
		}
		
		return array.length - tail + head;
	}

}
