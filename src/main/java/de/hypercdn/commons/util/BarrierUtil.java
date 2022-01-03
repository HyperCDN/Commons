package de.hypercdn.commons.util;

import de.hypercdn.commons.api.barrier.Barrier;

import java.util.concurrent.TimeUnit;

/**
 * Util class for working with barriers
 */
public class BarrierUtil{

	private BarrierUtil(){}

	/**
	 * Tries to acquire the lock within ten seconds
	 *
	 * @param barrier to acquire
	 */
	public static void tryBarricadeInTime(Barrier barrier){
		tryBarricadeInTime(barrier, 10, TimeUnit.SECONDS);
	}

	/**
	 * Tries to acquire the barrier within a specified amount of time
	 *
	 * @param barrier  to acquire
	 * @param time     to wait
	 * @param timeUnit of time
	 */
	public static void tryBarricadeInTime(Barrier barrier, long time, TimeUnit timeUnit){
		if(!barrier.tryAcquire(time, timeUnit)){
			throw new IllegalStateException("Failed to acquire barrier within " + time + " " + timeUnit.name());
		}
	}

	/**
	 * Execute the runnable after the lock has been acquired
	 * This times out after ten seconds
	 *
	 * @param barrier  to acquire
	 * @param runnable to run after acquisition
	 */
	public static void executeBarricaded(Barrier barrier, Runnable runnable){
		tryBarricadeInTime(barrier);
		runnable.run();
	}

}
