package ru.windcorp.tge2.util.jobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.synch.Waiter;

/*
 * TODO: remove debug code when sure that no problems arise
 * 
 * Synchronization seems to deadlock sometime, so leaving this ugly code here for some time.
 * - OLEGSHA
 */
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
				
				Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + " exited loop");
				
				synchronized (isRunning) {
					if (!isRunning()) {
						Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + " walked away quietly");
						
						if (workers == 1) {
							synchronized (getListeners()) {
								for (JobListener<? super T> l : getListeners()) {
									l.onJobsEnd(JobManager.this);
								}
							}
						}
						
						return;
					}
					
					Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + " ended JM");
					isRunning = Boolean.FALSE;
				}
				
				Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + " screaming \"HOORAY!\" at the top of its lungs");
				
				synchronized (getWaiter()) {
					getWaiter().notifyAll();
				}
				
				Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + " ran away");
				
			} catch (InvalidJobSetException e) {
				synchronized (getListeners()) {
					for (JobListener<? super T> l : getListeners()) {
						l.onJobDependencyProblem(JobManager.this, Thread.currentThread());
					}
				}
			} finally {
				workers--;
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
		Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + " entered nextJob()");
		do {
			synchronized (getJobsLeft()) {
				Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + "   entered synchronized (getLeftJobs())");
				if (getJobsLeft().isEmpty()) {
					Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + " encountered no jobs");
					return null;
				}
				
				Iterator<T> iterator = getJobsLeft().iterator();
				T j;
				
				Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + "   started iterating");
			
				while (iterator.hasNext()) {
					j = iterator.next();
					
					if (j.canRun(this)) {
						iterator.remove();
						Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + " found a job " + j.toString());
						return j;
					}
				}
			}
			
			Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + "   exited synchronized (getLeftJobs())");
			
			synchronized (getWaiter()) {
				Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + "   entered synchronized (getWaiter())");
				if (getWaiter().getWaiting() == getWorkers() - 1) {
					Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + "   found dep problem");
					synchronized (isRunning) {
						isRunning = Boolean.FALSE;
					}
					getWaiter().notifyAll();
					Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + " about to complain about dep problem");
					throw new InvalidJobSetException("Failed to resolve next job", this);
				}
				
				while (true) {
					try {
						Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + "   falling asleep");
						getWaiter().wait2();
						Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + "   awoken");
						break;
					} catch (InterruptedException e) {
						// Re-enter waiter loop
					}
				}
			}
		} while (isRunning());
		Log.debug("\t\t\t\t\t" + Thread.currentThread().getName() + " is slowpoke");
		return null;
	}

}
