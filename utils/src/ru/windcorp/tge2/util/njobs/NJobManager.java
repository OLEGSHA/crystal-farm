package ru.windcorp.tge2.util.njobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class NJobManager {
	
	private final Collection<NJob> availableJobs = Collections.synchronizedCollection(new LinkedList<>());
	private final Collection<NJob> orphanedJobs = Collections.synchronizedCollection(new LinkedList<>());
	
	private final Collection<NJobListener> listeners = Collections.synchronizedCollection(new ArrayList<>());
	
	public void addJob(NJob job) {
		synchronized (job) {
			availableJobs.forEach(availableJob -> {
				
			});
		}
	}

}
