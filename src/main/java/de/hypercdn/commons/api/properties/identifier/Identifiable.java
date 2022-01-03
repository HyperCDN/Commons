package de.hypercdn.commons.api.properties.identifier;

/**
 * Indicates that a class provides an identifiable value for its instances
 *
 * @param <T> of id value
 */
public interface Identifiable<T>{

	/**
	 * Returns the identifier of this object
	 *
	 * @return id value
	 */
	T getIdentifier();

}
