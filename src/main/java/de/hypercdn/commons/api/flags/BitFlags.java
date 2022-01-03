package de.hypercdn.commons.api.flags;

import java.util.List;

/**
 * Represents bit flags which can be set on numbers
 *
 * @param <T> of number
 */
public interface BitFlags<T extends Number>{

	/**
	 * Enables the specified flags
	 *
	 * @param flags to set
	 */
	void set(BitFlag... flags);

	/**
	 * Disables the specified flags
	 *
	 * @param flags to unset
	 */
	void unset(BitFlag... flags);

	/**
	 * Checks if a flag has been enabled
	 *
	 * @param flag to check for
	 *
	 * @return true if enabled, else false
	 */
	boolean has(BitFlag flag);

	/**
	 * Returns a list of all available flags
	 *
	 * @param <E> of the implementation
	 *
	 * @return list of flags
	 */
	<E extends BitFlag> List<E> getFlags();

	/**
	 * Returns the numeric value used to store the flags on
	 *
	 * @return numeric value
	 */
	T getValue();

}
