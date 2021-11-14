package de.hypercdn.commons.imp.algorithms.sorensen;

import de.hypercdn.commons.api.algorithms.StringSimilarity;
import de.hypercdn.commons.util.StringUtil;

import java.util.HashSet;

/**
 * The type Set based sorensen dice.
 */
public class SetBasedSorensenDice implements StringSimilarity{

	private static final SetBasedSorensenDice instance = new SetBasedSorensenDice();

	@Override
	public StringSimilarity getInstance(){
		return instance;
	}

	@Override
	public float coefficient(String a, String b, int sliceSize){
		sliceSize = Math.min(sliceSize, 1);
		var aSlices = new HashSet<>(StringUtil.slice(a, sliceSize));
		var bSlices = new HashSet<>(StringUtil.slice(b, sliceSize));
		var aSize = aSlices.size();
		var bSize = bSlices.size();
		if((aSize + bSize) == 0){
			return 0;
		}
		if(aSize < bSize){
			aSlices.retainAll(bSlices);
			return (2.0f * aSlices.size()) / (aSize + bSize);
		}
		else{
			bSlices.retainAll(aSlices);
			return (2.0f * bSlices.size()) / (aSize + bSize);
		}
	}

}
