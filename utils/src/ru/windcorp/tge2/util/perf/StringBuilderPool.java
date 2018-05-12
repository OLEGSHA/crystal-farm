package ru.windcorp.tge2.util.perf;

public class StringBuilderPool extends Pool<StringBuilder> {

	public StringBuilderPool(int initCapacity, int maxCapacity) {
		super(initCapacity, maxCapacity, new PoolHelper<StringBuilder>() {

			@Override
			public StringBuilder newInstance() {
				return new StringBuilder();
			}

			@Override
			public void reset(StringBuilder obj) {
				obj.setLength(0);
			}
			
		});
	}
	
	public StringBuilderPool() {
		this(1, 10);
	}
	
	public String getAndRelease(StringBuilder sb) {
		String result = sb.toString();
		release(sb);
		return result;
	}

}
