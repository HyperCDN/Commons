package de.hypercdn.commons.imp.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockUtil{

	private LockUtil(){}

	public static void tryLockInTime(Lock lock){
		tryLockInTime(lock, 10, TimeUnit.SECONDS);
	}

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
