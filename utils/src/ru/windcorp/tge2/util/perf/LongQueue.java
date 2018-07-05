package ru.windcorp.tge2.util.perf;

import java.util.NoSuchElementException;

public class LongQueue {
	
	private long[] array;
	private int head = 0;
	private int tail = 0;

	public LongQueue(int capacity) {
		this.array = new long[capacity];
	}
	
	public long get() {
		if (head == tail) {
			throw new NoSuchElementException();
		}
		
		long result = array[head];
		head = (head + 1) % array.length;
		return result;
	}
	
	public void add(long x) {
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
