package de.hypercdn.commons.imp.barrier;

import de.hypercdn.commons.api.barrier.Barrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Reentrant implementation of a barrier
 */
public class ReentrantBarrier implements Barrier{

	private final BlockingQueue<Thread> callQueue = new LinkedBlockingQueue<>();
	private final ConcurrentHashMap<Thread, Integer> callCounter = new ConcurrentHashMap<>();
	private final int timeoutmsDelta;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public ReentrantBarrier(){
		this.timeoutmsDelta = -20;
	}

	public ReentrantBarrier(int timeoutmsDelay){
		this.timeoutmsDelta = timeoutmsDelay;
	}

	@Override
	public void acquire(){
		long startTime = System.nanoTime();
		try{
			var thread = Thread.currentThread();
			synchronized(this){
				callCounter.putIfAbsent(thread, 0);
				callCounter.computeIfPresent(thread, (t1, i) -> i + 1);
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
			var thread = Thread.currentThread();
			callCounter.computeIfPresent(thread, (t1, i) -> i - 1);
			if(callCounter.get(thread) <= 0){
				callCounter.remove(thread);
				callQueue.remove(thread);
			}
			throw new RuntimeException(e);
		}
		finally{
			logger.debug("Barrier " + this.hashCode() + " acquired " + callCounter.get(Thread.currentThread()) + "x times by T" + Thread.currentThread().getId() + " after " + (System.nanoTime() - startTime) / 1_000_000F + "ms");
		}
	}

	@Override
	public boolean tryAcquire(){
		try{
			var thread = Thread.currentThread();
			synchronized(this){
				callCounter.putIfAbsent(thread, 0);
				callCounter.computeIfPresent(thread, (t1, i) -> i + 1);
				if(callQueue.peek() == null){
					callQueue.put(thread);
					return true;
				}
				else if(callQueue.peek() == thread){
					return true;
				}
				else{
					callCounter.computeIfPresent(thread, (t1, i) -> i - 1);
					if(callCounter.get(thread) <= 0){
						callCounter.remove(thread);
					}
					return false;
				}
			}
		}
		catch(InterruptedException e){
			throw new RuntimeException(e);
		}
		finally{
			logger.debug("Barrier " + this.hashCode() + " tried to acquire by T" + Thread.currentThread().getId() + " currently held by T" + ((this.callQueue.peek() != null) ? this.callQueue.peek().getId() : "none"));
		}
	}

	@Override
	public boolean tryAcquire(long time, TimeUnit timeUnit){
		long startTime = System.nanoTime();
		try{
			var thread = Thread.currentThread();
			synchronized(this){
				callCounter.putIfAbsent(thread, 0);
				callCounter.computeIfPresent(thread, (t1, i) -> i + 1);
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
				if(System.currentTimeMillis() > waitTime + timeUnit.toMillis(time) + timeoutmsDelta){
					callCounter.computeIfPresent(thread, (t1, i) -> i - 1);
					if(callCounter.get(thread) <= 0){
						callCounter.remove(thread);
						callQueue.remove(thread);
					}
					return false;
				}
				return true;
			}
		}
		catch(InterruptedException e){
			var thread = Thread.currentThread();
			callCounter.computeIfPresent(thread, (t1, i) -> i - 1);
			if(callCounter.get(thread) <= 0){
				callCounter.remove(thread);
				callQueue.remove(thread);
			}
			throw new RuntimeException(e);
		}
		finally{
			logger.debug("Tried to acquire barrier " + this.hashCode() + " " + callCounter.get(Thread.currentThread()) + "x times by T" + Thread.currentThread().getId() + " currently held by T" + ((this.callQueue.peek() != null) ? this.callQueue.peek().getId() : "none") + " after " + (System.nanoTime() - startTime) / 1_000_000F + "ms");
		}
	}

	@Override
	public void release(){
		synchronized(this){
			var thread = callQueue.peek();
			if(thread == null){
				return;
			}
			callCounter.computeIfPresent(thread, (t1, i) -> i - 1);
			if(callCounter.containsKey(thread) && callCounter.get(thread) <= 0){
				callQueue.remove(thread);
				callCounter.remove(thread);
				var next = callQueue.peek();
				if(next != null){
					synchronized(next){
						next.notify();
					}
				}
				logger.debug("Barrier " + this.hashCode() + " released from T" + thread.getId());
			}
			else{
				logger.debug("Barrier " + this.hashCode() + " re-released from T" + thread.getId());
			}
		}
	}

}
