package de.hypercdn.commons.imp.tuples;

/**
 * The type Pair.
 *
 * @param <V1> the type parameter
 * @param <V2> the type parameter
 */
public class Pair<V1, V2>{

	private final V1 v1;
	private final V2 v2;

	/**
	 * Instantiates a new Pair.
	 *
	 * @param v1 the v 1
	 * @param v2 the v 2
	 */
	public Pair(V1 v1, V2 v2){
		this.v1 = v1;
		this.v2 = v2;
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

}
