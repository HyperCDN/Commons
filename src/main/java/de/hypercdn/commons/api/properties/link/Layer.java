package de.hypercdn.commons.api.properties.link;

/**
 * Indicates that objects of the implementing class can be linked as layers
 *
 * @param <T>
 */
public interface Layer<T extends Layer<T>>{

	/**
	 * Points to the next inner object of the layer
	 *
	 * @return inner object
	 */
	T inner();

	/**
	 * Points to the next outer object of the layer
	 *
	 * @return outer layer
	 */
	T outer();

}
