package de.hypercdn.commons.imp.ratelimiter;

import de.hypercdn.commons.api.ratelimiter.RateLimiter;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * The type Generic rate limiter.
 */
public class GenericRateLimiter implements RateLimiter{

	private final TimeUnit refillUnit;
	private final long refillInterval;
	private final long nsWindowSize;
	private long filler;
	private long maximum;
	private int underflowFactor = 2;
	private long nsPerUsage;

	/**
	 * Instantiates a new Generic rate limiter.
	 *
	 * @param refillUnit     the refill unit
	 * @param refillInterval the refill interval
	 */
	public GenericRateLimiter(TimeUnit refillUnit, long refillInterval){
		Objects.requireNonNull(refillUnit);
		this.refillUnit = refillUnit;
		this.refillInterval = refillInterval;
		this.nsWindowSize = refillUnit.toNanos(Math.abs(refillInterval));
	}

	@Override
	public TimeUnit getRefillUnit(){
		return refillUnit;
	}

	@Override
	public long getRefillInterval(){
		return refillInterval;
	}

	@Override
	public int getUnderflowFactor(){
		return underflowFactor;
	}

	@Override
	public synchronized RateLimiter setUnderflowFactor(int factor){
		this.underflowFactor = factor;
		return this;
	}

	@Override
	public long getMaximum(){
		return maximum;
	}

	@Override
	public synchronized RateLimiter setMaximum(long max){
		this.maximum = max;
		this.nsPerUsage = this.nsWindowSize / this.maximum;
		return this;
	}

	@Override
	public synchronized long getRemaining(){
		var current = System.nanoTime();
		if(filler < current){
			filler = current;
		}
		var div = Math.max(current + nsWindowSize - filler, 0);
		return (div / nsPerUsage);
	}

	@Override
	public synchronized long getRefillTimestamp(){
		return System.currentTimeMillis() + ((nsWindowSize - (getRemaining() * nsPerUsage)) / 1_000_000);
	}

	@Override
	public synchronized void drainBy(long amount) throws RateLimiterException{
		long current = System.nanoTime();
		// lower limit
		if(filler < current){
			filler = current;
		}
		// add take to filler
		filler += nsPerUsage * amount;
		// upper limit
		if(filler > current + (nsWindowSize * 2)){
			filler = current + (nsWindowSize * 2);
		}
		// check if filler fits inside the window
		if((current + nsWindowSize) < filler){
			throw new RateLimiterException("Rate limit exceeded!");
		}
	}

	@Override
	public synchronized boolean tryDrainBy(long amount){
		try{
			drainBy(amount);
			return true;
		}
		catch(RateLimiterException e){
			return false;
		}
	}

}
