package de.hypercdn.commons.api.provider;

/**
 * The interface Provider.
 *
 * @param <I> the type parameter
 * @param <O> the type parameter
 */
public interface Provider<I, O>{

	/**
	 * Gets stored.
	 *
	 * @param i the
	 *
	 * @return the stored
	 */
	O getStored(I i);

	/**
	 * Gets and store.
	 *
	 * @param i the
	 *
	 * @return the and store
	 */
	O getAndStore(I i);

	/**
	 * Remove stored.
	 *
	 * @param i the
	 */
	void removeStored(I i);

	/**
	 * Clear.
	 */
	void clear();

	/**
	 * Size int.
	 *
	 * @return the int
	 */
	int size();

}
