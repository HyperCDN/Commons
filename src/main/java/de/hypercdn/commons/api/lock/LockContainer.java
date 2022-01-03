package de.hypercdn.commons.api.lock;

import de.hypercdn.commons.api.wrap.Wrap;

import java.util.concurrent.locks.Lock;

/**
 * Wraps lock typed objects
 *
 * @param <T> of lock
 */
public interface LockContainer<T extends Lock> extends Wrap<T>{}
