package de.hypercdn.commons.api.queue;

/**
 * Represents a simplifying wrap for queues
 *
 * @param <T> entry type
 */
public interface SimpleBlockingQueue<T> extends SimpleQueue<T>{

	/**
	 * Pulls the next object from the queue
	 * Will block when no entry is available
	 *
	 * @return object
	 *
	 * @throws InterruptedException when interrupted
	 */
	T get() throws InterruptedException;

}
