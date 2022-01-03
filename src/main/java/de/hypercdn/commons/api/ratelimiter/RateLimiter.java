package de.hypercdn.commons.api.ratelimiter;

import de.hypercdn.commons.imp.ratelimiter.RateLimiterException;

import java.util.concurrent.TimeUnit;

/**
 * Represents a rate limiter
 */
public interface RateLimiter{

	/**
	 * Returns the timeunit for the rate refill interval
	 *
	 * @return refill interval time unit
	 */
	TimeUnit getRefillUnit();

	/**
	 * Returns the refill time duration in time units specified by {@link #getRefillUnit()} ()}
	 *
	 * @return refill time duration
	 */
	long getRefillInterval();

	/**
	 * Returns the underflow factor to cap overusing the rate limiter
	 *
	 * @return underflow factor
	 */
	int getUnderflowFactor();

	/**
	 * Sets the underflow factor
	 * A value of 1 indicates that there will be no underflow
	 *
	 * @param factor underflow factor
	 *
	 * @return current instance
	 */
	RateLimiter setUnderflowFactor(int factor);

	/**
	 * Returns the maximum amount of usage tickets within the specified refresh interval
	 *
	 * @return usage tickets
	 */
	long getMaximum();

	/**
	 * Set the maximum amount of usage tickets within the specified refresh interval
	 *
	 * @param max amount
	 *
	 * @return current instance
	 */
	RateLimiter setMaximum(long max);

	/**
	 * Get the amount of currently remaining usage tickets
	 *
	 * @return amount
	 */
	long getRemaining();

	/**
	 * Returns the timestamp of when all usage tickets have been replenished
	 *
	 * @return timestamp
	 */
	long getRefillTimestamp();

	/**
	 * Tries to drain the amount of usage tickets by the specified amount
	 *
	 * @param amount to drain
	 *
	 * @throws RateLimiterException when there are not enough tickets left
	 */
	void drainBy(long amount) throws RateLimiterException;

	/**
	 * Tries to drain the amount of usage tickets by the specified amount
	 *
	 * @param amount to drain
	 *
	 * @return true if successful, else false
	 */
	boolean tryDrainBy(long amount);

}
