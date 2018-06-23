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
	
	public static char[] toFullHex(byte x) {
		char[] result = new char[] { '0', 'x', 0, 0 };
		
		result[3] = hexDigit((x       ) & 0xF);
		result[2] = hexDigit((x >>>  4) & 0xF);
		
		return result;
	}
	
	public static char[] toFullHex(short x) {
		char[] result = new char[] { '0', 'x', 0, 0, 0, 0 };
		
		result[5] = hexDigit((x       ) & 0xF);
		result[4] = hexDigit((x >>>= 4) & 0xF);
		result[3] = hexDigit((x >>>= 4) & 0xF);
		result[2] = hexDigit((x >>>  4) & 0xF);
		
		return result;
	}
	
	public static char[] toFullHex(int x) {
		char[] result = new char[] { '0', 'x', 0, 0, 0, 0,  0, 0, 0, 0 };
		
		result[9] = hexDigit((x       ) & 0xF);
		result[8] = hexDigit((x >>>= 4) & 0xF);
		result[7] = hexDigit((x >>>= 4) & 0xF);
		result[6] = hexDigit((x >>>= 4) & 0xF);
		
		result[5] = hexDigit((x >>>= 4) & 0xF);
		result[4] = hexDigit((x >>>= 4) & 0xF);
		result[3] = hexDigit((x >>>= 4) & 0xF);
		result[2] = hexDigit((x >>>  4) & 0xF);
		
		return result;
	}
	
	public static char[] toFullHex(long x) {
		char[] result = new char[] { '0', 'x', 0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0 };
		
		result[17] = hexDigit((int) (x       ) & 0xF);
		result[16] = hexDigit((int) (x >>>= 4) & 0xF);
		result[15] = hexDigit((int) (x >>>= 4) & 0xF);
		result[14] = hexDigit((int) (x >>>= 4) & 0xF);

		result[13] = hexDigit((int) (x >>>= 4) & 0xF);
		result[12] = hexDigit((int) (x >>>= 4) & 0xF);
		result[11] = hexDigit((int) (x >>>= 4) & 0xF);
		result[10] = hexDigit((int) (x >>>= 4) & 0xF);
		
		result[ 9] = hexDigit((int) (x >>>= 4) & 0xF);
		result[ 8] = hexDigit((int) (x >>>= 4) & 0xF);
		result[ 7] = hexDigit((int) (x >>>= 4) & 0xF);
		result[ 6] = hexDigit((int) (x >>>= 4) & 0xF);

		result[ 5] = hexDigit((int) (x >>>= 4) & 0xF);
		result[ 4] = hexDigit((int) (x >>>= 4) & 0xF);
		result[ 3] = hexDigit((int) (x >>>= 4) & 0xF);
		result[ 2] = hexDigit((int) (x >>>  4) & 0xF);
		
		return result;
	}
	
	public static char hexDigit(int value) {
		if (value < 0xA) {
			return (char) ('0' + value);
		} else {
			return (char) ('A' - 0xA + value);
		}
		
	}

}
