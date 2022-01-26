package de.hypercdn.commons.api.properties.link;

/**
 * Indicates that objects of the implementing class can be linked as a chain
 *
 * @param <T>
 */
public interface Chain<T extends Chain<T>>{

	/**
	 * Points to the next object within the current chain
	 *
	 * @return next object
	 */
	T next();

	/**
	 * Points to the previous object within the current chain
	 *
	 * @return previous object
	 */
	T previous();

}
