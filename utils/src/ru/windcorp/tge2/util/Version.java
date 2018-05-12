package ru.windcorp.tge2.util;

public class Version implements Comparable<Version> {
	
	private final int[] subVersions;
	
	public Version(int... subVersions) {
		if (subVersions == null ||
				subVersions.length < 1) {
			throw new IllegalArgumentException();
		}
		
		this.subVersions = subVersions;
	}

	public int[] getSubVersions() {
		return subVersions;
	}
	
	public int getSubVersion(int depth) {
		return getSubVersions().length > depth ? getSubVersions()[depth] : 0;
	}
	
	public int getMajor() {
		return getSubVersion(0);
	}
	
	public int getMinor() {
		return getSubVersion(1);
	}
	
	public int getBuild() {
		return getSubVersion(Math.max(getSubVersions().length - 1, 2));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(Integer.toString(getMajor()));
		for (int i = 1; i < getSubVersions().length; ++i) {
			sb.append('.');
			sb.append(getSubVersions()[i]);
		}
		
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 0;
		for (int i = 0; i < subVersions.length; ++i) result += subVersions[i] * prime;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return compareTo((Version) obj) == 0;
	}

	@Override
	public int compareTo(Version arg0) {
		int diff;
		for (int i = 0; i < Math.max(getSubVersions().length, arg0.getSubVersions().length); ++i) {
			diff = getSubVersion(i) - arg0.getSubVersion(i); 
			if (diff != 0) {
				return diff;
			}
		}
		
		return 0;
	}

}
