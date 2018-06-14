package ru.windcorp.tge2.util.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

/**
 * An {@link InputStream} that counts bytes read through it.
 * Must be backed by another stream.
 * <p>
 * In order to accommodate nested counting, position stack is used.
 * The current counter value is determined by {@code counter - position},
 * where {@code counter} is a global counter and {@code position} is the
 * top of a <i>position stack</i>. By default {@code position} is zero.
 * {@link #pushCounter()} can be used to push current {@code counter}
 * value into the stack effectively resetting {@link #getCounter()}
 * to zero. {@link #popCounter()} can later be used to restore previous
 * {@code position} and, thus, continue using the previous {@link #getCounter()}.
 * After popping, {@link #getCounter()} will correctly reflect the bytes read.
 * <p>
 * Position stack in {@link CountingInputStream} is analogous to position stack
 * in {@link CountingOutputStream}.
 * <p>
 * <b>Position stack usage example</b><br />
 * To get the amount of bytes read in {@code foo()}, use the following:
 * <pre>
 * {@code
 * stream.pushCounter();
 * foo(stream);
 * long read = stream.popCounter();
 * }</pre>
 * This operation can be nested arbitrarily.
 * 
 * @author OLEGSHA
 * @see {@link CountingOutputStream}
 */
public class CountingInputStream extends InputStream {
	
	private final InputStream parent;
	
	private Stack<Long> positions = new Stack<>();
	
	private long position = 0;
	private long counter = 0;
	
	/**
	 * Creates a new CountingInputStream backed by the given stream.
	 * @param parent the stream to get the data from
	 */
	public CountingInputStream(InputStream parent) {
		this.parent = parent;
	}
	
	/**
	 * Gets the current counter value. It is the amount of bytes read through
	 * this CounterInputStream since the last {@link #pushCounter()} that
	 * has not been {@linkplain #popCounter() popped} yet.
	 * @return bytes read
	 */
	public long getCounter() {
		return counter - position;
	}
	
	/**
	 * Resets this CounterInputStream, zeroing {@code counter}, {@code position}
	 * <i>and</i> position stack.
	 */
	public void resetCounter() {
		counter = 0;
		position = 0;
		positions.clear();
	}
	
	/**
	 * Pushes the position stack.
	 * @see {@link CountingInputStream}
	 */
	public void pushCounter() {
		positions.push(position);
		position = counter;
	}
	
	/**
	 * Pops the position stack.
	 * @throws IllegalStateException if no positions have been pushed.
	 * @return bytes read since the popped position has been pushed.
	 * @see {@link CountingInputStream}
	 */
	public long popCounter() {
		if (positions.isEmpty()) {
			throw new IllegalStateException("Stack empty, nothing to pop");
		}
		
		long result = getCounter();
		position = positions.pop();
		return result;
	}
	
	/**
	 * Returns the backing stream
	 * @return the InputStream that supplies data for this stream
	 */
	public InputStream getParent() {
		return parent;
	}

	@Override
	public int read() throws IOException {
		int result = getParent().read();
		counter++;
		return result;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int result = getParent().read(b, off, len);
		counter += result;
		return result;
	}
	
	@Override
	public void close() throws IOException {
		getParent().close();
	}
	
	@Override
	public boolean markSupported() {
		return getParent().markSupported();
	}
	
	@Override
	public synchronized void mark(int readlimit) {
		getParent().mark(readlimit);
	}
	
	@Override
	public synchronized void reset() throws IOException {
		getParent().reset();
	}
	
	@Override
	public long skip(long n) throws IOException {
		long result = super.skip(n);
		counter += result;
		return result;
	}
	
	@Override
	public int available() throws IOException {
		return getParent().available();
	}

}
