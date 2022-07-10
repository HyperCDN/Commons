package de.hypercdn.commons.imp.execution.interceptor;

import de.hypercdn.commons.api.execution.action.ExecutionAction;
import de.hypercdn.commons.api.execution.interceptor.ExecutionInterceptor;

public abstract class GenericExecutionChain implements ExecutionInterceptor.Chain.Internal{

	protected ExecutionAction<?, ?> action;
	volatile Object lastIn;
	volatile Object lastOut;

	public GenericExecutionChain(ExecutionAction<?, ?> action){
		this.action = action;
	}

	@Override
	public ExecutionAction<?, ?> action(){
		return action;
	}

	@Override
	public void setLast(Object in, Object out){
		this.lastIn = in;
		this.lastOut = out;
	}

	@Override
	public Object lastIn(){
		return lastIn;
	}

	@Override
	public Object lastOut(){
		return lastOut;
	}

}
