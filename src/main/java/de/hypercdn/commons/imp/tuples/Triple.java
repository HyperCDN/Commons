package de.hypercdn.commons.imp.tuples;

/**
 * The type Triple.
 *
 * @param <V1> the type parameter
 * @param <V2> the type parameter
 * @param <V3> the type parameter
 */
public class Triple<V1, V2, V3>{

	private final V1 v1;
	private final V2 v2;
	private final V3 v3;

	/**
	 * Instantiates a new Triple.
	 *
	 * @param v1 the v 1
	 * @param v2 the v 2
	 * @param v3 the v 3
	 */
	public Triple(V1 v1, V2 v2, V3 v3){
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	/**
	 * Get value 1 v 1.
	 *
	 * @return the v 1
	 */
	public V1 getValue1(){
		return v1;
	}

	/**
	 * Get value 2 v 2.
	 *
	 * @return the v 2
	 */
	public V2 getValue2(){
		return v2;
	}

	/**
	 * Get value 3 v 3.
	 *
	 * @return the v 3
	 */
	public V3 getValue3(){
		return v3;
	}

}
