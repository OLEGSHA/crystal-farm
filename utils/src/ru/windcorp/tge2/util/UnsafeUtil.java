package ru.windcorp.tge2.util;

import java.lang.reflect.Array;

public class UnsafeUtil {
	
	public static <T> T[] newArray(Class<T[]> clazz, int length) {
		return clazz.cast(Array.newInstance(clazz, length));
	}
	
	public static <T> T[][] newArray(Class<T[][]> clazz, int width, int height) {
		return clazz.cast(Array.newInstance(clazz, width, height));
	}
	
	public static <T> T[][][] newArray(Class<T[][][]> clazz, int width, int height, int depth) {
		return clazz.cast(Array.newInstance(clazz, width, height, depth));
	}

}
