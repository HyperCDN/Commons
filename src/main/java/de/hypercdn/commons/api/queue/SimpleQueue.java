package de.hypercdn.commons.api.queue;

import java.util.Queue;

public interface SimpleQueue<T>{

	T get() throws InterruptedException;

	void put(T t);

	int size();

	void clear();

	Queue<T> getWrappedQueue();

}
