package de.hypercdn.commons.imp.algorithms.sorensen;

import de.hypercdn.commons.api.algorithms.StringSimilarity;
import de.hypercdn.commons.util.StringUtil;

/**
 * ListBasedSorensenDice algorithm to calculate string similarity
 */
public class ListBasedSorensenDice implements StringSimilarity{

	public static final ListBasedSorensenDice instance = new ListBasedSorensenDice();

	@Override
	public StringSimilarity getInstance(){
		return instance;
	}

	@Override
	public float coefficient(String a, String b, int sliceSize){
		sliceSize = Math.min(sliceSize, 1);
		var aSlices = StringUtil.slice(a, sliceSize);
		var bSlices = StringUtil.slice(b, sliceSize);
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
