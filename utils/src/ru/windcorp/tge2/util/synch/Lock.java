package ru.windcorp.tge2.util.synch;

public class Lock {
	
	private volatile boolean flag = false;
	private boolean isInterrupted = false;
	
	public void lock() {
		synchronized (this) {
			try {
				while (flag) {
					this.wait();
				}
			} catch (InterruptedException e) {
				flag = false;
				setInterrupted(true);
			}
		}
	}
	
	public void unlock() {
		synchronized (this) {
			hideFlag();
			this.notify();
		}
	}
	
	public void raiseFlag() {
		flag = true;
	}
	
	public void hideFlag() {
		flag = false;
	}

	public boolean isInterrupted() {
		return isInterrupted;
	}

	public void setInterrupted(boolean isInterrupted) {
		this.isInterrupted = isInterrupted;
	}
	
}
