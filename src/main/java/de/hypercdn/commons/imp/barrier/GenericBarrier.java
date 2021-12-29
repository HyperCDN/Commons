package de.hypercdn.commons.imp.barrier;

import de.hypercdn.commons.api.barrier.Barrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class GenericBarrier implements Barrier{

	private final int timeoutmsDelay;
	private final BlockingQueue<Thread> callQueue = new LinkedBlockingQueue<>();
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public GenericBarrier(){
		this.timeoutmsDelay = -20;
	}

	public GenericBarrier(int timeoutmsDelay){
		this.timeoutmsDelay = timeoutmsDelay;
	}

	@Override
	public void acquire(){
		long startTime = System.nanoTime();
		try{
			var thread = Thread.currentThread();
			synchronized(this){
				if(callQueue.peek() == null){
					callQueue.put(thread);
					return;
				}
				else if(callQueue.peek() == thread){
					return;
				}
				else{
					callQueue.put(thread);
				}
			}
			synchronized(thread){
				thread.wait();
			}
		}
		catch(InterruptedException e){
			callQueue.remove(Thread.currentThread());
			throw new RuntimeException(e);
		}
		finally{
			logger.trace("Barrier " + this.hashCode() + " acquired by T" + Thread.currentThread().getId() + " after " + (System.nanoTime() - startTime) / 1_000_000F + "ms");
		}
	}

	@Override
	public boolean tryAcquire(){
		try{
			var thread = Thread.currentThread();
			synchronized(this){
				if(callQueue.peek() == null){
					callQueue.put(thread);
					return true;
				}
				else{
					return callQueue.peek() == thread;
				}
			}
		}
		catch(InterruptedException e){
			throw new RuntimeException(e);
		}
		finally{
			logger.trace("Barrier " + this.hashCode() + " tried to acquire by T" + Thread.currentThread().getId() + " currently held by T" + ((this.callQueue.peek() != null) ? this.callQueue.peek().getId() : "none"));
		}
	}

	@Override
	public boolean tryAcquire(long time, TimeUnit timeUnit){
		long startTime = System.nanoTime();
		try{
			var thread = Thread.currentThread();
			synchronized(this){
				if(callQueue.peek() == null){
					callQueue.put(thread);
					return true;
				}
				else if(callQueue.peek() == thread){
					return true;
				}
				else{
					callQueue.put(thread);
				}
			}
			var waitTime = System.currentTimeMillis();
			synchronized(thread){
				thread.wait(timeUnit.toMillis(time));
				if(System.currentTimeMillis() > (waitTime + timeUnit.toMillis(time) + timeoutmsDelay)){
					callQueue.remove(thread);
					return false;
				}
				return true;
			}
		}
		catch(InterruptedException e){
			callQueue.remove(Thread.currentThread());
			throw new RuntimeException(e);
		}
		finally{
			logger.trace("Tried to acquire barrier " + this.hashCode() + " by T" + Thread.currentThread().getId() + " currently held by T" + ((this.callQueue.peek() != null) ? this.callQueue.peek().getId() : "none") + " after " + (System.nanoTime() - startTime) / 1_000_000F + "ms");
		}
	}

	@Override
	public void release(){
		var thread = callQueue.poll();
		if(thread != null){
			var next = callQueue.peek();
			if(next != null){
				synchronized(next){
					next.notify();
				}
			}
			logger.trace("Barrier " + this.hashCode() + " released from T" + thread.getId());
		}
	}

}
