package de.hypercdn.commons.api.ratelimiter;

import de.hypercdn.commons.imp.ratelimiter.RateLimiterException;

import java.util.concurrent.TimeUnit;

public interface RateLimiter{

	TimeUnit getRefillUnit();

	long getRefillInterval();

	int getUnderflowFactor();

	RateLimiter setUnderflowFactor(int factor);

	long getMaximum();

	RateLimiter setMaximum(long max);

	long getRemaining();

	long getRefillTimestamp();

	void drainBy(long amount) throws RateLimiterException;

	boolean tryDrainBy(long amount);

}
