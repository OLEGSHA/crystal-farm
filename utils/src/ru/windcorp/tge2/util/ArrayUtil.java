package ru.windcorp.tge2.util;

import java.math.BigInteger;

public class ArrayUtil {

	public static int firstIndexOf(byte[] array, byte element) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int lastIndexOf(byte[] array, byte element) {
		for (int i = array.length - 1; i >= 0; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int occurences(byte[] array, byte element) {
		int result = 0;
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				++result;
			}
		}
		return result;
	}
	
	public static int firstIndexOf(short[] array, short element) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int lastIndexOf(short[] array, short element) {
		for (int i = array.length - 1; i >= 0; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int occurences(short[] array, short element) {
		int result = 0;
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				++result;
			}
		}
		return result;
	}
	
	public static int firstIndexOf(int[] array, int element) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int lastIndexOf(int[] array, int element) {
		for (int i = array.length - 1; i >= 0; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int occurences(int[] array, int element) {
		int result = 0;
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				++result;
			}
		}
		return result;
	}
	
	public static int firstIndexOf(long[] array, long element) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int lastIndexOf(long[] array, long element) {
		for (int i = array.length - 1; i >= 0; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int occurences(long[] array, long element) {
		int result = 0;
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				++result;
			}
		}
		return result;
	}
	
	public static int firstIndexOf(float[] array, float element) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int lastIndexOf(float[] array, float element) {
		for (int i = array.length - 1; i >= 0; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int occurences(float[] array, float element) {
		int result = 0;
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				++result;
			}
		}
		return result;
	}

	public static int firstIndexOf(double[] array, double element) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int lastIndexOf(double[] array, double element) {
		for (int i = array.length - 1; i >= 0; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int occurences(double[] array, double element) {
		int result = 0;
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				++result;
			}
		}
		return result;
	}
	
	public static int firstIndexOf(boolean[] array, boolean element) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int lastIndexOf(boolean[] array, boolean element) {
		for (int i = array.length - 1; i >= 0; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int occurences(boolean[] array, boolean element) {
		int result = 0;
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				++result;
			}
		}
		return result;
	}
	
	public static int firstIndexOf(Object[] array, Object element) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int lastIndexOf(Object[] array, Object element) {
		for (int i = array.length - 1; i >= 0; ++i) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	public static int occurences(Object[] array, Object element) {
		int result = 0;
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == element) {
				++result;
			}
		}
		return result;
	}
	
	public static int firstIndexOfEqual(Object[] array, Object element) {
		for (int i = 0; i < array.length; ++i) {
			if (equals(array[i], element)) {
				return i;
			}
		}
		return -1;
	}
	
	public static int lastIndexOfEqual(Object[] array, Object element) {
		for (int i = array.length - 1; i >= 0; ++i) {
			if (equals(array[i], element)) {
				return i;
			}
		}
		return -1;
	}
	
	public static int occurencesOfEqual(Object[] array, Object element) {
		int result = 0;
		for (int i = 0; i < array.length; ++i) {
			if (equals(array[i], element)) {
				++result;
			}
		}
		return result;
	}
	
	private static boolean equals(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}
	
	public static long sum(byte[] array, int start, int length) {
		long s = 0;
		length += start;
		for (int i = start; i < length; ++i) {
			s += array[i];
		}
		return s;
	}
	
	public static long sum(short[] array, int start, int length) {
		long s = 0;
		length += start;
		for (int i = start; i < length; ++i) {
			s += array[i];
		}
		return s;
	}
	
	public static long sum(int[] array, int start, int length) {
		long s = 0;
		length += start;
		for (int i = start; i < length; ++i) {
			s += array[i];
		}
		return s;
	}
	
	public static long sum(long[] array, int start, int length) {
		long s = 0;
		length += start;
		for (int i = start; i < length; ++i) {
			s += array[i];
		}
		return s;
	}
	
	public static BigInteger longSum(long[] array, int start, int length) {
		BigInteger s = BigInteger.ZERO;
		length += start;
		for (int i = start; i < length; ++i) {
			s = s.add(BigInteger.valueOf(array[i]));
		}
		return s;
	}
	
	public static float sum(float[] array, int start, int length) {
		float s = 0;
		length += start;
		for (int i = start; i < length; ++i) {
			s += array[i];
		}
		return s;
	}
	
	public static double sum(double[] array, int start, int length) {
		double s = 0;
		length += start;
		for (int i = start; i < length; ++i) {
			s += array[i];
		}
		return s;
	}
	
}
