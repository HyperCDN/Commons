package de.hypercdn.commons.imp.execution.action.internal;

import de.hypercdn.commons.api.execution.action.ExecutionAction;
import de.hypercdn.commons.api.execution.action.ExecutionBuffer;
import de.hypercdn.commons.imp.execution.action.GenericExecutionBuffer;
import de.hypercdn.commons.imp.execution.misc.exception.ExecutionException;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Implementation of a mapped execution action
 *
 * @param <IN>     type
 * @param <OUT>    type
 * @param <MAPPED> type
 */
public class MapExecutionAction<IN, OUT, MAPPED> implements ExecutionAction.Internal<IN, MAPPED>{

	private final ExecutionAction<IN, OUT> originalAction;
	private final Function<? super OUT, ? extends MAPPED> mapping;

	// internal
	private final ExecutionBuffer<IN, MAPPED> executionBuffer = new GenericExecutionBuffer<>();

	public MapExecutionAction(ExecutionAction<IN, OUT> originalAction, Function<? super OUT, ? extends MAPPED> mapping){
		Objects.requireNonNull(originalAction);
		Objects.requireNonNull(mapping);
		this.originalAction = originalAction;
		this.mapping = mapping;
	}

	@Override
	public ExecutionAction<IN, MAPPED> setCheck(BooleanSupplier check){
		Objects.requireNonNull(check);
		originalAction.setCheck(check);
		return this;
	}

	@Override
	public BooleanSupplier getCheck(){
		return originalAction.getCheck();
	}

	@Override
	public ExecutionAction<IN, MAPPED> setExecutor(Executor executor){
		Objects.requireNonNull(executor);
		originalAction.setExecutor(executor);
		return this;
	}

	@Override
	public Executor getExecutor(){
		return originalAction.getExecutor();
	}

	@Override
	public Supplier<IN> getInputSupplier(){
		return originalAction.getInputSupplier();
	}

	@Override
	public ExecutionAction<IN, MAPPED> setInputSupplier(Supplier<IN> inputSupplier){
		Objects.requireNonNull(inputSupplier);
		originalAction.setInputSupplier(inputSupplier);
		return this;
	}

	@Override
	public Function<IN, MAPPED> getActionFunction(){
		throw new UnsupportedOperationException("Not allowed on mapped action");
	}

	@Override
	public ExecutionAction<IN, MAPPED> setActionFunction(Function<IN, MAPPED> actionFunction){
		throw new UnsupportedOperationException("Not allowed on mapped action");
	}

	@Override
	public void queue(IN input, Consumer<? super MAPPED> successConsumer, Consumer<? super Throwable> exceptionConsumer){

		Consumer<MAPPED> successCallback = out -> {
			if(successConsumer != null){
				successConsumer.accept(out);
			}
		};

		// return buffer content if present and match
		if(input == executionBuffer.getInput() && executionBuffer.getTimestamp() != null){
			successCallback.accept(executionBuffer.getOutput());
			return;
		}

		originalAction.queue(result -> {
			var applied = mapping.apply(result);
			// write to buffer and return
			((ExecutionBuffer.Internal<IN, MAPPED>) executionBuffer).setData(input, applied);
			successCallback.accept(applied);
		});
	}

	@Override
	public MAPPED execute(IN input) throws ExecutionException{
		try{
			// return buffer content if present and match
			if(input == executionBuffer.getInput() && executionBuffer.getTimestamp() != null){
				return executionBuffer.getOutput();
			}
			// execute
			var first = originalAction.execute();
			var result = mapping.apply(first);
			// write to buffer and return
			((ExecutionBuffer.Internal<IN, MAPPED>) executionBuffer).setData(input, result);
			return result;
		}
		catch(Throwable t){
			if(t instanceof Error || t instanceof ExecutionException){
				throw t;
			}
			throw new ExecutionException(t);
		}
	}

	@Override
	public ExecutionBuffer<IN, MAPPED> resultBuffer(){
		return executionBuffer;
	}

}
