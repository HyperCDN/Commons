package de.hypercdn.commons.util;

import de.hypercdn.commons.api.barrier.Barrier;

import java.util.concurrent.TimeUnit;

public class BarrierUtil{

	private BarrierUtil(){}

	public static void tryBarricadeInTime(Barrier barrier){
		tryBarricadeInTime(barrier, 10, TimeUnit.SECONDS);
	}

	public static void tryBarricadeInTime(Barrier barrier, long time, TimeUnit timeUnit){
		if(!barrier.tryAcquire(time, timeUnit)){
			throw new IllegalStateException("Failed to acquire barrier within " + time + " " + timeUnit.name());
		}
	}

	public static void executeBarricaded(Barrier barrier, Runnable runnable){
		tryBarricadeInTime(barrier);
		runnable.run();
	}

}
