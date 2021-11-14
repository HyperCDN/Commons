package de.hypercdn.commons.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type Lock util.
 */
public class LockUtil{

	private LockUtil(){}

	/**
	 * Try lock in time.
	 *
	 * @param lock the lock
	 */
	public static void tryLockInTime(Lock lock){
		tryLockInTime(lock, 10, TimeUnit.SECONDS);
	}

	/**
	 * Try lock in time.
	 *
	 * @param lock     the lock
	 * @param time     the time
	 * @param timeUnit the time unit
	 */
	public static void tryLockInTime(Lock lock, long time, TimeUnit timeUnit){
		try{
			if(!lock.tryLock() && !lock.tryLock(time, timeUnit)){
				throw new IllegalStateException("Failed to acquire lock within " + time + " " + timeUnit.name());
			}
		}
		catch(InterruptedException e){
			throw new IllegalStateException("Unable to acquire lock while thread is interrupted!");
		}
	}

	/**
	 * Execute locked.
	 *
	 * @param lock     the lock
	 * @param runnable the runnable
	 */
	public static void executeLocked(ReentrantLock lock, Runnable runnable){
		try{
			tryLockInTime(lock);
			runnable.run();
		}
		finally{
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
	}

}
