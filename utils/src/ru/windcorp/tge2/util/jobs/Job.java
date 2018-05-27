package ru.windcorp.tge2.util.jobs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import ru.windcorp.tge2.util.Describable;

public abstract class Job extends Describable implements Runnable {
	
	/**
	 * The job has been initialized but has not been scheduled for execution.
	 */
	public static final int STATE_NOT_SCHEDULED = 0;
	
	/**
	 * The job has been scheduled and is currently waiting for execution.
	 */
	public static final int STATE_SCHEDULED = 1;
	
	/**
	 * The job is being executed at the moment.
	 */
	public static final int STATE_EXECUTING = 2;
	
	/**
	 * The job has been executed successfully.
	 */
	public static final int STATE_EXECUTED = 3;
	
	/**
	 * The job has started executing but failed to do so.
	 */
	public static final int STATE_ERRORED = 4;
	
	private int state = STATE_NOT_SCHEDULED;
	
	/**
	 * Jobs that need to be executed before this job.
	 */
	private final Collection<Job> dependencies = Collections.synchronizedCollection(new ArrayList<Job>());
	
	/**
	 * Objects that this job <i>uses</i>. These objects are needed for synchronization. If a one job is using
	 * a certain object and is being executed, all other jobs that use that object will be delayed until the
	 * first job is done. 
	 */
	private final Collection<Object> usedObjects = Collections.synchronizedCollection(new ArrayList<Object>());

	public Job(String name, String description) {
		super(name, description);
	}
	
	public int getState() {
		return state;
	}
	
	public synchronized boolean isExecuted() {
		return state == STATE_EXECUTED;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <T extends Job> void execute(JobManager<T> manager) throws JobException, InvocationTargetException {
		if (getState() != STATE_SCHEDULED) {
			// This is obviously an error - we can spend time here
			
			int state = getState();
			
			switch (state) {
			case STATE_NOT_SCHEDULED:
				throw new JobException("Job has not been scheduled", this, state);
				
			// This shouldn't happen normally - access to method execute() is synchronized
			case STATE_EXECUTING:
				throw new JobException("Job is already executing", this, state);
				
			default:
				throw new JobException("Job has already been executed", this, state);
				
			}
		}
		
		synchronized (usedObjects) {
			for (Object o : usedObjects) {
				manager.occupy((T) this, o);
			}
		}
		
		this.state = STATE_EXECUTING;
		
		try {
			
			run();
			
		} catch (Exception e) {
			this.state = STATE_ERRORED;
			throw new InvocationTargetException(e, "Job " + this + " failed to execute");
		} finally {
			
			synchronized (usedObjects) {
				for (Object o : usedObjects) {
					manager.release(o);
				}
			}
			
		}
		
		this.state = STATE_EXECUTED;
	}
	
	public synchronized boolean canRun(JobManager<?> manager) {
		if (getState() != STATE_SCHEDULED) {
			return false;
		}

		synchronized (dependencies) {
			for (Job j : dependencies) {
				if (!j.isExecuted()) {
					return false;
				}
			}
		}
		
		synchronized (usedObjects) {
			for (Object o : usedObjects) {
				if (manager.getOccupant(o) != null) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public synchronized String debugCanRun(JobManager<?> manager, String ok) {
		if (getState() != STATE_SCHEDULED) {
			return "Job not scheduled";
		}
		
		synchronized (dependencies) {
			for (Job j : dependencies) {
				if (!j.isExecuted()) {
					return "Dependency " + j + " not executed";
				}
			}
		}
		
		if (!usedObjects.isEmpty()) {
			synchronized (usedObjects) {
				Job occupant;
				for (Object object : usedObjects) {
					occupant = manager.getOccupant(object);
					if (occupant != null) {
						return "Object \"" + object + "\" is occupied by job " + occupant;
					}
				}
			}
		}
		
		return ok;
	}
	
	public Collection<Job> getDependencies() {
		return dependencies;
	}
	
	public synchronized void addDependency(Job dependent) {
		this.dependencies.add(dependent);
	}
	
	/**
	 * Objects that this job <i>uses</i>. These objects are needed for synchronization. If a one job is using
	 * a certain object and is being executed, all other jobs that use that object will be delayed until the
	 * first job is done. 
	 * @return the objects that this job uses.
	 */
	public Collection<Object> getUsedObjects() {
		return usedObjects;
	}
	
	/**
	 * Marks an object as <i>used</i> by this job. See {@link Job#getUsedObjects()} for details.
	 * @param object the object to mark as used
	 */
	public synchronized void addUsedObject(Object object) {
		this.usedObjects.add(object);
	}
	
	public synchronized void setScheduled() {
		this.state = STATE_SCHEDULED;
	}

}
