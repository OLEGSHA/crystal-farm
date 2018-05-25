package ru.windcorp.tge2.util.jobs;

public interface JobListener<T extends Job> {
	
	/**
	 * Called whenever a new job has been added (scheduled) to the job manager.
	 * This method is invoked in the thread that has added the job.
	 * @param manager the job manager
	 * @param job the newly-added job
	 */
	public void onJobAdded(JobManager<? extends T> manager, T job);
	
	/**
	 * Called whenever a job has begun execution.
	 * This method is invoked is the worker thread.
	 * @param manager the job manager
	 * @param job the job that is now being executed
	 * @param worker the worker thread executing the job
	 */
	public void onJobStarted(JobManager<? extends T> manager, T job, Thread worker);
	
	/**
	 * Called when a job has finished executing.
	 * This method is invoked in the worker thread.
	 * @param manager the job manager
	 * @param job the job
	 * @param worker the worker thread that has executed the job
	 */
	public void onJobDone(JobManager<? extends T> manager, T job, Thread worker);
	
	/**
	 * Called when a job manager has begun executing jobs.
	 * This method is invoked in the thread that has initiated the execution.
	 * @param manager the job manager
	 * @param threads the amount of worker threads that are going to be used
	 */
	public void onJobsBegin(JobManager<? extends T> manager, int threads);
	
	/**
	 * Called when a job manager has ran out of job to execute.
	 * This method is invoked in one of the worker threads.
	 * @param manager the job manager
	 */
	public void onJobsEnd(JobManager<? extends T> manager);
	
	/**
	 * Called when a job dependency problem has been encountered by one of the worker threads.
	 * Other worker threads may still be running. This method is invoked in one of the
	 * worker threads.
	 * @param manager the job manager
	 * @param worker the worker thread that has encountered the problem
	 */
	public void onJobDependencyProblem(JobManager<? extends T> manager, Thread worker);
	
	/**
	 * Called when a job has fail to execute.
	 * This method is invoked in one of the worker threads.
	 * @param manager the job manager
	 * @param worker the worker thread where the error occurred
	 * @param job the job that failed
	 * @param exception the exception raised
	 */
	public void onJobErrored(JobManager<? extends T> manager, Thread worker, T job, Exception exception);

}
