package de.hypercdn.commons.imp.execution.action;

import de.hypercdn.commons.api.execution.action.ExecutionAction;
import de.hypercdn.commons.imp.execution.misc.ExecutionException;
import de.hypercdn.commons.imp.execution.misc.ExecutionStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Implements a chained execution action
 *
 * @param <IN>    type
 * @param <TRANS> type
 * @param <OUT>   type
 */
public class ChainedExecutionAction<IN, TRANS, OUT> implements ExecutionAction<IN, OUT>{

	private final ExecutionAction<IN, TRANS> firstExecutionAction;
	private final ExecutionAction<TRANS, OUT> secondExecutionAction;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final ExecutionStack executionStack = new ExecutionStack();
	private volatile long lastExecutionDuration = -1L;

	public ChainedExecutionAction(ExecutionAction<IN, TRANS> firstExecutionAction, ExecutionAction<TRANS, OUT> secondExecutionAction){
		this.firstExecutionAction = firstExecutionAction;
		this.secondExecutionAction = secondExecutionAction;
	}

	@Override
	public ExecutionAction<IN, OUT> setCheck(BooleanSupplier check){
		Objects.requireNonNull(check);
		firstExecutionAction.setCheck(check);
		secondExecutionAction.setCheck(check);
		return this;
	}

	@Override
	public BooleanSupplier getCheck(){
		return () ->
			firstExecutionAction.getCheck().getAsBoolean()
				&& secondExecutionAction.getCheck().getAsBoolean();
	}

	@Override
	public ExecutionAction<IN, OUT> setExecutor(Executor executor){
		Objects.requireNonNull(executor);
		firstExecutionAction.setExecutor(executor);
		secondExecutionAction.setExecutor(executor);
		return this;
	}

	@Override
	public Executor getExecutor(){
		return firstExecutionAction.getExecutor();
	}

	@Override
	public Supplier<IN> getInputSupplier(){
		return firstExecutionAction.getInputSupplier();
	}

	@Override
	public ExecutionAction<IN, OUT> setInputSupplier(Supplier<IN> inputSupplier){
		Objects.requireNonNull(inputSupplier);
		firstExecutionAction.setInputSupplier(inputSupplier);
		return this;
	}

	@Override
	public Function<IN, OUT> getActionFunction(){
		throw new UnsupportedOperationException("Not allowed on chained action");
	}

	@Override
	public ExecutionAction<IN, OUT> setActionFunction(Function<IN, OUT> actionFunction){
		throw new UnsupportedOperationException("Not allowed on chained action");
	}

	@Override
	public ExecutionStack getExecutionStack(){
		return executionStack;
	}

	@Override
	public ExecutionAction<IN, OUT> passExecutionStack(ExecutionStack executionStack){
		this.executionStack.push(executionStack);
		return this;
	}

	@Override
	public float lastExecutionDuration(){
		return lastExecutionDuration / 1_000_000F;
	}

	@Override
	public void queue(IN input, Consumer<? super OUT> successConsumer, Consumer<? super Throwable> exceptionConsumer){
		logger.trace("Initializing execution of " + getClass().getSimpleName() + "#" + hashCode());
		var startTime = System.nanoTime();

		Consumer<Throwable> failureCallback = (throwable) -> {
			lastExecutionDuration = (System.nanoTime() - startTime);
			logger.trace("Failed execution of " + getClass().getSimpleName() + "#" + hashCode() + " after " + lastExecutionDuration() + " ms");

			if(exceptionConsumer != null){
				exceptionConsumer.accept(throwable instanceof ExecutionException ? throwable : new ExecutionException(throwable));
			}
		};

		Consumer<OUT> successCallback = out -> {
			lastExecutionDuration = (System.nanoTime() - startTime);
			logger.trace("Finished execution of " + getClass().getSimpleName() + "#" + hashCode() + " after " + lastExecutionDuration() + " ms");

			if(successConsumer != null){
				successConsumer.accept(out);
			}
		};

		try{
			firstExecutionAction.queue(firstResult -> {
				secondExecutionAction.queue(firstResult, successCallback, failureCallback);
			}, failureCallback);
		}
		catch(Throwable t){
			logger.trace("Failed to initialize execution of " + getClass().getSimpleName() + "#" + hashCode() + " after " + lastExecutionDuration() + " ms");
			t.setStackTrace(executionStack.getFullContextStack(t.getStackTrace()));
			lastExecutionDuration = (System.nanoTime() - startTime);

			if(t instanceof Error){
				throw t;
			}
			if(exceptionConsumer != null){
				exceptionConsumer.accept(t instanceof ExecutionException ? t : new ExecutionException(t));
			}
		}
	}

	@Override
	public OUT execute(IN input) throws ExecutionException{
		logger.debug("Started execution of " + getClass().getSimpleName() + "#" + hashCode());
		var startTime = System.nanoTime();
		try{
			if(!getCheck().getAsBoolean()){
				return null;
			}
			var first = firstExecutionAction.execute(input);
			var result = secondExecutionAction.execute(first);
			return result;
		}
		catch(Throwable t){
			if(t instanceof Error || t instanceof ExecutionException){
				throw t;
			}
			throw new ExecutionException(t);
		}
		finally{
			lastExecutionDuration = (System.nanoTime() - startTime);
			logger.debug("Finished execution of " + getClass().getSimpleName() + "#" + hashCode() + " after " + lastExecutionDuration() + " ms");
		}
	}

}
