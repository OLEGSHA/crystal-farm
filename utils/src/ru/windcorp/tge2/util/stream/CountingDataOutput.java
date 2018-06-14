package ru.windcorp.tge2.util.stream;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CountingDataOutput implements DataOutput {

	private final CountingOutputStream stream;
	private final DataOutputStream data;
	
	public CountingDataOutput(CountingOutputStream stream) {
		this.stream = stream;
		this.data = new DataOutputStream(stream);
	}
	
	public CountingDataOutput(OutputStream stream) {
		this(new CountingOutputStream(stream));
	}

	protected CountingOutputStream getStream() {
		return stream;
	}
	
	protected DataOutputStream getData() {
		return data;
	}

	public long getCounter() {
		return stream.getCounter();
	}

	public void resetCounter() {
		stream.resetCounter();
	}

	public void pushCounter() {
		stream.pushCounter();
	}

	public long popCounter() {
		return stream.popCounter();
	}

	public void close() throws IOException {
		data.close();
	}

	public void flush() throws IOException {
		data.flush();
	}

	public void write(byte[] arg0, int arg1, int arg2) throws IOException {
		data.write(arg0, arg1, arg2);
	}

	public void write(byte[] arg0) throws IOException {
		data.write(arg0);
	}

	public void write(int arg0) throws IOException {
		data.write(arg0);
	}

	public final void writeBoolean(boolean arg0) throws IOException {
		data.writeBoolean(arg0);
	}

	public final void writeByte(int arg0) throws IOException {
		data.writeByte(arg0);
	}

	public final void writeBytes(String arg0) throws IOException {
		data.writeBytes(arg0);
	}

	public final void writeChar(int arg0) throws IOException {
		data.writeChar(arg0);
	}

	public final void writeChars(String arg0) throws IOException {
		data.writeChars(arg0);
	}

	public final void writeDouble(double arg0) throws IOException {
		data.writeDouble(arg0);
	}

	public final void writeFloat(float arg0) throws IOException {
		data.writeFloat(arg0);
	}

	public final void writeInt(int arg0) throws IOException {
		data.writeInt(arg0);
	}

	public final void writeLong(long arg0) throws IOException {
		data.writeLong(arg0);
	}

	public final void writeShort(int arg0) throws IOException {
		data.writeShort(arg0);
	}

	public final void writeUTF(String arg0) throws IOException {
		data.writeUTF(arg0);
	}

}
