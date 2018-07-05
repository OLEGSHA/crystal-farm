package ru.windcorp.tge2.util.perf;

import java.util.NoSuchElementException;

public class ByteQueue {
	
	private byte[] array;
	private int head = 0;
	private int tail = 0;

	public ByteQueue(int capacity) {
		this.array = new byte[capacity];
	}
	
	public byte get() {
		if (head == tail) {
			throw new NoSuchElementException();
		}
		
		byte result = array[head];
		head = (head + 1) % array.length;
		return result;
	}
	
	public void add(byte x) {
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
