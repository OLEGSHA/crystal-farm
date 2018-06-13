package ru.windcorp.tge2.util.debug.er;

import java.io.File;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ru.windcorp.tge2.util.ExceptionHandler;
import ru.windcorp.tge2.util.Nameable;
import ru.windcorp.tge2.util.StringBuilderOutputStream;
import ru.windcorp.tge2.util.debug.Debug;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport.DamagedResourceReport.DamagedResourceIssue;
import ru.windcorp.tge2.util.debug.er.ExecutionReport.ProblemReport.ProblemLevel;

/**
 * Execution report holding information about application's status for debug purposes.
 * <p>
 * <b>Error types:</b>
 * <ul>
 * <li><b>Critical</b> level errors are severe problems that automatically terminate the application.
 * Use this to report last critical error only.<br><i>Example:</i> a graphical application failed to
 * initialize GUI.</li>
 * <li><b>Error</b> level errors are important problems that should call user's attention as further
 * malfunction is expected.<br><i>Example:</i> an application failed to initialize its sound engine.</li>
 * <li><b>Warning</b> level errors are problems that are not critical but indicate an abnormal situation.
 * <br><i>Example:</i> a plug-in failed to load.</li>
 * <li><b>Notification</b> level errors are situations that albeit unwanted can occur normally.
 * <br><i>Example:</i> an application failed to check for updates as update server is not available.</li>
 * <li><b>Debug</b> level errors are problems that can only concern programmers or experienced users.
 * They are ignored unless debug is {@linkplain ru.windcorp.tge2.util.debug.Debug#setDebugMode(boolean) enabled}.
 * <br><i>Example:</i> an application's resources seem to have been modified.</li>
 * <li><b>Uncaught</b> level is assigned to all problems raised due to uncaught exceptions, including
 * reports about the exceptions themselves.</li>
 * </ul>
 * <p>
 * <b>Damaged resource problems:</b>
 * <ul>
 * <li><b>Missing</b> resources are confirmed to not be present at expected location. Do not use for
 * timed-out Internet resource requests, use "unreachable" instead.<br><i>Example:</i> Web file was not
 * found as the server returned 404 HTTP status code.<br><i>Example:</i> Local database file does not exist.</li>
 * <li><b>Unreachable</b> resources could not be accessed for any reason other than not existing.
 * <br><i>Example:</i> An application was denied to write data to a file.<br><i>Example:</i> An FTP request
 * has timed out.</li>
 * <li><b>Corrupted</b> resources could not be read or written due to syntax errors, illegal values, invalid
 * checksums, etc.<br><i>Example:</i> An integer field could not be parsed in a configuration file.</li>
 * <li><b>Not supported</b> resources do not comply with application requirements.<br><i>Example:</i>
 * A texture has been supplied in JPG format while PNG is required.<br><i>Example:</i> A file is indicated as a
 * plug-in but no plug-in description file has been found.<br><i>Example:</i> A file indicates an incompatible
 * application version.</li>
 * </ul>
 * 
 * @author OLEGSHA
 */
public class ExecutionReport {
	
	public static abstract class DebugValueSection extends Nameable {
		
		public DebugValueSection(String name) {
			super(name.toUpperCase());
		}

		public int getMaxNameLength() {
			int maxNameLength = 0;
			for (String s : getNames()) {
				if (s.length() > maxNameLength) {
					maxNameLength = s.length();
				}
			}
			
			return maxNameLength;
		}
		
		public abstract String[] getNames();
		public abstract Object[] getValues();
		
	}
	
	public static class ProblemReport {
		
		public static enum ProblemLevel {
			CRITICAL_ERROR,
			ERROR,
			WARNING,
			NOTIFICATION,
			DEBUG,
			UNCAUGHT;
			
			@Override
			public String toString() {
				return name().replace('_', ' ');
			}
		}

		private static int nextId = 0;
		
		private final int id = nextId++;
		private final int damagedResourceId;
		private final Date date;
		private final Throwable e;
		private final String description;
		private final String problemId;
		private final ProblemLevel type;
		
