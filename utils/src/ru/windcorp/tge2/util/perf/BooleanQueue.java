package ru.windcorp.tge2.util.perf;

import java.util.NoSuchElementException;

public class BooleanQueue {
	
	private boolean[] array;
	private int head = 0;
	private int tail = 0;

	public BooleanQueue(int capacity) {
		this.array = new boolean[capacity];
	}
	
	public boolean get() {
		if (head == tail) {
			throw new NoSuchElementException();
		}
		
		boolean result = array[head];
		head = (head + 1) % array.length;
		return result;
	}
	
	public void add(boolean x) {
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
