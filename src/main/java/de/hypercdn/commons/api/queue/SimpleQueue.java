package de.hypercdn.commons.api.queue;

import java.util.Queue;

/**
 * The interface Simple queue.
 *
 * @param <T> the type parameter
 */
public interface SimpleQueue<T>{

	/**
	 * Get t.
	 *
	 * @return the t
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	T get() throws InterruptedException;

	/**
	 * Put.
	 *
	 * @param t the t
	 */
	void put(T t);

	/**
	 * Size int.
	 *
	 * @return the int
	 */
	int size();

	/**
	 * Clear.
	 */
	void clear();

	/**
	 * Gets wrapped queue.
	 *
	 * @return the wrapped queue
	 */
	Queue<T> getWrappedQueue();

}