		public ProblemReport(Throwable e,
				DamagedResourceReport damagedResource,
				String description, String problemId,
				ProblemLevel type) {
			
			this.date = new Date();
			this.e = e;
			this.description = description;
			this.problemId = problemId;
			this.type = type;
			
			if (damagedResource == null) {
				this.damagedResourceId = -1;
			} else {
				this.damagedResourceId = damagedResource.getId();
				damagedResource.linkToProblem(this);
			}
		}

		public Date getDate() {
			return date;
		}

		public Throwable getThrowable() {
			return e;
		}

		public String getDescription() {
			return description;
		}

		public String getProblemId() {
			return problemId;
		}

		public ProblemLevel getType() {
			return type;
		}
		
		public int getId() {
			return id;
		}

		public int getDamagedResourceId() {
			return damagedResourceId;
		}
		
	}
	
	public static class DamagedResourceReport {
		
		public static enum DamagedResourceIssue {
			MISSING,
			CORRUPTED,
			UNREACHABLE,
			NOT_SUPPORTED;
			
			@Override
			public String toString() {
				return name().replace('_', ' ');
			}
		}
		
		private static int nextId = 0;
		
		private final int id = nextId++;
		private int problemId = -1;
		private final String resource;
		private final String description;
		
		private ProblemLevel level = ProblemLevel.WARNING;
		private final DamagedResourceIssue issue;
		
		public DamagedResourceReport(String resource, String description, DamagedResourceIssue problem) {
			this.resource = resource;
			this.description = description;
			this.issue = problem;
		}

		public String getResource() {
			return resource;
		}

		public String getDescription() {
			return description;
		}

		public DamagedResourceIssue getIssue() {
			return issue;
		}
		
		public ProblemLevel getLevel() {
			return level;
		}
		
		public int getId() {
			return id;
		}
		
		public int getProblemId() {
			return problemId;
		}
		
		public void linkToProblem(ProblemReport error) {
			this.problemId = error.getId();
			this.level = error.getType();
		}
		
	}
	
	public static abstract class SuggestionProvider extends Nameable {

		public SuggestionProvider(String name) {
			super(name);
		}
		
		/**
		 * "Try X ." / "Check X ." / "Do X ."
		 */
		public abstract List<String> createSuggestions();
		
	}
	
	public static abstract class ReportPart extends Nameable {

		public ReportPart(String name) {
			super(name.toUpperCase());
		}
		
		protected abstract void printBody(StringBuilder b);
		
		public void print(StringBuilder b) {
			b.append('\n');
			b.append(getName());
			b.append('\n');
			
			printBody(b);
			
			b.append('\n');
		}
		
	}
	
	private static final List<ProblemReport> PROBLEMS =
			Collections.synchronizedList(new LinkedList<ProblemReport>());
	
	private static int surpressedDebugProblems = 0;
	
	private static final List<DebugValueSection> VALUE_SECTIONS =
			Collections.synchronizedList(new ArrayList<DebugValueSection>());
	
	private static final List<DamagedResourceReport> DAMAGED_RESOURCES =
			Collections.synchronizedList(new LinkedList<DamagedResourceReport>());
	
	private static final List<SuggestionProvider> SUGGESTION_PROVIDERS =
			Collections.synchronizedList(new LinkedList<SuggestionProvider>());
	
	private static final List<ReportPart> EXTRA_REPORT_PARTS =
			Collections.synchronizedList(new LinkedList<ReportPart>());
	
	private static final Collection<ExecutionReportListener> LISTENERS =
			Collections.synchronizedCollection(new ArrayList<>());
	
	private static ExecutionReportBackend backend = new ExecutionReportBackendDefault();
	
	private static int maxDVNameLength = 0;
	private static boolean shutdownHookAdded = false;

	private static boolean forceReport = false;

	public static List<ProblemReport> getProblems() {
		return PROBLEMS;
	}

	public static int getSurpressedDebugProblems() {
		return surpressedDebugProblems;
	}

