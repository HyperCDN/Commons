package de.hypercdn.commons.imp.lock;

import de.hypercdn.commons.api.wrap.Wrap;
import de.hypercdn.commons.imp.barrier.ReentrantBarrier;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Implementation of a barrier wrapped as lock
 */
public class ReentrantBarrierLock implements Lock, Wrap<ReentrantBarrier>{

	private final ReentrantBarrier reentrantBarrier = new ReentrantBarrier();

	@Override
	public void lock(){
		reentrantBarrier.acquire();
	}

	@Override
	public void lockInterruptibly() throws InterruptedException{
		lock();
	}

	@Override
	public boolean tryLock(){
		return reentrantBarrier.tryAcquire();
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException{
		return reentrantBarrier.tryAcquire(time, unit);
	}

	@Override
	public void unlock(){
		reentrantBarrier.release();
	}

	@Override
	public Condition newCondition(){
		throw new UnsupportedOperationException();
	}

	@Override
	public ReentrantBarrier getWrapped(){
		return reentrantBarrier;
	}

}
