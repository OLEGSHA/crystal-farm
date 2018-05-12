package ru.windcorp.tge2.fileio.data;

import java.util.Arrays;

import ru.windcorp.tge2.util.NumberUtil;
import ru.windcorp.tge2.util.bytes.ByteIOUtil;
import ru.windcorp.tge2.util.exceptions.UnexpectedException;

public class BDElement implements Cloneable {
	
	private final int name;
	private byte[] value;
	
	public BDElement(int name, byte[] value) {
		this.name = name;
		this.value = value;
	}
	
	public int getName() {
		return name;
	}

	public byte[] getValue() {
		return value;
	}
	
	public int getValueLength() {
		return getValue().length;
	}
	
	public void setValue(byte[] value) {
		this.value = value;
	}
	
	public byte[] toBytes() {
		byte[] data = new byte[getValueLength() + (ByteIOUtil.BYTES_INT * 2)];
		ByteIOUtil.writeInt(data, 0, getName());
		ByteIOUtil.writeInt(data, ByteIOUtil.BYTES_INT, getValueLength());
		System.arraycopy(getValue(), 0, data, ByteIOUtil.BYTES_INT * 2, getValueLength());
		return data;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + name;
		result = prime * result + Arrays.hashCode(value);
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
		BDElement other = (BDElement) obj;
		if (name != other.name)
			return false;
		if (!Arrays.equals(value, other.value))
			return false;
		return true;
	}

	@Override
	protected BDElement clone() {
		try {
			BDElement clone = (BDElement) super.clone();
			clone.value = new byte[value.length];
			System.arraycopy(value, 0, clone.value, 0, value.length);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public String toString() {
		return "name=" + getName()
				+ ";value.length=" + getValueLength()
				+ ";value=" + NumberUtil.toUnsignedHexString(getValue());
	}

}
