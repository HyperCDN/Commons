package de.hypercdn.commons.api.barrier;

import java.util.concurrent.TimeUnit;

/**
 * The interface Barrier.
 */
public interface Barrier{

	/**
	 * Will block till the barrier has been acquired
	 *
	 * @throws InterruptedException when this was not possible
	 */
	void acquire() throws InterruptedException;

	/**
	 * Tries to acquire the barrier without blocking.
	 *
	 * @return true if the barrier has been acquired, false if not
	 */
	boolean tryAcquire();

	/**
	 * Tries to acquire the barrier within the given timeframe
	 *
	 * @param time     amount
	 * @param timeUnit of time
	 *
	 * @return true if the block has been acquired, false if not
	 */
	boolean tryAcquire(long time, TimeUnit timeUnit);

	/**
	 * Will release the current acquisition on the barrier
	 * Unlike releasing a lock, this can be done from any thread context if it is considered safe to do so.
	 * This allows to use barriers instead of locks in async environments.
	 */
	void release();

}
