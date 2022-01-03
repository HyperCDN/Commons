package de.hypercdn.commons.api.properties.compare;

import de.hypercdn.commons.imp.algorithms.jaccard.LiamusJaccard;
import de.hypercdn.commons.util.BitArray64;

/**
 * Indicates that a class provides the functionality to compare string similarities based on the {@link LiamusJaccard} algorithm
 */
public interface StringSimilarity{

	/**
	 * Gets the calculated similarity hash
	 *
	 * @return similarity hash
	 */
	BitArray64 similarityHash();

	/**
	 * Compares similarity to another object
	 *
	 * @param other object
	 *
	 * @return similarity coefficient
	 */
	default float compareSimilarityTo(StringSimilarity other){
		return LiamusJaccard.instance.coefficient(similarityHash(), other.similarityHash());
	}

}
