package de.hypercdn.commons.imp.ratelimiter;

import de.hypercdn.commons.api.ratelimiter.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Generic rate limiter implementation
 */
public class GenericRateLimiter implements RateLimiter{

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private final TimeUnit refillUnit;
	private final long refillInterval;
	private final long nsWindowSize;
	private long filler;
	private long maximum;
	private int underflowFactor = 2;
	private long nsPerUsage;

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
		logger.trace("Trying to drain rate limiter " + this + " by " + amount);
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
			logger.trace("Draining rate limiter " + this + " by " + amount + " failed. Window is " + ((current + nsWindowSize) / (float) filler) * 100 + "% depleted");
			throw new RateLimiterException("Rate limit exceeded!");
		}
		logger.trace("Drained rate limiter " + this + " by " + amount + ". Window is " + ((current + nsWindowSize) / (float) filler) * 100 + "% depleted");
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
