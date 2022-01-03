package de.hypercdn.commons.imp.queue;

import de.hypercdn.commons.api.properties.misc.Suspendable;
import de.hypercdn.commons.api.queue.SimpleBlockingQueue;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Implementation of a suspendable blocking queue
 *
 * @param <T>
 */
public class SuspendableBlockingQueue<T> implements Suspendable, SimpleBlockingQueue<T>{

	private final Queue<T> queue;
	private boolean isSuspended = false;

	public SuspendableBlockingQueue(){
		this(new LinkedBlockingQueue<>());
	}

	public SuspendableBlockingQueue(Queue<T> queue){
		Objects.requireNonNull(queue);
		this.queue = queue;
	}

	@Override
	public T get() throws InterruptedException{
		synchronized(queue){
			while(isSuspended || queue.isEmpty()){
				queue.wait();
			}
			return queue.poll();
		}
	}

	@Override
	public void put(T t){
		synchronized(queue){
			queue.offer(t);
			if(!isSuspended){
				queue.notify();
			}
		}
	}

	@Override
	public int size(){
		return queue.size();
	}

	@Override
	public void clear(){
		queue.clear();
	}

	@Override
	public synchronized boolean isSuspended(){
		return isSuspended;
	}

	@Override
	public synchronized boolean suspend(){
		var wasSuspended = !isSuspended;
		isSuspended = true;
		return wasSuspended;
	}

	@Override
	public synchronized boolean resume(){
		var wasSuspended = isSuspended;
		isSuspended = false;
		return wasSuspended;
	}

	@Override
	public Queue<T> getWrapped(){
		return queue;
	}

}
