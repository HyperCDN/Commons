package de.hypercdn.commons.api.files;

import java.io.IOException;

/**
 * Represents a file which can be read in blocks of predefined sized
 */
public interface BlockFile extends AutoCloseable{

	/**
	 * Returns the supposed block size in bytes
	 *
	 * @return block size
	 */
	int getBlockByteSize();

	/**
	 * Read the block file headers
	 *
	 * @param pos of the selected block
	 *
	 * @return block content
	 *
	 * @throws IOException on exception
	 */
	byte[] readHeader(int pos) throws IOException;

	/**
	 * Read blocks from the file
	 *
	 * @param pos of the selected block
	 *
	 * @return block content
	 *
	 * @throws IOException on exception
	 */
	byte[] read(long pos) throws IOException;

	/**
	 * Write blocks to the file
	 *
	 * @param pos  of the selected block
	 * @param data content
	 *
	 * @return true on success
	 *
	 * @throws IOException on exception
	 */
	boolean write(long pos, byte[] data) throws IOException;

}
