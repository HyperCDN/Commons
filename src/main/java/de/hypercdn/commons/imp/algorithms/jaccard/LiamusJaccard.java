package de.hypercdn.commons.imp.algorithms.jaccard;

import de.hypercdn.commons.api.algorithms.StringSimilarity;
import de.hypercdn.commons.util.BitArray64;
import de.hypercdn.commons.util.StringUtil;

/**
 * LiamusJaccard algorithm to calculare string similarity
 */
public class LiamusJaccard implements StringSimilarity{

	public static final LiamusJaccard instance = new LiamusJaccard();
	public static int JACCARD_ARRAY_64WORDS = 16;

	@Override
	public StringSimilarity getInstance(){
		return instance;
	}

	@Override
	public float coefficient(String a, String b, int sliceSize){
		if(sliceSize < 1){
			throw new IllegalArgumentException("Illegal slice size");
		}
		var stringABits = StringUtil.hash(a, sliceSize, JACCARD_ARRAY_64WORDS);
		var stringBBits = StringUtil.hash(b, sliceSize, JACCARD_ARRAY_64WORDS);
		return coefficient(stringABits, stringBBits);
	}

	public float coefficient(BitArray64 a, BitArray64 b){
		return a.popCntJaccard(b);
	}

}
