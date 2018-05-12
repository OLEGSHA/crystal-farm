package ru.windcorp.tge2.util.bytes;

public class ByteArrays {
	
	public static byte[] not(byte[] src) {
		for (int i = 0; i < src.length; ++i) {
			src[i] = (byte) ~src[i];
		}
		return src;
	}
	
	public static byte[] and(byte[] src, byte[] mod) {
		for (int i = 0; i < src.length; ++i) {
			src[i] &= mod[i % mod.length];
		}
		return src;
	}
	
	public static byte[] or(byte[] src, byte[] mod) {
		for (int i = 0; i < src.length; ++i) {
			src[i] |= mod[i % mod.length];
		}
		return src;
	}
	
	public static byte[] xor(byte[] src, byte[] mod) {
		for (int i = 0; i < src.length; ++i) {
			src[i] ^= mod[i % mod.length];
		}
		return src;
	}
	
	public static byte[] nand(byte[] src, byte[] mod) {
		for (int i = 0; i < src.length; ++i) {
			src[i] = (byte) ~(src[i] & mod[i % mod.length]);
		}
		return src;
	}

	public static byte[] nor(byte[] src, byte[] mod) {
		for (int i = 0; i < src.length; ++i) {
			src[i] = (byte) ~(src[i] | mod[i % mod.length]);
		}
		return src;
	}
	
	public static byte[] nxor(byte[] src, byte[] mod) {
		for (int i = 0; i < src.length; ++i) {
			src[i] = (byte) ~(src[i] ^ mod[i % mod.length]);
		}
		return src;
	}
	
	public static byte[] copy(byte[] src) {
		byte[] result = new byte[src.length];
		System.arraycopy(src, 0, result, 0, src.length);
		return result;
	}
	
	public static byte[] join(byte[] b1, byte[] b2) {
		byte[] result = new byte[b1.length + b2.length];
		System.arraycopy(b1, 0, result, 0, b1.length);
		System.arraycopy(b2, 0, result, b1.length, b2.length);
		return result;
	}
	
}
