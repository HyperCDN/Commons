package de.hypercdn.commons.api.files;

import java.io.IOException;

/**
 * The interface Block file.
 */
public interface BlockFile extends AutoCloseable{

	/**
	 * Gets block byte size.
	 *
	 * @return the block byte size
	 */
	int getBlockByteSize();

	/**
	 * Read byte [ ].
	 *
	 * @param pos the pos
	 *
	 * @return the byte [ ]
	 *
	 * @throws IOException the io exception
	 */
	byte[] read(long pos) throws IOException;

	/**
	 * Write boolean.
	 *
	 * @param pos  the pos
	 * @param data the data
	 *
	 * @return the boolean
	 *
	 * @throws IOException the io exception
	 */
	boolean write(long pos, byte[] data) throws IOException;

}
