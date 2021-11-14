package de.hypercdn.commons.api.lock;

import de.hypercdn.commons.api.wrap.Wrap;

import java.util.concurrent.locks.Lock;

/**
 * The interface Lock container.
 *
 * @param <T> the type parameter
 */
public interface LockContainer<T extends Lock> extends Wrap<T>{}
