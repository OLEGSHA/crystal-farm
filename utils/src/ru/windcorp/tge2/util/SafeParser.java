package ru.windcorp.tge2.util;

public class SafeParser {
	
	public static byte toByte(String declar, byte def) {
		try {
			return Byte.parseByte(declar);
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	public static short toShort(String declar, short def) {
		try {
			return Short.parseShort(declar);
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	public static int toInt(String declar, int def) {
		try {
			return Integer.parseInt(declar);
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	public static long toLong(String declar, long def) {
		try {
			return Long.parseLong(declar);
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	public static float toFloat(String declar, float def) {
		try {
			return Float.parseFloat(declar);
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	public static double toDouble(String declar, double def) {
		try {
			return Double.parseDouble(declar);
		} catch (NumberFormatException e) {
			return def;
		}
	}

}