	public static List<DebugValueSection> getValueSections() {
		return VALUE_SECTIONS;
	}

	public static List<DamagedResourceReport> getDamagedResources() {
		return DAMAGED_RESOURCES;
	}
	
	public static List<SuggestionProvider> getSuggestionProviders() {
		return SUGGESTION_PROVIDERS;
	}

	public static List<ReportPart> getExtraReportParts() {
		return EXTRA_REPORT_PARTS;
	}
	
	public static Collection<ExecutionReportListener> getListeners() {
		return LISTENERS;
	}
	
	public static void addListener(ExecutionReportListener listener) {
		getListeners().add(listener);
	}
	
	public static void removeListener(ExecutionReportListener listener) {
		getListeners().remove(listener);
	}
	
	

	public static synchronized ProblemReport registerProblem(ProblemLevel type,
			Throwable e,
			DamagedResourceReport damagedResource,
			String format,
			Object... params) {
		
		ProblemReport report = new ProblemReport(
				e,
				damagedResource,
				String.format(format, params),
				String.format("%08X", format.hashCode()),
				type);
		
		getProblems().add(report);
		
		synchronized (getListeners()) {
			for (ExecutionReportListener l : getListeners()) {
				l.onProblem(report);
			}
		}
		
		if (damagedResource != null) {
			registerDamagedResource(damagedResource);
		}
		
		if (e instanceof Exception && type != ProblemLevel.DEBUG) {
			ExceptionHandler.handle((Exception) e, String.format(format, params));
		}
		
		return report;
	}
	
	public static synchronized DamagedResourceReport createDamagedResourceReport(String resource,
			DamagedResourceIssue issue,
			String description,
			Object... args) {
		return new DamagedResourceReport(resource, String.format(description, args), issue);
	}
	
	/**
	 * Creates a {@link DamagedResourceReport} indicating a missing resource.
	 * Convenience method for
	 * <p>
	 * <code>createDamagedResourceReport(resource,
	 * ExecutionReport.DamagedResourceReport.DamagedResourceProblem.MISSING,
	 * description, args)</code>
	 * @param resource Damaged resource, e.g. <code>"C:\Dir\File.txt"</code> or
	 * <code>"http://example.com/file"</code> or <code>"module description file"</code>.
	 * @param description
	 * <a href=http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax>format string</a>
	 * specifying the problem or providing additional details.
	 * @param args format string arguments for <code>description</code>.
	 * @return damaged resource report
	 */
	public static DamagedResourceReport rscMissing(String resource, String description, Object... args) {
		return createDamagedResourceReport(resource, DamagedResourceIssue.MISSING, description, args);
	}
	
	/**
	 * Creates a {@link DamagedResourceReport} indicating a corrupted resource.
	 * Convenience method for
	 * <p>
	 * <code>createDamagedResourceReport(resource,
	 * ExecutionReport.DamagedResourceReport.DamagedResourceProblem.CORRUPTED,
	 * description, args)</code>
	 * @param resource Damaged resource, e.g. <code>"C:\Dir\File.txt"</code> or
	 * <code>"http://example.com/file"</code> or <code>"module description file"</code>.
	 * @param description
	 * <a href=http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax>format string</a>
	 * specifying the problem or providing additional details.
	 * @param args format string arguments for <code>description</code>.
	 * @return damaged resource report
	 */
	public static DamagedResourceReport rscCorrupt(String resource, String description, Object... args) {
		return createDamagedResourceReport(resource, DamagedResourceIssue.CORRUPTED, description, args);
	}
	
	/**
	 * Creates a {@link DamagedResourceReport} indicating a uncreachable resource.
	 * Convenience method for
	 * <p>
	 * <code>createDamagedResourceReport(resource,
	 * ExecutionReport.DamagedResourceReport.DamagedResourceProblem.CORRUPTED,
	 * description, args)</code>
	 * @param resource Damaged resource, e.g. <code>"C:\Dir\File.txt"</code> or
	 * <code>"http://example.com/file"</code> or <code>"module description file"</code>.
	 * @param description
	 * <a href=http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax>format string</a>
	 * specifying the problem or providing additional details.
	 * @param args format string arguments for <code>description</code>.
	 * @return damaged resource report
	 */
	public static DamagedResourceReport rscUnrch(String resource, String description, Object... args) {
		return createDamagedResourceReport(resource, DamagedResourceIssue.UNREACHABLE, description, args);
	}
	
