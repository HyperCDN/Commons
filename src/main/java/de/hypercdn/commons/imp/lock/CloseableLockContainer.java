package de.hypercdn.commons.imp.lock;

import de.hypercdn.commons.api.lock.LockContainer;

import java.util.Objects;
import java.util.concurrent.locks.Lock;

public class CloseableLockContainer<T extends Lock> implements LockContainer<T>, AutoCloseable {

    private final T lock;

    public CloseableLockContainer(T t) {
        Objects.requireNonNull(t);
        this.lock = t;
    }

    @Override
    public T getWrapped() {
        return lock;
    }

    @Override
    public void close() {
        try{
            lock.unlock();
        }catch(IllegalMonitorStateException ignore){}
    }
}
