package de.hypercdn.commons.api.flags;

import java.util.List;

/**
 * The interface Bit flags.
 */
public interface BitFlags{

	/**
	 * Set.
	 *
	 * @param flags the flags
	 */
	void set(BitFlag... flags);

	/**
	 * Unset.
	 *
	 * @param flags the flags
	 */
	void unset(BitFlag... flags);

	/**
	 * Has boolean.
	 *
	 * @param flag the flag
	 *
	 * @return the boolean
	 */
	boolean has(BitFlag flag);

	/**
	 * Gets flags.
	 *
	 * @param <T> the type parameter
	 *
	 * @return the flags
	 */
	<T extends BitFlags> List<T> getFlags();

}
