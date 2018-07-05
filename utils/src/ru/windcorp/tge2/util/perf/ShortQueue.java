package ru.windcorp.tge2.util.perf;

import java.util.NoSuchElementException;

public class ShortQueue {
	
	private short[] array;
	private int head = 0;
	private int tail = 0;

	public ShortQueue(int capacity) {
		this.array = new short[capacity];
	}
	
	public short get() {
		if (head == tail) {
			throw new NoSuchElementException();
		}
		
		short result = array[head];
		head = (head + 1) % array.length;
		return result;
	}
	
	public void add(short x) {
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
