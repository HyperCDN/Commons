package de.hypercdn.commons.imp.files;

import de.hypercdn.commons.api.files.BlockFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class GenericBlockFile implements BlockFile{

	public static final int DEFAULT_BLOCK_SIZE = 16;
	public static final String FILE_INDICATOR = "GBF";
	public static final int RESERVED_HEADER_BLOCKS = 16;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private final int blockByteSize;
	private final int headerSizeBytes;
	private final FileChannel fileChannel;

	protected GenericBlockFile(int blockByteSize, FileChannel fileChannel){
		this.blockByteSize = blockByteSize;
		this.fileChannel = fileChannel;
		// File Indicator + Block Size Indicator + Reserved Header Blocks = Header Size
		this.headerSizeBytes = FILE_INDICATOR.getBytes(StandardCharsets.UTF_8).length + Integer.BYTES + RESERVED_HEADER_BLOCKS * blockByteSize;
	}

	public static BlockFile withFile(File file) throws IOException{
		return withFile(file, DEFAULT_BLOCK_SIZE, null);
	}

	public static BlockFile withFile(File file, int blockByteSize, byte[][] headers) throws IOException{
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
			// write header contents
			if(headers != null){
				for(int i = 0; i < Math.min(headers.length, RESERVED_HEADER_BLOCKS); i++){
					var header = headers[i];
					fileChannel.write(ByteBuffer.wrap(header), FILE_INDICATOR.getBytes(StandardCharsets.UTF_8).length + Integer.BYTES + (long) i * blockByteSize);
				}
			}
			return new GenericBlockFile(blockBS.flip().getInt(), fileChannel);
		}
	}

	@Override
	public int getBlockByteSize(){
		return blockByteSize;
	}

	@Override
	public byte[] readHeader(int pos) throws IOException{
		if(pos < 0){
			throw new IllegalArgumentException("Position cant be negative");
		}
		else if(pos >= RESERVED_HEADER_BLOCKS){
			throw new IllegalArgumentException("Position out of range for header");
		}
		return read(pos, FILE_INDICATOR.getBytes(StandardCharsets.UTF_8).length + Integer.BYTES);
	}

	@Override
	public byte[] read(long pos) throws IOException{
		return read(pos, headerSizeBytes);
	}

	private byte[] read(long pos, long offset) throws IOException{
		if(pos < 0){
			throw new IllegalArgumentException("Position cant be negative");
		}
		logger.debug("Reading data block at position " + pos + " with an offset of " + offset + " bytes from the file channel " + fileChannel);
		var read = 0;
		var byteBuffer = ByteBuffer.allocate(Byte.BYTES * blockByteSize);
		while(read < blockByteSize * Byte.BYTES){
			int rc = fileChannel.read(byteBuffer, offset * Byte.BYTES + blockByteSize * pos * Byte.BYTES + read * Byte.BYTES);
			if(rc == -1){
				break;
			}
			read += rc;
			logger.trace("Reading data block at position " + pos + " with an offset of " + offset + " bytes from the file channel " + fileChannel + " Currently read bytes " + read);
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
		logger.debug("Writing " + data.length + " bytes at position " + pos + " with an offset of " + offset + " to the file channel " + fileChannel);
		var written = 0;
		var byteBuffer = ByteBuffer.wrap(data);
		while(written < data.length){
			written += fileChannel.write(byteBuffer, offset * Byte.BYTES + blockByteSize * pos * Byte.BYTES + written * Byte.BYTES);
			logger.trace("Writing " + data.length + " bytes at position " + pos + " with an offset of " + offset + " to the file channel " + fileChannel + " Currently written bytes " + written);
		}
		return written == data.length;
	}

	@Override
	public void close() throws Exception{
		fileChannel.close();
	}

}