	/**
	 * Creates a {@link DamagedResourceReport} indicating a not supported resource.
	 * Convenience method for
	 * <p>
	 * <code>createDamagedResourceReport(resource,
	 * ExecutionReport.DamagedResourceReport.DamagedResourceProblem.NOT_SUPPORTED,
	 * description, args)</code>
	 * @param resource Damaged resource, e.g. <code>"C:\Dir\File.txt"</code> or
	 * <code>"http://example.com/file"</code> or <code>"module description file"</code>.
	 * @param description
	 * <a href=http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax>format string</a>
	 * specifying the problem or providing additional details.
	 * @param args format string arguments for <code>description</code>.
	 * @return damaged resource report
	 */
	public static DamagedResourceReport rscNotSupp(String resource, String description, Object... args) {
		return createDamagedResourceReport(resource, DamagedResourceIssue.NOT_SUPPORTED, description, args);
	}

	public static synchronized void registerDamagedResource(DamagedResourceReport report) {
		getDamagedResources().add(report);
		log(report.getLevel(), "Resource " + report.getResource() +
				" is " + report.getIssue().toString() +
				": " + report.getDescription());
		
		synchronized (getListeners()) {
			for (ExecutionReportListener l : getListeners()) {
				l.onDamagedResource(report);
			}
		}
	}
	
	/**
	 * Report a critical error. The application will terminate.
	 * This method never returns normally.
	 * Application exit status is calculated using
	 * <code>format.hashCode()</code>.
	 * @param e Throwable linked to this error. May be <code>null</code>.
	 * @param damagedResource {@link DamagedResourceReport} linked to this error. May be <code>null</code>.
	 * @param format description
	 * <a href=http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax>format string</a>.
	 * @param params Format string arguments.
	 */
	public static void reportCriticalError(Throwable e,
			DamagedResourceReport damagedResource,
			String format,
			Object... params) {
		
		if (format == null) {
			if (damagedResource != null && damagedResource.getDescription() != null)
				format = damagedResource.getDescription() + " (" + damagedResource.getResource() + ")";
			else
				format = "Null description provided";
		}
		
		Log.critical(String.format(format, params));
		ProblemReport report = registerProblem(ProblemLevel.CRITICAL_ERROR, e, damagedResource, format, params);
		Log.critical("Critical problem encountered, application will terminate");
		getBackend().requestExit(report);
	}
	
	/**
	 * Report an error.
	 * @param e Throwable linked to this error. May be <code>null</code>.
	 * @param damagedResource {@link DamagedResourceReport} linked to this error. May be <code>null</code>.
	 * @param format description
	 * <a href=http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax>format string</a>.
	 * @param params Format string arguments.
	 */
	public static void reportError(Throwable e,
			DamagedResourceReport damagedResource,
			String format,
			Object... params) {
		
		if (format == null) {
			if (damagedResource != null && damagedResource.getDescription() != null)
				format = damagedResource.getDescription() + " (" + damagedResource.getResource() + ")";
			else
				format = "Null description provided";
		}
		
		Log.error(String.format(format, params));
		registerProblem(ProblemLevel.ERROR, e, damagedResource, format, params);
	}
	
	/**
	 * Report a warning.
	 * @param e Throwable linked to this error. May be <code>null</code>.
	 * @param damagedResource {@link DamagedResourceReport} linked to this error. May be <code>null</code>.
	 * @param format description
	 * <a href=http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax>format string</a>.
	 * @param params Format string arguments.
	 */
	public static void reportWarning(Throwable e,
			DamagedResourceReport damagedResource,
			String format,
			Object... params) {
		
		if (format == null) {
			if (damagedResource != null && damagedResource.getDescription() != null)
				format = damagedResource.getDescription() + " (" + damagedResource.getResource() + ")";
			else
				format = "Null description provided";
		}
		
		Log.warn(String.format(format, params));
		registerProblem(ProblemLevel.WARNING, e, damagedResource, format, params);
	}
	
