package de.hypercdn.commons.imp.lock;

import de.hypercdn.commons.api.lock.LockContainer;

import java.util.Objects;
import java.util.concurrent.locks.Lock;

/**
 * The type Closeable lock container.
 *
 * @param <T> the type parameter
 */
public class CloseableLockContainer<T extends Lock> implements LockContainer<T>, AutoCloseable{

	private final T lock;

	/**
	 * Instantiates a new Closeable lock container.
	 *
	 * @param t the t
	 */
	public CloseableLockContainer(T t){
		Objects.requireNonNull(t);
		this.lock = t;
	}

	@Override
	public T getWrapped(){
		return lock;
	}

	@Override
	public void close(){
		try{
			lock.unlock();
		}
		catch(IllegalMonitorStateException ignore){
		}
	}

}
