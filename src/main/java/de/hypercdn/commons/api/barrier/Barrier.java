package de.hypercdn.commons.api.barrier;

import java.util.concurrent.TimeUnit;

/**
 * Processing barrier which can be used to organise access to critical code.
 * Similar in functionality to a lock but more suitable for async environments.
 */
public interface Barrier{

	/**
	 * Acquires the barrier.
	 * Will block when it has been acquired before and will continue when the previous hold has been released
	 *
	 * @throws InterruptedException
	 */
	void acquire() throws InterruptedException;

	/**
	 * Tries to acquire the barrier.
	 * Will not block.
	 *
	 * @return true when the barrier has been acquired, false if it is already held
	 */
	boolean tryAcquire();

	/**
	 * Tries to acquire the barrier.
	 * Similar to {@link #tryAcquire()} but will try to perform the acquisition for as long as specified
	 *
	 * @param time     to wait
	 * @param timeUnit of time
	 *
	 * @return true when the barrier has been acquired, false if it is already held
	 */
	boolean tryAcquire(long time, TimeUnit timeUnit);

	/**
	 * Will release the current hold on the barrier
	 * Can be called from anywhere
	 */
	void release();

}
