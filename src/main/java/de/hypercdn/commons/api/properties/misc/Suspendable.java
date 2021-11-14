package de.hypercdn.commons.api.properties.misc;

/**
 * The interface Suspendable.
 */
public interface Suspendable{

	/**
	 * Is suspended boolean.
	 *
	 * @return the boolean
	 */
	boolean isSuspended();

	/**
	 * Suspend boolean.
	 *
	 * @return the boolean
	 */
	boolean suspend();

	/**
	 * Resume boolean.
	 *
	 * @return the boolean
	 */
	boolean resume();

	/**
	 * Suspend boolean.
	 *
	 * @param enable the enable
	 *
	 * @return the boolean
	 */
	default boolean suspend(boolean enable){
		return enable ? suspend() : resume();
	}

}
