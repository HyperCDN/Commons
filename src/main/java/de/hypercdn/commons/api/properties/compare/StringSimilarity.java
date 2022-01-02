package de.hypercdn.commons.api.properties.compare;

import de.hypercdn.commons.imp.algorithms.jaccard.LiamusJaccard;
import de.hypercdn.commons.util.BitArray64;

public interface StringSimilarity{

	BitArray64 similarityHash();

	default float compareSimilarityTo(StringSimilarity other){
		return LiamusJaccard.instance.coefficient(similarityHash(), other.similarityHash());
	}

}
