package ru.windcorp.tge2.util.dataio;

import ru.windcorp.tge2.util.grh.Saveable;

/**
 * Represents an amount of data that can be written.
 * Methods {@link Saveable#save(DataWriter) save(DataWriter)}
 * and {@link ByteBody#getByteLength() getBodyLength()} must be consistent when the object is locked.
 * The object is locked with {@link ByteBody#lockBody() lockBody()} and unlocked with {@link ByteBody#unlockBody() unlockBody()}
 * @author OLEGSHA
 *
 */
public interface ByteBody extends Saveable<DataWriter> {
	
	/**
	 * Calculates the amount of bytes that will be written if {@link Saveable#save(DataWriter) save(DataWriter)} is invoked on a {@link AbstractByteDataWriter}.
	 * @return the body's length is bytes
	 */
	public int getBodyLength();
	
	public void lockBody();
	public void unlockBody();

}
