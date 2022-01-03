package de.hypercdn.commons.util;

import java.util.Arrays;

public class BitArray64{

	private final long[] words;

	public BitArray64(int bits64){
		this.words = new long[bits64];
	}

	public BitArray64(BitArray64 other){
		this.words = new long[other.words.length];
		System.arraycopy(other.words, 0, words, 0, words.length);
	}

	public void clear(){
		Arrays.fill(words, 0);
	}

	public void setBit(int bitIdx){
		int wordIdx = bitIdx >>> 6;
		int bitShift = bitIdx & 0x3F;
		words[wordIdx] |= (1L << bitShift);
	}

	public int popCntUnion(BitArray64 other){
		int bits = 0;
		for(int i = 0; i < words.length; i++){
			bits += Long.bitCount(words[i] | other.words[i]);
		}
		return bits;
	}

	public int popCntIntersect(BitArray64 other){
		int bits = 0;
		for(int i = 0; i < words.length; i++){
			bits += Long.bitCount(words[i] & other.words[i]);
		}
		return bits;
	}

	public float popCntJaccard(BitArray64 other){
		int bitsU = 0;
		int bitsI = 0;
		for(int i = 0; i < words.length; i++){
			long wordA = words[i];
			long wordB = other.words[i];
			bitsU += Long.bitCount(wordA | wordB);
			bitsI += Long.bitCount(wordA & wordB);
		}
		return (float) bitsI / (float) bitsU;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(long word : words){
			String binStr = Long.toBinaryString(word);
			binStr = "0".repeat(64 - binStr.length()) + binStr;
			sb.append(binStr);
		}
		return sb.toString();
	}

}
