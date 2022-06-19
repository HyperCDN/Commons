package de.hypercdn.commons.imp.execution.action;

import de.hypercdn.commons.api.execution.action.ExecutionAction;
import de.hypercdn.commons.imp.execution.misc.ExecutionException;
import de.hypercdn.commons.imp.execution.misc.ExecutionStack;
import de.hypercdn.commons.util.StackTraceUtil;
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
public class GenericExecutionAction<IN, OUT> implements ExecutionAction<IN, OUT>{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final ExecutionStack executionStack = new ExecutionStack();
	private Executor executor = DEFAULT_EXECUTOR;
	private Supplier<IN> inputSupplier = () -> null;
	private Function<IN, OUT> actionFunction = (unused) -> null;
	private BooleanSupplier check = () -> true;
	private volatile long lastExecutionDuration = -1L;

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
	public ExecutionStack getExecutionStack(){
		return executionStack;
	}

	@Override
	public ExecutionAction<IN, OUT> passExecutionStack(ExecutionStack executionStack){
		this.executionStack.push(executionStack);
		return this;
	}

	@Override
	public void queue(IN input, Consumer<? super OUT> successConsumer, Consumer<? super Throwable> exceptionConsumer){
		logger.trace("Initializing execution of " + getClass().getSimpleName() + "#" + hashCode());
		var startTime = System.nanoTime();

		try{
			executionStack.setCurrentStack(StackTraceUtil.currentStacktrace());
			executionStack.push(executionStack);
			executor.execute(() -> {
				executionStack.setCurrentStack(StackTraceUtil.currentStacktrace());
				try{
					var result = execute(input);
					lastExecutionDuration = (System.nanoTime() - startTime); // override the processing time set by the execute() call as this is does not include the waiting time
					if(successConsumer != null){
						successConsumer.accept(result);
					}
				}
				catch(Throwable t){
					lastExecutionDuration = (System.nanoTime() - startTime); // override the processing time set by the execute() call as this is does not include the waiting time
					if(t instanceof Error){
						throw t;
					}
					if(exceptionConsumer != null){
						exceptionConsumer.accept(t);
					}
				}
			});
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
	public OUT execute(IN input){
		logger.trace("Started execution of " + getClass().getSimpleName() + "#" + hashCode());
		var startTime = System.nanoTime();
		try{
			if(!getCheck().getAsBoolean()){
				return null;
			}
			var result = actionFunction.apply(input);
			return result;
		}
		catch(Throwable t){
			t.setStackTrace(executionStack.getFullContextStack(t.getStackTrace()));
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

}
