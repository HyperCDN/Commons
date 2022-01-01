package de.hypercdn.commons.imp.algorithms.jaccard;

import de.hypercdn.commons.api.algorithms.StringSimilarity;
import de.hypercdn.commons.util.StringUtil;

import java.util.HashSet;

/**
 * The type Set based jaccard.
 */
public class SetBasedJaccard implements StringSimilarity{

	public static final SetBasedJaccard instance = new SetBasedJaccard();

	@Override
	public StringSimilarity getInstance(){
		return instance;
	}

	@Override
	public float coefficient(String a, String b, int sliceSize){
		if(a.equals(b)){
			return 1F;
		}
		var setA = new HashSet<>(StringUtil.slice(a, sliceSize));
		var setB = new HashSet<>(StringUtil.slice(b, sliceSize));
		var merge = new HashSet<>(){{
			addAll(setA);
			addAll(setB);
		}};
		int dif = (setA.size() + setB.size()) - merge.size();
		return (dif / (float) merge.size());
	}

}
