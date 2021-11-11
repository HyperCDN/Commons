package de.hypercdn.commons.api.lock;

import de.hypercdn.commons.api.wrap.Wrap;

import java.util.concurrent.locks.Lock;

public interface LockContainer<T extends Lock> extends Wrap<T> {}