	/**
	 * Report a notification.
	 * @param e Throwable linked to this error. May be <code>null</code>.
	 * @param damagedResource {@link DamagedResourceReport} linked to this error. May be <code>null</code>.
	 * @param format description
	 * <a href=http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax>format string</a>.
	 * @param params Format string arguments.
	 */
	public static void reportNotification(Throwable e,
			DamagedResourceReport damagedResource,
			String format,
			Object... params) {
		
		if (format == null) {
			if (damagedResource != null && damagedResource.getDescription() != null)
				format = damagedResource.getDescription() + " (" + damagedResource.getResource() + ")";
			else
				format = "Null description provided";
		}
		
		Log.info(String.format(format, params));
		registerProblem(ProblemLevel.NOTIFICATION, e, damagedResource, format, params);
	}
	
	/**
	 * Report a error.
	 * @param e Throwable linked to this error. May be <code>null</code>.
	 * @param damagedResource {@link DamagedResourceReport} linked to this error. May be <code>null</code>.
	 * @param format description
	 * <a href=http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax>format string</a>.
	 * @param params Format string arguments.
	 */
	public static void reportDebugNotification(Throwable e,
			DamagedResourceReport damagedResource,
			String format,
			Object... params) {
		
		if (Debug.allowDebug) {
			
			if (format == null) {
				if (damagedResource != null && damagedResource.getDescription() != null)
					format = damagedResource.getDescription() + " (" + damagedResource.getResource() + ")";
				else
					format = "Null description provided";
			}
			
			Log.debug(String.format(format, params));
			registerProblem(ProblemLevel.NOTIFICATION, e, damagedResource, format, params);
		} else {
			surpressedDebugProblems++;
		}
	}
	
	public static void reportUncaughtThrowable(Throwable e, Thread t) {
		Log.error("Uncaught Throwable encountered: " + e.toString());
		Log.error("See execution report for details");
		registerProblem(ProblemLevel.UNCAUGHT, e, null, "Uncaught Throwable registered in thread %s",
				t.getName());
	}
	
	public static void reportUncaughtThrowable(Throwable e) {
		reportUncaughtThrowable(e, Thread.currentThread());
	}
	
	public static void reportIllegalAccessException(IllegalAccessException e, Class<?> accessor, Class<?> subject) {
		reportError(e, null, "Could not access %s from %s", 
				subject.toString(), accessor.toString());
	}
	
	
	
	public static void reportFileProblem(File file, boolean critical, String format, Object[] args, String prefix) {
		format = prefix + " " + format;
		
		if (critical) {
			reportCriticalError(null,
					rscUnrch(file.getPath(), format, args),
					null);
		}
		
		reportError(null,
				rscUnrch(file.getPath(), format, args),
				null);
	}
	
	public static void reportReadProtectedFile(File file, boolean critical, String format, Object... args) {
		reportFileProblem(file, critical, format, args, "Denied access to read");
	}
	
	public static void reportWriteProtectedFile(File file, boolean critical, String format, Object... args) {
		reportFileProblem(file, critical, format, args, "Denied access to write");
	}
	
	public static void reportMissingFile(File file, boolean critical, String format, Object... args) {
		reportFileProblem(file, critical, format, args, "Failed to locate");
	}
	
	
	
	public static synchronized void addDebugValueSection(DebugValueSection section) {
		getValueSections().add(section);
		maxDVNameLength = Math.max(maxDVNameLength, section.getMaxNameLength());
	}
	
	
	
	public static synchronized void addSuggestionProvider(SuggestionProvider provider) {
		getSuggestionProviders().add(provider);
	}
	
	
	public static synchronized void addExtraReportPart(ReportPart part) {
		getExtraReportParts().add(part);
	}
	

