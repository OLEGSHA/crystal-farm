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
		
		result[2] = Character.forDigit((x >>>= 4) & 0xF, 0x10);
		result[3] = Character.forDigit((x >>>= 4) & 0xF, 0x10);
		
		return result;
	}
	
	public static char[] toFullHex(short x) {
		char[] result = new char[] { '0', 'x', 0, 0, 0, 0 };
		
		result[2] = Character.forDigit((x >>>= 4) & 0xF, 0x10);
		result[3] = Character.forDigit((x >>>= 4) & 0xF, 0x10);
		result[4] = Character.forDigit((x >>>= 4) & 0xF, 0x10);
		result[5] = Character.forDigit((x >>>= 4) & 0xF, 0x10);
		
		return result;
	}
	
	public static char[] toFullHex(int x) {
		char[] result = new char[] { '0', 'x', 0, 0, 0, 0,  0, 0, 0, 0 };
		
		result[2] = Character.forDigit((x >>>= 4) & 0xF, 0x10);
		result[3] = Character.forDigit((x >>>= 4) & 0xF, 0x10);
		result[4] = Character.forDigit((x >>>= 4) & 0xF, 0x10);
		result[5] = Character.forDigit((x >>>= 4) & 0xF, 0x10);

		result[6] = Character.forDigit((x >>>= 4) & 0xF, 0x10);
		result[7] = Character.forDigit((x >>>= 4) & 0xF, 0x10);
		result[8] = Character.forDigit((x >>>= 4) & 0xF, 0x10);
		result[9] = Character.forDigit((x >>>= 4) & 0xF, 0x10);
		
		return result;
	}
	
	public static char[] toFullHex(long x) {
		char[] result = new char[] { '0', 'x', 0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0 };
		
		result[ 2] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		result[ 3] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		result[ 4] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		result[ 5] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);

		result[ 6] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		result[ 7] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		result[ 8] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		result[ 9] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		
		result[10] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		result[11] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		result[12] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		result[13] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);

		result[14] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		result[15] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		result[16] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		result[17] = Character.forDigit((int) (x >>>= 4) & 0xF, 0x10);
		
		return result;
	}

}
