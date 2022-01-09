package de.hypercdn.commons.api.flags;

/**
 * Represents a specific bit flag
 */
public interface BitFlag<T extends Number>{

	/**
	 * Returns the raw value for the given flag
	 *
	 * @return raw value as T
	 */
	T getRawValue();

	/**
	 * Returns a new and combined bitflag
	 *
	 * @param other to combine
	 * @return combined bitflag
	 */
	BitFlag<T> and(BitFlag<T> other);

	/**
	 * Returns a new or combined bitflag
	 *
	 * @param other to combine
	 * @return combined bitflag
	 */
	BitFlag<T> or(BitFlag<T> other);

}
