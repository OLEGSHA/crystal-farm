package ru.windcorp.tge2.util.synch;

import java.util.concurrent.atomic.AtomicInteger;

public class Waiter {
	
	private AtomicInteger waiting = new AtomicInteger(0);
	
	public int getWaiting() {
		return waiting.get();
	}
	
	public void wait2() throws InterruptedException {
		waiting.incrementAndGet();
		wait();
		waiting.decrementAndGet();
	}
	
	public void wait2(long timeout) throws InterruptedException {
		waiting.incrementAndGet();
		wait(timeout);
		waiting.decrementAndGet();
	}
	
	public void wait2(long timeout, int nanos) throws InterruptedException {
		waiting.incrementAndGet();
		wait(timeout, nanos);
		waiting.decrementAndGet();
	}

}
