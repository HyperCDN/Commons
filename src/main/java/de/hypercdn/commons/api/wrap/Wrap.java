package de.hypercdn.commons.api.wrap;

/**
 * Represents an object with has been wrapped
 *
 * @param <T> wrapped type
 */
public interface Wrap<T>{

	/**
	 * Return the bject which has been wrapped
	 *
	 * @return wrapped object
	 */
	T getWrapped();

}
