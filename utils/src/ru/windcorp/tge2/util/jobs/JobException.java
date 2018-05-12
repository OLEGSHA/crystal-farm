package ru.windcorp.tge2.util.jobs;

public class JobException extends Exception {
	
	private static final long serialVersionUID = -2490720537229732791L;
	
	private final Job job;
	private final int jobState;
	
	public JobException(String message, Job job, int jobState) {
		super(message);
		this.job = job;
		this.jobState = jobState;
	}

	public Job getJob() {
		return job;
	}

	public int getJobState() {
		return jobState;
	}

}
