package de.hypercdn.commons.imp.files;

import de.hypercdn.commons.api.files.BlockFile;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * The type Generic block file.
 */
public class GenericBlockFile implements BlockFile{

	/**
	 * The constant DEFAULT_BLOCK_SIZE.
	 */
	public static final int DEFAULT_BLOCK_SIZE = 16;
	/**
	 * The constant FILE_INDICATOR.
	 */
	public static final String FILE_INDICATOR = "GBF";
	/**
	 * The constant RESERVED_HEADER_BLOCKS.
	 */
	public static final int RESERVED_HEADER_BLOCKS = 16;

	private final int blockByteSize;
	private final int headerSizeBytes;
	private final FileChannel fileChannel;

	private GenericBlockFile(int blockByteSize, FileChannel fileChannel){
		this.blockByteSize = blockByteSize;
		this.fileChannel = fileChannel;
		// File Indicator + Block Size Indicator + Reserved Header Blocks = Header Size
		this.headerSizeBytes = FILE_INDICATOR.getBytes(StandardCharsets.UTF_8).length + Integer.BYTES + RESERVED_HEADER_BLOCKS * blockByteSize;
	}

	/**
	 * With file block file.
	 *
	 * @param file the file
	 *
	 * @return the block file
	 *
	 * @throws IOException the io exception
	 */
	public static BlockFile withFile(File file) throws IOException{
		return withFile(file, DEFAULT_BLOCK_SIZE);
	}

	/**
	 * With file block file.
	 *
	 * @param file          the file
	 * @param blockByteSize the block byte size
	 *
	 * @return the block file
	 *
	 * @throws IOException the io exception
	 */
	public static BlockFile withFile(File file, int blockByteSize) throws IOException{
		var fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
		if(fileChannel.size() > 0){
			var fileId = ByteBuffer.allocate(FILE_INDICATOR.getBytes(StandardCharsets.UTF_8).length);
			fileChannel.read(fileId, 0);
			if(!FILE_INDICATOR.equalsIgnoreCase(new String(fileId.flip().array()))){
				throw new IOException("Not a GBF file");
			}
			var blockBS = ByteBuffer.allocate(Integer.BYTES);
			fileChannel.read(blockBS, FILE_INDICATOR.getBytes(StandardCharsets.UTF_8).length);
			return new GenericBlockFile(blockBS.flip().getInt(), fileChannel);
		}
		else{
			// write file id
			var fileId = ByteBuffer.allocate(FILE_INDICATOR.getBytes(StandardCharsets.UTF_8).length).put(FILE_INDICATOR.getBytes(StandardCharsets.UTF_8)).flip();
			fileChannel.write(fileId, 0);
			// write block size
			var blockBS = ByteBuffer.allocate(Integer.BYTES).putInt(blockByteSize).flip();
			fileChannel.write(blockBS, FILE_INDICATOR.getBytes(StandardCharsets.UTF_8).length);
			// claim header area
			var headerAR = ByteBuffer.allocate(Byte.BYTES * blockByteSize * RESERVED_HEADER_BLOCKS);
			Arrays.fill(headerAR.array(), (byte) 0xff);
			fileChannel.write(headerAR, FILE_INDICATOR.getBytes(StandardCharsets.UTF_8).length + Integer.BYTES);
		}
		return null;
	}

	@Override
	public int getBlockByteSize(){
		return blockByteSize;
	}

	@Override
	public byte[] read(long pos) throws IOException{
		return read(pos, headerSizeBytes);
	}

	private byte[] read(long pos, long offset) throws IOException{
		if(pos < 0){
			throw new IllegalArgumentException("Position cant be negative");
		}
		var read = 0;
		var byteBuffer = ByteBuffer.allocate(Byte.BYTES * blockByteSize);
		while(read < blockByteSize * Byte.BYTES){
			int rc = fileChannel.read(byteBuffer, offset * Byte.BYTES + blockByteSize * pos * Byte.BYTES + read * Byte.BYTES);
			if(rc == -1){
				break;
			}
			read += rc;
		}
		return byteBuffer.array();
	}

	@Override
	public boolean write(long pos, byte[] data) throws IOException{
		return write(pos, headerSizeBytes, data);
	}

	private boolean write(long pos, long offset, byte[] data) throws IOException{
		if(pos < 0){
			throw new IllegalArgumentException("Position cant be negative");
		}
		var written = 0;
		var byteBuffer = ByteBuffer.wrap(data);
		while(written < data.length){
			written += fileChannel.write(byteBuffer, offset * Byte.BYTES + blockByteSize * pos * Byte.BYTES + written * Byte.BYTES);
		}
		return written == data.length;
	}

	@Override
	public void close() throws Exception{
		fileChannel.close();
	}

}
