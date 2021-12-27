package de.hypercdn.commons.imp.checksum;

import java.util.zip.Checksum;

public class CRC64 implements Checksum{

	private static final long[] LOOKUP;

	static{
		LOOKUP = new long[256];
		for(int i = 0; i < 256; i++){
			long v = i;
			for(int j = 0; j < 8; j++){
				if((v & 1) == 1){
					v = (v >>> 1) ^ 0xd800000000000000L;
				}
				else{
					v = (v >>> 1);
				}
			}
			LOOKUP[i] = v;
		}
	}

	long sum = 1;

	@Override
	public void update(int b){
		sum = (sum >>> 8) ^ LOOKUP[((int) sum ^ b) & 0xff];
	}

	public void update(byte... bytes){
		for(var b : bytes){
			update(b);
		}
	}

	@Override
	public void update(byte[] b, int off, int len){
		for(int i = off; i < (off + len); i++){
			update(b[i]);
		}
	}

	@Override
	public long getValue(){
		return sum;
	}

	@Override
	public void reset(){
		sum = 1;
	}

}
