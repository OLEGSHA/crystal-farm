package ru.windcorp.tge2.util.jobs;

public class InvalidJobSetException extends Exception {
	
	private static final long serialVersionUID = 764975536479024052L;
	
	private final JobManager<?> manager;
	
	public InvalidJobSetException(String message, JobManager<?> manager) {
		super(message);
		this.manager = manager;
	}

	public JobManager<?> getManager() {
		return manager;
	}

}
