package de.hypercdn.commons.api.properties.link;

import de.hypercdn.commons.api.properties.identifier.Identifiable;

/**
 * Indicates that objects of the implementing class have an id and can be linked as a chain
 *
 * @param <T>
 * @param <U>
 */
public interface IDChain<T extends IDChain<T, U> & Identifiable<U>, U> extends Chain<T>{

	/**
	 * Returns the id of the next object
	 *
	 * @return id
	 */
	U nextId();

	/**
	 * Returns the id of the previous object
	 *
	 * @return id
	 */
	U previousId();

}
