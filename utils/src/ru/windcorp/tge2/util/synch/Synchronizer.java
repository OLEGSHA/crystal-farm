package ru.windcorp.tge2.util.synch;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Synchronizer implements Runnable {
	
	private class DelayedTask extends TimerTask {
		private final Runnable runnable;
		
		public DelayedTask(Runnable r) {
			runnable = r;
		}

		@Override
		public void run() {
			add(runnable);
		}
	}
	
	private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<Runnable>();
	private boolean running = true;
	
	private final Object lock = new Object();
	
	private final Timer delayedTaskPlacer;
	
	public Synchronizer(String name) {
		delayedTaskPlacer = new Timer("Delayed Task Placer for Synchronizer \"" + name + "\"");
		new Thread(this, name).start();
	}

	public Queue<Runnable> getTasks() {
		return tasks;
	}
	
	protected Timer getDelayedTaskPlacer() {
		return delayedTaskPlacer;
	}

	public boolean isRunning() {
		return running;
	}
	
	public void stop() {
		this.running = false;
	}

	public void add(Runnable task) {
		getTasks().add(task);
		SynchUtil.unlock(lock);
	}
	
	public void add(Runnable task, long delay) {
		getDelayedTaskPlacer().schedule(new DelayedTask(task), delay);
	}

	@Override
	public void run() {
		while (isRunning()) {
			while (getTasks().isEmpty()) {
				SynchUtil.lock(lock);
			}
			
			try {
				getTasks().poll().run();
			} catch (Exception e) {
				ru.windcorp.tge2.util.ExceptionHandler.handle(e,
						"Exception in synchronizer thread");
			} catch (Throwable t) {
				t.printStackTrace();
				System.err.println("Error in synchronizer thread, terminating...");
				stop();
				return;
			}
		}
	}

}
