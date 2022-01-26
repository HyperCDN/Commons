package de.hypercdn.commons.api.properties.link;

import de.hypercdn.commons.api.properties.identifier.Identifiable;

/**
 * Indicates that objects of the implementing class have ids and can be linked as layers
 *
 * @param <T>
 * @param <U>
 */
public interface IDLayer<T extends IDLayer<T, U> & Identifiable<U>, U> extends Layer<T>{

	/**
	 * Returns the id of the inner object
	 *
	 * @return id
	 */
	U innerId();

	/**
	 * Returns the id of the outer object
	 *
	 * @return id
	 */
	U outerId();

}
