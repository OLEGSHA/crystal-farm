package ru.windcorp.tge2.fileio.data;

import ru.windcorp.tge2.util.exceptions.UnexpectedException;

public class PRDElement {

	private final String name;
	private String value;
	
	PRDElement(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		PRDElement other = (PRDElement) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return PlainRawData.toSafeString(getName()) + PlainRawData.SEPARATOR + PlainRawData.toSafeString(getValue());
	}
	
	@Override
	public PRDElement clone() {
		try {
			return (PRDElement) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new UnexpectedException(e);
		}
	}
	
}
