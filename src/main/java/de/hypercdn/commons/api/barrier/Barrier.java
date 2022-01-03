package de.hypercdn.commons.api.barrier;

import java.util.concurrent.TimeUnit;

public interface Barrier{

	void acquire() throws InterruptedException;

	boolean tryAcquire();

	boolean tryAcquire(long time, TimeUnit timeUnit);

	void release();

}
