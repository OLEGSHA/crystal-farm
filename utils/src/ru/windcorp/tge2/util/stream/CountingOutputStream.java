package ru.windcorp.tge2.util.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

/**
 * An {@link InputStream} that counts bytes written through it.
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
 * After popping, {@link #getCounter()} will correctly reflect the bytes written.
 * <p>
 * Position stack in {@link CountingOutputStream} is analogous to position stack
 * in {@link CountingInputStream}.
 * <p>
 * <b>Position stack usage example</b><br />
 * To get the amount of bytes written in {@code foo()}, use the following:
 * <pre>
 * {@code
 * stream.pushCounter();
 * foo(stream);
 * long written = stream.popCounter();
 * }</pre>
 * This operation can be nested arbitrarily.
 * 
 * @author OLEGSHA
 * @see {@link CountingInputStream}
 */
public class CountingOutputStream extends OutputStream {
	
	private final OutputStream parent;

	private Stack<Long> positions = new Stack<>();
	
	private long position;
	private long counter;

	/**
	 * Creates a new CountingOutputStream backed by the given stream.
	 * @param parent the stream to write the data into
	 */
	public CountingOutputStream(OutputStream parent) {
		this.parent = parent;
	}
	
	/**
	 * Gets the current counter value. It is the amount of bytes written through
	 * this CounterOutputStream since the last {@link #pushCounter()} that
	 * has not been {@linkplain #popCounter() popped} yet.
	 * @return bytes written
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
	 * @see {@link CountingOutputStream}
	 */
	public void pushCounter() {
		positions.push(position);
		position = counter;
	}
	
	/**
	 * Pops the position stack.
	 * @throws IllegalStateException if no positions have been pushed.
	 * @return bytes written since the popped position has been pushed.
	 * @see {@link CountingOutputStream}
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
	 * @return the OutputStream that receives the data written to this stream
	 */
	public OutputStream getParent() {
		return parent;
	}

	@Override
	public void write(int arg0) throws IOException {
		getParent().write(arg0);
		counter++;
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		getParent().write(b, off, len);
		counter += len;
	}
	
	@Override
	public void flush() throws IOException {
		getParent().flush();
	}
	
	@Override
	public void close() throws IOException {
		getParent().close();
	}

}
