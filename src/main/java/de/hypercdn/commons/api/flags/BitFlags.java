package de.hypercdn.commons.api.flags;

import java.util.List;

/**
 * The interface Bit flags.
 */
public interface BitFlags<T extends Number>{

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
	 * @param <E> the type parameter
	 *
	 * @return the flags
	 */
	<E extends BitFlag> List<E> getFlags();

	/**
	 * Gets value.
	 *
	 * @return the value
	 */
	T getValue();

}
