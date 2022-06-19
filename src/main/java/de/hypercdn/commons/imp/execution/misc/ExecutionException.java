package de.hypercdn.commons.imp.execution.misc;

import de.hypercdn.commons.api.wrap.Wrap;

/**
 * Wrap used to simplify exception handling in the execution actions by wrapping them as runtime exception
 */
public class ExecutionException extends RuntimeException implements Wrap<Throwable>{

	public ExecutionException(Throwable throwable){
		super(throwable);
	}

	@Override
	public Throwable getWrapped(){
		return super.getCause();
	}

}
