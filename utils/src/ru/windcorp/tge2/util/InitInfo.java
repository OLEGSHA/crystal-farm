package ru.windcorp.tge2.util;

import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ru.windcorp.tge2.util.vectors.Vector3;

public class InitInfo {
	
	private static Date startTime;
	private static final List<Vector3<Exception, String, Date>> ERRORS = new LinkedList<Vector3<Exception, String, Date>>();
	private static final List<URL> DAMAGED_RESOURCES = new LinkedList<URL>();
	private static int initState = 0;
	private static String[] initStates = new String[] { "Initialization Complete" };

	public static Date getStartTime() {
		return startTime;
	}
	
	public static long getElapsedTime() {
		return new Date().getTime() - getStartTime().getTime();
	}

	public static void setStartTime(Date startTime) {
		InitInfo.startTime = startTime;
	}

	public static int getInitState() {
		return initState;
	}

	public static void setInitState(int initState) {
		InitInfo.initState = initState;
	}
	
	public static void nextInitState() {
		++InitInfo.initState;
	}

	public static String[] getInitStates() {
		return initStates;
	}
	
	public static String getInitStateString() {
		return getInitStates()[Math.max(getInitState(), getInitStates().length - 1)];
	}

	public static void setInitStates(String[] initStates) {
		if (initStates == null || initStates.length == 0) {
			throw new IllegalArgumentException();
		}
		
		InitInfo.initStates = initStates;
	}

	public static List<Vector3<Exception, String, Date>> getErrors() {
		return ERRORS;
	}

	public static List<URL> getDamagedResources() {
		return DAMAGED_RESOURCES;
	}
	
	// TODO onError(), onCriticalError(), onDamagedResource(), ...

}
