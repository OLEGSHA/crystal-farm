package ru.windcorp.tge2.util.njobs;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class NJob {
	
	public static enum State {
		NOT_SCHEDULED,
		SCHEDULED,
		EXECUTING,
		COMPLETED,
		SCHEDULING_FAILED,
		EXECUTION_FAILED
	}
	
	private final String id;
	
	private State state = State.NOT_SCHEDULED;
	private Exception lastException = null;
	
	private final Map<String, Boolean> unmetDependencies = Collections.synchronizedMap(new HashMap<>());
	private final Map<String, Boolean> unmetDependents = Collections.synchronizedMap(new HashMap<>());
	
	private final Collection<WeakReference<NJob>> dependencies = Collections.synchronizedCollection(new LinkedList<>());
	private final Collection<NJob> dependents = Collections.synchronizedCollection(new LinkedList<>());
	
	public NJob(String id, Map<String, Boolean> dependencies, Map<String, Boolean> dependents) {
		this.id = id;
		this.unmetDependencies.putAll(dependencies);
		this.unmetDependents.putAll(dependents);
	}
	
	public NJob(String id,
			String[] dependencies,
			String[] optionalDependencies,
			String[] dependents,
			String[] optionalDependents) {
		
		this.id = id;
		
		if (dependencies != null) {
			for (String jid : dependencies) {
				unmetDependencies.put(jid, true);
			}
		}
		
		if (optionalDependencies != null) {
			for (String jid : optionalDependencies) {
				unmetDependencies.put(jid, false);
			}
		}
		
		if (dependents != null) {
			for (String jid : dependents) {
				unmetDependencies.put(jid, true);
			}
		}
		
		if (optionalDependents != null) {
			for (String jid : optionalDependents) {
				unmetDependencies.put(jid, false);
			}
		}
		
	}

	public String getId() {
		return id;
	}

	public State getState() {
		return state;
	}
	
	protected synchronized void setState(State state) {
		this.state = state;
	}

	public Map<String, Boolean> getUnmetDependencies() {
		return unmetDependencies;
	}

	public Map<String, Boolean> getUnmetDependents() {
		return unmetDependents;
	}

	public Collection<WeakReference<NJob>> getDependencies() {
		return dependencies;
	}

	public Collection<NJob> getDependents() {
		return dependents;
	}
	
	public Exception getLastException() {
		return lastException;
	}

	private void setLastException(Exception lastException) {
		this.lastException = lastException;
	}

	public boolean execute() {
		try {
			run();
			return true;
		} catch (Exception e) {
			setState(State.EXECUTION_FAILED);
			setLastException(e);
			return false;
		}
	}
	
	protected abstract void run() throws Exception;

}
