package de.hypercdn.commons.imp.execution.misc.exception;

/**
 * Wrap used to simplify exception handling in the execution actions by wrapping them as runtime exception
 */
public class FriendlyExecutionException extends ExecutionException {

	public FriendlyExecutionException(String message){
		super(message);
	}

	public FriendlyExecutionException(Throwable throwable){
		super(throwable);
	}

	public FriendlyExecutionException(String message, Throwable throwable){
		super(message, throwable);
	}

}
