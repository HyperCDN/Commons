package de.hypercdn.commons.imp.tuples;

public class Triple<V1, V2, V3>{

	private final V1 v1;
	private final V2 v2;
	private final V3 v3;

	public Triple(V1 v1, V2 v2, V3 v3){
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	public V1 getValue1(){
		return v1;
	}

	public V2 getValue2(){
		return v2;
	}

	public V3 getValue3(){
		return v3;
	}

}
