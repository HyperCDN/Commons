package de.hypercdn.commons.util;

import de.hypercdn.commons.api.barrier.Barrier;

import java.util.concurrent.TimeUnit;

public class BarrierUtil{

	private BarrierUtil(){}

	/**
	 * Tries to acquire the barrier within 10 seconds
	 *
	 * @param barrier to acquire
	 *
	 * @throws IllegalStateException if the barrier could not get acquired
	 */
	public static void tryBarricadeInTime(Barrier barrier){
		tryBarricadeInTime(barrier, 10, TimeUnit.SECONDS);
	}

	/**
	 * Tries to acquire the barrier within a given time period
	 *
	 * @param barrier  to acquire
	 * @param time     amount
	 * @param timeUnit of time
	 *
	 * @throws IllegalStateException if the barrier could not get acquired
	 */
	public static void tryBarricadeInTime(Barrier barrier, long time, TimeUnit timeUnit){
		if(!barrier.tryAcquire(time, timeUnit)){
			throw new IllegalStateException("Failed to acquire barrier within " + time + " " + timeUnit.name());
		}
	}

	/**
	 * Tries to execute a runnable after acquiring a barrier within 10 seconds
	 *
	 * @param barrier  to acquire
	 * @param runnable to run
	 *
	 * @throws IllegalStateException if the barrier could not get acquired
	 */
	public static void executeBarricaded(Barrier barrier, Runnable runnable){
		tryBarricadeInTime(barrier);
		runnable.run();
	}

}
