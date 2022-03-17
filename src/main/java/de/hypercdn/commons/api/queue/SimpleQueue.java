package de.hypercdn.commons.api.queue;

import de.hypercdn.commons.api.wrap.Wrap;

import java.util.Queue;

public interface SimpleQueue<T> extends Wrap<Queue<T>>{

	/**
	 * Pulls the next object from the queue
	 *
	 * @return object
	 *
	 * @throws Exception on exception
	 */
	T get() throws Exception;

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
