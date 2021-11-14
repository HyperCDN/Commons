package de.hypercdn.commons.api.algorithms;

public interface StringSimilarity{

	static float coefficient(StringSimilarity algorithm, String a, String b, int sliceSize){
		return algorithm.getInstance().coefficient(a, b, sliceSize);
	}

	static float multiCoefficient(StringSimilarity algorithm, String a, String b, int... sliceSizes){
		var sum = 0F;
		for(int i : sliceSizes){
			sum += coefficient(algorithm, a, b, i);
		}
		return sum / sliceSizes.length;
	}

	StringSimilarity getInstance();

	float coefficient(String a, String b, int sliceSize);

}
