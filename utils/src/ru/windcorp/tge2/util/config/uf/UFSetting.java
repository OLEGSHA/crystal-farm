package ru.windcorp.tge2.util.config.uf;

import ru.windcorp.tge2.util.Nameable;
import ru.windcorp.tge2.util.config.*;

public class UFSetting<T> extends Config implements ISetting<T> {
	
	public static final String DESCRIPTION_SETTING_NAME = UFConfig.DESCRIPTION_SETTING_NAME;
	
	private final Setting<T> implementation;
	private final Setting<String> description;

	public UFSetting(String name, Class<T> type, T defaultValue, String description) {
		super(name);
		
		add(this.implementation = new Setting<T>(name, type, defaultValue));
		add(this.description = new Setting<String>(DESCRIPTION_SETTING_NAME, String.class, description));
	}

	@Override
	public String getName() {
		return implementation.getName();
	}
	
	public String getDescription() {
		return description.getValue();
	}

	@Override
	public String toString() {
		return implementation.toString();
	}

	@Override
	public Class<T> getContentType() {
		return implementation.getContentType();
	}

	@Override
	public T getValueOr(T def) {
		return implementation.getValueOr(def);
	}

	@Override
	public int compareTo(Nameable arg0) {
		return implementation.compareTo(arg0);
	}

	@Override
	public T getValue() {
		return implementation.getValue();
	}

	@Override
	public void setValue(T value) {
		implementation.setValue(value);
	}

	@Override
	public T getDefaultValue() {
		return implementation.getDefaultValue();
	}

	@Override
	public void setDefaultValue(T defaultValue) {
		implementation.setDefaultValue(defaultValue);
	}

	public void resetValue() {
		implementation.resetValue();
	}

	@Override
	public String getLastProblem() {
		return implementation.getLastProblem();
	}

}
