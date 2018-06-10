package ru.windcorp.tge2.util.jobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import ru.windcorp.tge2.util.synch.Waiter;

public class JobManager<T extends Job> {
	
	private class JobRunner implements Runnable {

		@Override
		public void run() {
			
			T job;
			
			try {
				while ((job = nextJob()) != null) {
					synchronized (getListeners()) {
						for (JobListener<? super T> l : getListeners()) {
							l.onJobStarted(JobManager.this, job, Thread.currentThread());
						}
					}
					
					try {
						job.execute(JobManager.this);
					} catch (Exception e) {
						synchronized (getListeners()) {
							for (JobListener<? super T> l : getListeners()) {
								l.onJobErrored(JobManager.this, Thread.currentThread(), job, e);
							}
						}
						break;
					}
					
					synchronized (getWaiter()) {
						getWaiter().notifyAll();
					}
					
					synchronized (getListeners()) {
						for (JobListener<? super T> l : getListeners()) {
							l.onJobDone(JobManager.this, job, Thread.currentThread());
						}
					}
					
				}
				
				synchronized (getListeners()) {
					for (JobListener<? super T> l : getListeners()) {
						l.onJobsEnd(JobManager.this);
					}
				}

				synchronized (isRunning) {
					if (!isRunning()) {
						return;
					}
					
					isRunning = Boolean.FALSE;
				}
				
				synchronized (getWaiter()) {
					getWaiter().notifyAll();
				}
				
			} catch (InvalidJobSetException e) {
				synchronized (getListeners()) {
					for (JobListener<? super T> l : getListeners()) {
						l.onJobDependencyProblem(JobManager.this, Thread.currentThread());
					}
				}
			}
			
		}
		
	}
	
	private static int nextRunnerId = 0;
	
	private final Map<String, T> jobs = Collections.synchronizedMap(new HashMap<String, T>());
	private final Collection<T> todo = Collections.synchronizedCollection(new LinkedList<T>());
	
	private Boolean isRunning = false;
	private int workers = 0;
	
	private final Map<Object, T> usedObjects = Collections.synchronizedMap(new HashMap<Object, T>());
	
	private final Collection<JobListener<? super T>> listeners = Collections.synchronizedCollection(new ArrayList<JobListener<? super T>>());
	
	private final Waiter waiter = new Waiter();
	
	public Collection<T> getJobs() {
		return jobs.values();
	}
	
	public Collection<T> getJobsLeft() {
		return todo;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public Job getOccupant(Object object) {
		return usedObjects.get(object);
	}
	
	public void occupy(T job, Object object) throws JobException {
		synchronized (usedObjects) {
			if (getOccupant(object) != null) {
				throw new JobException("Object \"" + object + "\" used by job " + job +
						" is currently occupied by job " + getOccupant(object),
						job, job.getState());
			}
			
			usedObjects.put(object, job);
		}
	}
	
	public void release(Object object) {
		usedObjects.remove(object);
	}

	public Collection<JobListener<? super T>> getListeners() {
		return listeners;
	}

	public Waiter getWaiter() {
		return waiter;
	}
	
	public int getWorkers() {
		return workers;
	}

	public void addJob(T job) {
		job.setScheduled();
		
		jobs.put(job.toString(), job);
		getJobsLeft().add(job);
		
		synchronized (getWaiter()) {
			getWaiter().notifyAll();
		}
		
		synchronized (getListeners()) {
			for (JobListener<? super T> l : getListeners()) {
				l.onJobAdded(this, job);
			}
		}
	}
	
	public T getJob(String name) {
		return jobs.get(name);
	}
	
	public void addJobListener(JobListener<? super T> listener) {
		getListeners().add(listener);
	}
	
	public void doJobs() {
		doJobs(Runtime.getRuntime().availableProcessors());
	}
	
	public void doJobs(int threads) {
		synchronized (isRunning) {
			if (isRunning()) {
				throw new IllegalStateException("JobManager " + this + " already running");
			}
			
			isRunning = Boolean.TRUE;
		}
		
		this.workers = threads;
		
		synchronized (getListeners()) {
			for (JobListener<? super T> l : getListeners()) {
				l.onJobsBegin(this, threads);
			}
		}
		
		for (int i = 0; i < threads; ++i) {
			new Thread(new JobRunner(), "Job Runner Thread " + nextRunnerId++).start();
		}
	}
	
	public T nextJob() throws InvalidJobSetException {
		do {
			synchronized (getJobsLeft()) {
				if (getJobsLeft().isEmpty()) {
					return null;
				}
				
				Iterator<T> iterator = getJobsLeft().iterator();
				T j;
			
				while (iterator.hasNext()) {
					j = iterator.next();
					
					if (j.canRun(this)) {
						iterator.remove();
						return j;
					}
				}
			}
			
			synchronized (getWaiter()) {
				if (getWaiter().getWaiting() == getWorkers() - 1) {
					synchronized (isRunning) {
						isRunning = Boolean.FALSE;
					}
					getWaiter().notifyAll();
					throw new InvalidJobSetException("Failed to resolve next job", this);
				}
				
				while (true) {
					try {
						getWaiter().wait2();
						break;
					} catch (InterruptedException e) {
						// Re-enter waiter loop
					}
				}
			}
		} while (isRunning());
		
		return null;
	}

}
