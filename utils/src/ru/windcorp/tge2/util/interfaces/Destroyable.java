package ru.windcorp.tge2.util.interfaces;

public interface Destroyable {
	
	/**
	 * Releases all the resources owned and invalidates the object.
	 * The object is supposed to let all of its previously owned resources
	 * be collected by the garbage collector after this method has completed.
	 * Implementations must forward this call to all owned
	 * {@link Destroyable Destroyables}, if present.
	 * <p>
	 * The behavior of the object is no longer defined after this method has been called.
	 * The subsequent invocations of any of this object's methods are likely going to result
	 * in Runtime exceptions.
	 * The subsequent invocations of <code>destroy()</code> are <i>not</i> safe.
	 * <p>
	 * <i>Implementation note</i>: a call to <code>super.destroy()</code>
	 * is generally required.
	 * */
	public void destroy();

}
