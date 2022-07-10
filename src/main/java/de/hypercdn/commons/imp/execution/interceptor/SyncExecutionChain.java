package de.hypercdn.commons.imp.execution.interceptor;

import de.hypercdn.commons.api.execution.action.ExecutionAction;
import de.hypercdn.commons.imp.execution.misc.exception.ExecutionException;

public class SyncExecutionChain<IN, OUT> extends GenericExecutionChain{

	public SyncExecutionChain(ExecutionAction<IN, OUT> action){
		super(action);
	}

	@Override
	public Object execute(Object object) throws ExecutionException{
		setLast(object, null);
		var result = ((ExecutionAction.Internal<IN, OUT>) action).executeInternal((IN) object);
		setLast(object, result);
		return result;
	}

}
