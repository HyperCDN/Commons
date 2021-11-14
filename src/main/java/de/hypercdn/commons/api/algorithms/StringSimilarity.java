package de.hypercdn.commons.api.algorithms;

/**
 * The interface String similarity.
 */
public interface StringSimilarity{

	/**
	 * Coefficient float.
	 *
	 * @param algorithm the algorithm
	 * @param a         the a
	 * @param b         the b
	 * @param sliceSize the slice size
	 *
	 * @return the float
	 */
	static float coefficient(StringSimilarity algorithm, String a, String b, int sliceSize){
		return algorithm.getInstance().coefficient(a, b, sliceSize);
	}

	/**
	 * Multi coefficient float.
	 *
	 * @param algorithm  the algorithm
	 * @param a          the a
	 * @param b          the b
	 * @param sliceSizes the slice sizes
	 *
	 * @return the float
	 */
	static float multiCoefficient(StringSimilarity algorithm, String a, String b, int... sliceSizes){
		var sum = 0F;
		for(int i : sliceSizes){
			sum += coefficient(algorithm, a, b, i);
		}
		return sum / sliceSizes.length;
	}

	/**
	 * Gets instance.
	 *
	 * @return the instance
	 */
	StringSimilarity getInstance();

	/**
	 * Coefficient float.
	 *
	 * @param a         the a
	 * @param b         the b
	 * @param sliceSize the slice size
	 *
	 * @return the float
	 */
	float coefficient(String a, String b, int sliceSize);

}
