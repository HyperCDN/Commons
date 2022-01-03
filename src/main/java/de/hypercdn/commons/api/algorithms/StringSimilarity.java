package de.hypercdn.commons.api.algorithms;

/**
 * Calculates a similarity coefficient for two strings
 */
public interface StringSimilarity{

	/**
	 * Calculates a similarity coefficient for two strings
	 *
	 * @param algorithm which should be used
	 * @param a         string
	 * @param b         string
	 * @param sliceSize to compare against each other. Using 2 seems like a reasonable value
	 *
	 * @return similarity coefficient
	 */
	static float coefficient(StringSimilarity algorithm, String a, String b, int sliceSize){
		return algorithm.getInstance().coefficient(a, b, sliceSize);
	}

	/**
	 * Calculates a similarity coefficient for two strings though the average of multiple slices
	 *
	 * @param algorithm  which should be used
	 * @param a          string
	 * @param b          string
	 * @param sliceSizes to compare against each other
	 *
	 * @return similarity coefficient
	 */
	static float multiCoefficient(StringSimilarity algorithm, String a, String b, int... sliceSizes){
		var sum = 0F;
		for(int i : sliceSizes){
			sum += coefficient(algorithm, a, b, i);
		}
		return sum / sliceSizes.length;
	}

	/**
	 * Returns an instance of an algorithm implementation
	 *
	 * @return instance
	 */
	StringSimilarity getInstance();

	/**
	 * Calculates a similarity coefficient for two strings
	 *
	 * @param a         string
	 * @param b         string
	 * @param sliceSize to compare against each other. Using 2 seems like a reasonable value
	 *
	 * @return similarity coefficient
	 */
	float coefficient(String a, String b, int sliceSize);

}
