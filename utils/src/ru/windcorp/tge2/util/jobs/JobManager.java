package ru.windcorp.tge2.util.jobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class JobManager {
	
	private class JobRunner implements Runnable {

		@Override
		public void run() {
			
			Job job;
			
			try {
				while ((job = nextJob()) != null) {
					synchronized (getListeners()) {
						for (JobListener l : getListeners()) {
							l.onJobStarted(JobManager.this, job, Thread.currentThread());
						}
					}
					
					try {
						job.execute(JobManager.this);
					} catch (Exception e) {
						synchronized (getListeners()) {
							for (JobListener l : getListeners()) {
								l.onJobErrored(JobManager.this, Thread.currentThread(), job, e);
							}
						}
						break;
					}
					
					synchronized (getListeners()) {
						for (JobListener l : getListeners()) {
							l.onJobDone(JobManager.this, job, Thread.currentThread());
						}
					}
					
				}
			} catch (InvalidJobSetException e) {
				synchronized (getListeners()) {
					for (JobListener l : getListeners()) {
						l.onJobDependencyProblem(JobManager.this, Thread.currentThread());
					}
				}
			}
			
		}
		
	}
	
	private static int nextRunnerId = 0;
	
	private final Collection<Job> jobs = Collections.synchronizedCollection(new ArrayList<Job>());
	private final Collection<Job> todo = Collections.synchronizedCollection(new LinkedList<Job>());
	
	private Boolean isRunning = false;
	
	private final Map<Object, Job> usedObjects = Collections.synchronizedMap(new HashMap<Object, Job>());
	
	private final Collection<JobListener> listeners = Collections.synchronizedCollection(new ArrayList<JobListener>());
	
	/**
	 * Object for worker threads to wait() on.
	 */
	private final Object hook = new Object();
	
	public Collection<Job> getJobs() {
		return jobs;
	}
	
	public Collection<Job> getJobsLeft() {
		return todo;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public Job getOccupant(Object object) {
		return usedObjects.get(object);
	}
	
	public void occupy(Job job, Object object) throws JobException {
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

	public Collection<JobListener> getListeners() {
		return listeners;
	}

	public Object getHook() {
		return hook;
	}

	public void addJob(Job job) {
		getJobs().add(job);
		getJobsLeft().add(job);
		
		synchronized (getListeners()) {
			for (JobListener l : getListeners()) {
				l.onJobAdded(this, job);
			}
		}
	}
	
	public void addJobListener(JobListener listener) {
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
		
		synchronized (getListeners()) {
			for (JobListener l : getListeners()) {
				l.onJobsBegin(this, threads);
			}
		}
		
		for (int i = 0; i < threads; ++i) {
			new Thread(new JobRunner(), "Job Runner Thread " + nextRunnerId++).start();
		}
	}
	
	public Job nextJob() throws InvalidJobSetException {
		synchronized (getJobsLeft()) {
			if (getJobsLeft().isEmpty()) {
				return null;
			}
			
			Iterator<Job> iterator = getJobsLeft().iterator();
			Job j;
		
			while (iterator.hasNext()) {
				j = iterator.next();
				
				if (j.canRun(this)) {
					iterator.remove();
					return j;
				}
				
			}
			
			throw new InvalidJobSetException("Failed to resolve next job", this);
		}
	}

}
