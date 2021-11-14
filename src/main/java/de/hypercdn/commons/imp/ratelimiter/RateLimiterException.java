package de.hypercdn.commons.imp.ratelimiter;

/**
 * The type Rate limiter exception.
 */
public class RateLimiterException extends RuntimeException{

	/**
	 * Instantiates a new Rate limiter exception.
	 *
	 * @param message the message
	 */
	public RateLimiterException(String message){
		super(message);
	}

	/**
	 * Instantiates a new Rate limiter exception.
	 *
	 * @param message the message
	 * @param e       the e
	 */
	public RateLimiterException(String message, Exception e){
		super(message, e);
	}

}
