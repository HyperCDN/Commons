package de.hypercdn.commons.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil{

	private StringUtil(){}

	public static List<String> slice(String string, int sliceSize){
		var slices = new ArrayList<String>();
		for(int i = 0; i < string.length() - (sliceSize - 1); i++){
			slices.add(string.substring(i, i + sliceSize));
		}
		return slices;
	}

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
