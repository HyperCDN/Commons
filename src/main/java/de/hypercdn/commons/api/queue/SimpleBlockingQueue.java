package de.hypercdn.commons.api.queue;

import de.hypercdn.commons.api.wrap.Wrap;

import java.util.Queue;

/**
 * Represents a simplifying wrap for queues
 *
 * @param <T> entry type
 */
public interface SimpleBlockingQueue<T> extends Wrap<Queue<T>>{

	/**
	 * Pulls the next object from the queue
	 * Will block when no entry is available
	 *
	 * @return object
	 *
	 * @throws InterruptedException when interrupted
	 */
	T get() throws InterruptedException;

	/**
	 * Adds a new object to the queue
	 *
	 * @param t object
	 */
	void put(T t);

	/**
	 * Returns the amount of elements currently enqueued
	 *
	 * @return amount
	 */
	int size();

	/**
	 * Removes all elements from the queue
	 */
	void clear();

}
