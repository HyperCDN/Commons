package de.hypercdn.commons.api.files;

import java.io.IOException;

public interface BlockFile extends AutoCloseable{

	int getBlockByteSize();


	byte[] readHeader(int pos) throws IOException;

	byte[] read(long pos) throws IOException;

	boolean write(long pos, byte[] data) throws IOException;

}
