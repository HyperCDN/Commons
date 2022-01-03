package de.hypercdn.commons.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Util class for working with locks
 */
public class LockUtil{

	private LockUtil(){}

	/**
	 * Tries to acquire the lock within ten seconds
	 *
	 * @param lock to acquire
	 */
	public static void tryLockInTime(Lock lock){
		tryLockInTime(lock, 10, TimeUnit.SECONDS);
	}

	/**
	 * Tries to acquire the lock within a specified amount of time
	 *
	 * @param lock     to acquire
	 * @param time     to wait
	 * @param timeUnit of time
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
	 * Execute the runnable after the lock has been acquired
	 * This times out after ten seconds
	 *
	 * @param lock     to acquire
	 * @param runnable to run after acquisition
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
