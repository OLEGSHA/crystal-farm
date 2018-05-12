package ru.windcorp.tge2.util.config;

import java.io.IOException;

import ru.windcorp.tge2.fileio.data.sbd.*;
import ru.windcorp.tge2.util.Nameable;

public class Setting<T> extends Nameable implements ISetting<T> {

	private final Class<T> contentType;
	private SBDValue<T> value;
	private T defaultValue;
	private transient String lastProblem;
	
	public Setting(String name, Class<T> type, T defaultValue) {
		super(name);
		this.contentType = type;
		this.setDefaultValue(defaultValue);
	}

	@Override
	public Class<T> getContentType() {
		return contentType;
	}
	
	protected synchronized T getRawValue() {
		if (value == null)
			throw new IllegalStateException("Setting " + this + " has not yet been linked to a data structure");
			
		return value.getValue();
	}
	
	@Override
	public T getValueOr(T def) {
		T val = getRawValue(); // Local variable for better synchronization
		return val == null ? def : val;
	}
	
	@Override
	public T getValue() {
		return getValueOr(null);
	}

	protected synchronized void setRawValue(T value) {
		if (this.value == null)
			throw new IllegalStateException("Setting " + this + " has not yet been linked to a data structure");
		
		if (check(value))
			throw new IllegalArgumentException("Value " + value + " could not be accepted by setting " + this + ": " +
					getLastProblem() + "");
		
		if (!this.value.accepts(value))
			throw new IllegalArgumentException("SBDValue of type " + this.value.getFullTypeName() +
					"( instance " + this.value + " in setting " + this + ") does not accept value " + value);
		
		this.value.setValue(value);
	}
	
	@Override
	public void setValue(T value) {
		setRawValue(value);
	}
	
	/**
	 * Access to this method and getLastProblem() must be synchronized on this object.
	 */
	protected synchronized boolean check(T value) {
		setLastProblem(accept(value));
		return getLastProblem() != null;
	}
	
	/**
	 * Checks whether the supplied value is appropriate for this setting. The value
	 * may not be acceptable by the SBD element.
	 * @param value The value to check against. May be null.
	 * @return null if value can be accepted or a brief description of the problem otherwise.
	 * Example: "the size cannot be negative"
	 */
	protected String accept(T value) {
		return null;
	}

	@Override
	public synchronized T getDefaultValue() {
		return defaultValue;
	}

	@Override
	public synchronized void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public void resetValue() {
		setValue(getDefaultValue());
	}
	
	@Override
	public String getLastProblem() {
		return lastProblem;
	}

	protected void setLastProblem(String lastProblem) {
		this.lastProblem = lastProblem;
	}

	@Override
	public synchronized void load(SBDStructure structure) throws IOException {
		this.value = structure.getValue(getName(), getContentType());
		
		if (this.value == null) {
			this.value = StructuredByteData.create(getContentType(), getName());
			if (this.value == null) {
				throw new IOException("Could not find SBDValue implementation of type " +
						getContentType() + " for setting " + getName());
			}
			structure.put(this.value);
			resetValue();
		} else if (check(value.getValue())) {
			resetValue();
		}
	}
	
}
