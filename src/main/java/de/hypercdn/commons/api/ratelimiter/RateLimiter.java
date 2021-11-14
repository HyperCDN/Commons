package de.hypercdn.commons.api.ratelimiter;

import de.hypercdn.commons.imp.ratelimiter.RateLimiterException;

import java.util.concurrent.TimeUnit;

/**
 * The interface Rate limiter.
 */
public interface RateLimiter{

	/**
	 * Gets refill unit.
	 *
	 * @return the refill unit
	 */
	TimeUnit getRefillUnit();

	/**
	 * Gets refill interval.
	 *
	 * @return the refill interval
	 */
	long getRefillInterval();

	/**
	 * Gets underflow factor.
	 *
	 * @return the underflow factor
	 */
	int getUnderflowFactor();

	/**
	 * Sets underflow factor.
	 *
	 * @param factor the factor
	 *
	 * @return the underflow factor
	 */
	RateLimiter setUnderflowFactor(int factor);

	/**
	 * Gets maximum.
	 *
	 * @return the maximum
	 */
	long getMaximum();

	/**
	 * Sets maximum.
	 *
	 * @param max the max
	 *
	 * @return the maximum
	 */
	RateLimiter setMaximum(long max);

	/**
	 * Gets remaining.
	 *
	 * @return the remaining
	 */
	long getRemaining();

	/**
	 * Gets refill timestamp.
	 *
	 * @return the refill timestamp
	 */
	long getRefillTimestamp();

	/**
	 * Drain by.
	 *
	 * @param amount the amount
	 *
	 * @throws RateLimiterException the rate limiter exception
	 */
	void drainBy(long amount) throws RateLimiterException;

	/**
	 * Try drain by boolean.
	 *
	 * @param amount the amount
	 *
	 * @return the boolean
	 */
	boolean tryDrainBy(long amount);

}
