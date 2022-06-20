package de.hypercdn.commons.imp.execution.misc.exception;

import de.hypercdn.commons.api.wrap.Wrap;

/**
 * Wrap used to simplify exception handling in the execution actions by wrapping them as runtime exception
 */
public class ExecutionException extends RuntimeException implements Wrap<Throwable>{

	public ExecutionException(String message){
		super(message);
	}

	public ExecutionException(Throwable throwable){
		super(throwable);
	}

	public ExecutionException(String message, Throwable throwable){
		super(message, throwable);
	}

	@Override
	public Throwable getWrapped(){
		return super.getCause();
	}

}
