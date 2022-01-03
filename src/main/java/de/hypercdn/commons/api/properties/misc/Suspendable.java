package de.hypercdn.commons.api.properties.misc;

/**
 * Indicates that a class provides suspendable functionality
 */
public interface Suspendable{

	/**
	 * Checks if the functionality is suspended
	 *
	 * @return true if suspended, else false
	 */
	boolean isSuspended();

	/**
	 * Suspends some functionality
	 *
	 * @return true if functionality is now suspended, else false
	 */
	boolean suspend();

	/**
	 * Resumes some functionality
	 *
	 * @return true if functionality is now resuming, else false
	 */
	boolean resume();

	/**
	 * Sets some functionality to suspend or not
	 *
	 * @param enable state
	 *
	 * @return true if change affected behaviour, else false
	 */
	default boolean suspend(boolean enable){
		return enable ? suspend() : resume();
	}

}
