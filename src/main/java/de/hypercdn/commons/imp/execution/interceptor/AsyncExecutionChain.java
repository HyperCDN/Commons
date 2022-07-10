package de.hypercdn.commons.imp.execution.interceptor;

import de.hypercdn.commons.api.execution.action.ExecutionAction;
import de.hypercdn.commons.imp.execution.misc.exception.ExecutionException;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class AsyncExecutionChain<IN, OUT> extends GenericExecutionChain{

	public AsyncExecutionChain(ExecutionAction<IN, OUT> action){
		super(action);
	}

	@Override
	public Object execute(Object object) throws ExecutionException{
		setLast(object, null);

		AtomicReference<Object> result = new AtomicReference<>(null);
		AtomicReference<Throwable> exception = new AtomicReference<>(null);

		((ExecutionAction.Internal<IN, OUT>) action).queueInternal((IN) object, result::set, exception::set);

		try{
			var random = new Random();
			while(result.get() == null && exception.get() != null){
				System.out.println("a");
				Thread.sleep(random.nextInt(15) + 1);
			}
		}
		catch(Throwable t){
			exception.set(t);
		}
		// handle possible exception
		var mayThrowable = exception.get();
		if(mayThrowable != null){
			if(mayThrowable instanceof ExecutionException mayThrowableExecutionException){
				throw mayThrowableExecutionException;
			}
			throw new ExecutionException(mayThrowable);
		}

		var resultObject = result.get();
		setLast(object, resultObject);
		return resultObject;
	}

}
