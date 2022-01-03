package de.hypercdn.commons.imp.lock;

import de.hypercdn.commons.api.lock.LockContainer;

import java.util.Objects;
import java.util.concurrent.locks.Lock;

/**
 * Implementation of a lock container which is implements auto closeable functionality
 *
 * @param <T> type of lock
 */
public class CloseableLockContainer<T extends Lock> implements LockContainer<T>, AutoCloseable{

	private final T lock;

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
