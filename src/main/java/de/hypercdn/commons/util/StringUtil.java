package de.hypercdn.commons.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class for working with strings
 */
public class StringUtil{

	private StringUtil(){}

	/**
	 * Slice a string with the given slice size
	 *
	 * @param string    to slice
	 * @param sliceSize of slices
	 *
	 * @return sliced string
	 */
	public static List<String> slice(String string, int sliceSize){
		var slices = new ArrayList<String>();
		for(int i = 0; i < string.length() - (sliceSize - 1); i++){
			slices.add(string.substring(i, i + sliceSize));
		}
		return slices;
	}

	/**
	 * Creates a bitarray64 hash of a string
	 *
	 * @param string    to hash
	 * @param nGramSize to slice the string
	 * @param words     length of the hash
	 *
	 * @return hash
	 */
	public static BitArray64 hash(String string, int nGramSize, int words){
		if(string == null){
			return null;
		}
		final int JACCARD_HASHBITS = (words * 64) - 1;
		BitArray64 stringBits = new BitArray64(words);
		for(int i = 0; i < string.length() - nGramSize + 1; i++){
			int hash = 0;
			for(int h = 0; h < nGramSize; h++){
				hash = 31 * hash + string.charAt(i + h);
			}
			hash &= JACCARD_HASHBITS;
			stringBits.setBit(hash);
		}
		return stringBits;
	}

}