	public static void enableReportAtShutdown() {
		if (shutdownHookAdded) return;
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (getProblems().isEmpty() && !isReportForced()) {
					return;
				}
				
				print();
			}
		}));
		
		shutdownHookAdded = true;
	}

	public static boolean isReportForced() {
		return forceReport;
	}
	
	public static void setForcedReport(boolean forced) {
		forceReport = forced;
	}
	
	/**
	 * @return the backend
	 */
	public static ExecutionReportBackend getBackend() {
		return backend;
	}

	/**
	 * @param backend the backend to set
	 */
	public static void setBackend(ExecutionReportBackend backend) {
		ExecutionReport.backend = backend;
	}

	public static void enableUnhandledThrowableHandling() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				ExecutionReport.reportUncaughtThrowable(e, t);
			}
			
		});
	}
	
	public static void addDefaults() {
		addDebugValueSection(new ExecutionReportProblemAnalysisSection());
	}
	
	public static void log(ProblemLevel level, String text) {
		switch (level) {
		case CRITICAL_ERROR:
			Log.critical(text);
			break;
		case DEBUG:
			Log.debug(text);
			break;
		case ERROR:
		case UNCAUGHT:
		default:
			Log.error(text);
			break;
		case NOTIFICATION:
			Log.info(text);
			break;
		case WARNING:
			Log.warn(text);
			break;
		}
	}
	
	public static ProblemReport getProblemReport(int id) {
		synchronized (getProblems()) {
			for (ProblemReport report : getProblems()) {
				if (report.getId() == id) {
					return report;
				}
			}
		}
		
		return null;
	}
	
	public static void print(PrintStream s) {
		s.println();
		s.println(create());
	}
	
	public static void print() {
		Log.report("");
		Log.report(create());
		Log.report("");
	}
	
	public static synchronized String create() {
		synchronized (getProblems()) {
			synchronized (getDamagedResources()) {
				synchronized (getValueSections()) {
					synchronized (getSuggestionProviders()) {
						synchronized (getExtraReportParts()) {
							// Sigh
							return createImpl();
						}
					}
				}
			}
		}
	}
	
	private static String createImpl() {
		StringBuilder b = new StringBuilder();
		
		b.append("\n\n"
				+ "=================================================\n"
				+ "            ||  EXECUTION  REPORT  ||            \n"
				+ "=================================================\n");
		
		appendValues(b);
		b.append("\n=================================================\n");
		appendProblems(b);
		b.append("\n=================================================\n");
		appendResoruces(b);
		b.append("\n=================================================\n");
		appendSuggestions(b);
		
		for (ReportPart part : getExtraReportParts()) {
			b.append("\n=================================================\n");
			part.print(b);
		}
		
		b.append("\n=================================================\n");
		appendEndNote(b);
		
		return b.toString();
	}

	private static void appendEndNote(StringBuilder b) {
		b.append("\nREMINDER ON TROUBLESHOOTING:"
				+ "\n- Fix any damaged resources before fixing errors as errors are often caused by resource damage"
				+ "\n- Fix problems one by one starting from the first one chronologically"
				+ "\n- If using modded or otherwise altered version of application, "
				+     "try recreating the problem with default configuration to better locate problem sources"
				+ "\n- Carefully check application logs for unreported problems and warnings"
				+ "\n- Before submitting this report remove any confidential data that may be included");
		
		b.append("\n\nThis execution report was created with ExecutionReport service from TGE2Utils library.");
		b.append("\nReport size in characters: ");
		b.append(b.length() + (int) (Math.floor(Math.log10(b.length()))) + 1);
	}

	private static void appendSuggestions(StringBuilder b) {
		b.append("\nSUGGESTIONS\n");
		
		if (getSuggestionProviders().isEmpty()) {
			b.append("\n  No help available in this application configuration\n");
			return;
		}
		
		if (!doAppendSuggestions(b)) {
			b.append("\n  No suggestions available\n");
			return;
		}
		
		b.append( "\n  These suggestions are of automatic nature and may not be accurate."
				+ "\n    Always judge their safety, applicability and usefulness before following."
				+ "\n    Follow suggestions in order provided. Backup application and data before making any changes."
				+ "\n    Contact your system administrator or application distributors for further assistance."
				+ "\n");
		
	}

	private static boolean doAppendSuggestions(StringBuilder b) {
		boolean provided = false;
		
		for (SuggestionProvider provider : getSuggestionProviders()) {
			List<String> suggestions = provider.createSuggestions();
			
			if (suggestions == null || suggestions.isEmpty()) {
				continue;
			}
			
			provided = true;
			
			b.append("\n  Suggestor ");
			b.append(provider.getName());
			b.append(":");
			
			if (suggestions.size() == 1) {
				b.append("\n  ");
				b.append(suggestions.get(0));
			} else {
				for (int i = 0; i < suggestions.size(); ++i) {
					b.append("\n  ");
					b.append(i);
					b.append(". ");
					b.append(suggestions.get(i));
				}
			}
			
			b.append('\n');
		}
		
		return provided;
	}

	private static void appendResoruces(StringBuilder b) {
		b.append("\nDAMAGED RESOURCES\n");
		
		if (getDamagedResources().isEmpty()) {
			b.append("\n  No damaged resources reported\n");
			return;
		}
		
		b.append('\n');
		b.append(getDamagedResources().size());
		b.append(" resources damaged");
		
		for (DamagedResourceReport report : getDamagedResources()) {
			b.append("\n  R-");
			b.append(report.getId());
			b.append(" linked with P-");
			b.append(report.getProblemId());
			
			b.append("\n  Resource ");
			b.append(report.getResource());
			b.append(" is ");
			b.append(report.getIssue().toString());
			
			if (report.getDescription() != null) {
				b.append(": ");
				b.append(report.getDescription());
			}
			
			b.append('\n');
		}
	}

	private static void appendValues(StringBuilder b) {
		for (DebugValueSection section : getValueSections()) {
			b.append('\n');
			
			b.append(section.getName());
			b.append("\n\n");
			
			Object[] values = section.getValues();
			String[] names = section.getNames();
			for (int i = 0; i < names.length; ++i) {
				b.append("  ");
				
				for (int j = 0; j < maxDVNameLength - names[i].length(); ++j) {
					b.append(' ');
				}
				
				b.append(names[i]);
				b.append(":   ");
				b.append(values[i]);
				b.append('\n');
			}
			
		}
	}
	
	private static void appendProblems(StringBuilder b) {
		b.append("\nPROBLEM REPORTS\n");
		
		if (getProblems().isEmpty()) {
			b.append("\n  No problems reported, check logs for unreported throwables\n");
			return;
		}
		
		PrintStream ps = new PrintStream(new StringBuilderOutputStream(b));
		
		b.append("\nReported problem IDs:");
		for (ProblemReport report : getProblems()) {
			b.append(' ');
			b.append(report.getProblemId());
		}
		
		b.append('\n');
		
		for (ProblemReport report : getProblems()) {
			b.append("\n  ");
			b.append(report.getType().toString());
			b.append(" P-");
			b.append(report.getId());
			
			if (report.getDamagedResourceId() != -1) {
				b.append(" linked with R-");
				b.append(report.getDamagedResourceId());
			}
			
			b.append("\n  at ");
			b.append(report.getDate().toString());
			b.append("\n  Problem ID: ");
			b.append(report.getProblemId());
			b.append("\n    ");
			b.append(report.getDescription());
			
			if (report.getThrowable() == null) {
				b.append("\n    No throwable linked");
			} else {
				b.append("\n    Throwable " + report.getThrowable().getClass() + ", including stacktrace:\n"
						+ "-------------------------------------------------\n");
				report.getThrowable().printStackTrace(ps);
				b.append("-------------------------------------------------");
			}
			
			b.append("\n");
		}
		
	}
}
