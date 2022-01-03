package de.hypercdn.commons.api.provider;

/**
 * Represents a class which is used to provide data through a given input
 *
 * @param <I> identifier type
 * @param <O> output type
 */
public interface Provider<I, O>{

	/**
	 * Returns the stored object by the supplied identifier
	 *
	 * @param i identifier
	 *
	 * @return stored object or null if none has been stored
	 */
	O getStored(I i);

	/**
	 * Returns the stored object by the supplied identifier
	 *
	 * @param i identifier
	 *
	 * @return stored object
	 */
	O getAndStore(I i);

	/**
	 * Removes a stored object from the provider
	 *
	 * @param i identifier
	 */
	void removeStored(I i);

	/**
	 * Clears the storage of this provider
	 */
	void clear();

	/**
	 * Returns the amount of stored entities
	 *
	 * @return amount
	 */
	int size();

}
