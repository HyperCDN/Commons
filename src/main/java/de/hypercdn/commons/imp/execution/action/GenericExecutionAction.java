package de.hypercdn.commons.imp.execution.action;

import de.hypercdn.commons.api.execution.action.ExecutionAction;
import de.hypercdn.commons.api.execution.action.ExecutionBuffer;
import de.hypercdn.commons.imp.execution.misc.exception.ExecutionException;
import de.hypercdn.commons.imp.execution.misc.exception.FriendlyExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Generic implementation of an execution action
 *
 * @param <IN>  type
 * @param <OUT> type
 */
public class GenericExecutionAction<IN, OUT> implements ExecutionAction.Internal<IN, OUT>{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private Executor executor = DEFAULT_EXECUTOR;
	private Supplier<IN> inputSupplier = () -> null;
	private Function<IN, OUT> actionFunction = (unused) -> null;
	private BooleanSupplier check = () -> true;
	private volatile long lastExecutionDuration = -1L;

	// internal
	private final ExecutionBuffer<IN, OUT> executionBuffer = new GenericExecutionBuffer<>();

	public GenericExecutionAction(){}

	public GenericExecutionAction(Supplier<IN> inputSupplier, Function<IN, OUT> actionFunction){
		Objects.requireNonNull(inputSupplier);
		Objects.requireNonNull(actionFunction);
		this.inputSupplier = inputSupplier;
		this.actionFunction = actionFunction;
	}

	@Override
	public ExecutionAction<IN, OUT> setCheck(BooleanSupplier check){
		Objects.requireNonNull(check);
		this.check = check;
		return this;
	}

	@Override
	public BooleanSupplier getCheck(){
		return check;
	}

	@Override
	public ExecutionAction<IN, OUT> setExecutor(Executor executor){
		Objects.requireNonNull(executor);
		this.executor = executor;
		return this;
	}

	@Override
	public Executor getExecutor(){
		return executor;
	}

	@Override
	public Supplier<IN> getInputSupplier(){
		return inputSupplier;
	}

	@Override
	public ExecutionAction<IN, OUT> setInputSupplier(Supplier<IN> inputSupplier){
		Objects.requireNonNull(inputSupplier);
		this.inputSupplier = inputSupplier;
		return this;
	}

	@Override
	public Function<IN, OUT> getActionFunction(){
		return actionFunction;
	}

	@Override
	public ExecutionAction<IN, OUT> setActionFunction(Function<IN, OUT> actionFunction){
		Objects.requireNonNull(actionFunction);
		this.actionFunction = actionFunction;
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

		Consumer<Throwable> failureCallback = throwable -> {
			lastExecutionDuration = (System.nanoTime() - startTime);
			if(throwable instanceof FriendlyExecutionException) {
				logger.trace("Stopped execution of " + getClass().getSimpleName() + "#" + hashCode() + " after " + lastExecutionDuration() + " ms");
			} else {
				logger.trace("Failed execution of " + getClass().getSimpleName() + "#" + hashCode() + " after " + lastExecutionDuration() + " ms");
				if(exceptionConsumer != null){
					exceptionConsumer.accept(throwable instanceof ExecutionException ? throwable : new ExecutionException(throwable));
				}
			}
		};

		Consumer<OUT> successCallback = out -> {
			lastExecutionDuration = (System.nanoTime() - startTime);
			logger.trace("Finished execution of " + getClass().getSimpleName() + "#" + hashCode() + " after " + lastExecutionDuration() + " ms");
			if(successConsumer != null){
				successConsumer.accept(out);
			}
		};

		// return buffer content if present and match
		if(input == executionBuffer.getInput() && executionBuffer.getTimestamp() != null){
			successCallback.accept(executionBuffer.getOutput());
			return;
		}

		try{
			executor.execute(() -> {
				try{
					// perform check
					if(!getCheck().getAsBoolean()){
						throw new FriendlyExecutionException("Execution aborted by pre execution check");
					}
					// execute processing
					var result = actionFunction.apply(input);
					// write to buffer and return
					((ExecutionBuffer.Internal<IN, OUT>) executionBuffer).setData(input, result);
					successCallback.accept(result);
				}
				catch(Throwable t){
					failureCallback.accept(t);
				}
			});
		}
		catch(Throwable t){
			failureCallback.accept(t);
		}
	}

	@Override
	public OUT execute(IN input){
		logger.trace("Started execution of " + getClass().getSimpleName() + "#" + hashCode());
		var startTime = System.nanoTime();
		try{
			// return buffer content if present and match
			if(input == executionBuffer.getInput() && executionBuffer.getTimestamp() != null){
				return executionBuffer.getOutput();
			}
			// perform check
			if(!getCheck().getAsBoolean()){
				throw new FriendlyExecutionException("Execution aborted by pre execution check");
			}
			// execute processing
			var result = actionFunction.apply(input);
			// write to buffer and return
			((ExecutionBuffer.Internal<IN, OUT>) executionBuffer).setData(input, result);
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
			logger.trace("Finished execution of " + getClass().getSimpleName() + "#" + hashCode() + " after " + lastExecutionDuration() + " ms");
		}
	}

	@Override
	public ExecutionBuffer<IN, OUT> resultBuffer(){
		return executionBuffer;
	}

}
