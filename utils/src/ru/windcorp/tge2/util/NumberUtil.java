package ru.windcorp.tge2.util;

public class NumberUtil {
	
	public static String toUnsignedHexString(byte b) {
		int unsigned = b;
		if (b < 0) {
			unsigned += 0x100;
		}
		
		char[] chars = new char[2];
		
		chars[0] = Character.forDigit(unsigned >>> 4, 0x10);
		chars[1] = Character.forDigit(unsigned & 0x0F, 0x10);
		
		return new String(chars);
	}
	
	public static String toUnsignedHexString(byte[] bytes, String separator, int size) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < bytes.length; ++i) {
			sb.append(NumberUtil.toUnsignedHexString(bytes[i]));
			if (i < bytes.length - 1 && ((i + 1) % size == 0)) {
				sb.append(separator);
			}
		}
		
		return sb.toString();
	}
	
	public static String toUnsignedHexString(byte[] bytes) {
		return toUnsignedHexString(bytes, ", ", 1);
	}

}
