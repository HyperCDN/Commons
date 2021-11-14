package de.hypercdn.commons.util;

import java.util.ArrayList;
import java.util.List;

/**
 * The type String util.
 */
public class StringUtil{

	private StringUtil(){}

	/**
	 * Slice list.
	 *
	 * @param string    the string
	 * @param sliceSize the slice size
	 *
	 * @return the list
	 */
	public static List<String> slice(String string, int sliceSize){
		var slices = new ArrayList<String>();
		for(int i = 0; i < string.length() - (sliceSize - 1); i++){
			slices.add(string.substring(i, i + sliceSize));
		}
		return slices;
	}

	/**
	 * Hash bit array 64.
	 *
	 * @param string    the string
	 * @param nGramSize the n gram size
	 * @param words     the words
	 *
	 * @return the bit array 64
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
