package de.hypercdn.commons.imp.ratelimiter;

public class RateLimiterException extends RuntimeException {

    public RateLimiterException(String message){
        super(message);
    }

    public RateLimiterException(String message, Exception e){
        super(message, e);
    }

}
