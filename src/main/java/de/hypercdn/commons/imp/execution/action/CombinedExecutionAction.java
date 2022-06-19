package de.hypercdn.commons.imp.execution.action;

import de.hypercdn.commons.api.execution.action.ExecutionAction;
import de.hypercdn.commons.imp.execution.misc.ExecutionException;
import de.hypercdn.commons.imp.execution.misc.ExecutionStack;
import de.hypercdn.commons.util.LockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;

/**
 * Creates a new instance of a combined execution action
 *
 * @param <OUT1>   type
 * @param <OUT2>   type
 * @param <MAPPED> type
 */
public class CombinedExecutionAction<OUT1, OUT2, MAPPED> implements ExecutionAction<Void, MAPPED>{

	private final ExecutionAction<?, OUT1> executionAction1;
	private final ExecutionAction<?, OUT2> executionAction2;
	private final BiFunction<? super OUT1, ? super OUT2, ? extends MAPPED> accumulator;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final ExecutionStack executionStack = new ExecutionStack();
	private volatile boolean failed = false;
	private volatile long lastExecutionDuration = -1L;

	public CombinedExecutionAction(ExecutionAction<?, OUT1> executionAction1, ExecutionAction<?, OUT2> executionAction2, BiFunction<? super OUT1, ? super OUT2, ? extends MAPPED> accumulator){
		this.executionAction1 = executionAction1;
		this.executionAction2 = executionAction2;
		this.accumulator = accumulator;
	}

	@Override
	public ExecutionAction<Void, MAPPED> setCheck(BooleanSupplier check){
		Objects.requireNonNull(check);
		executionAction1.setCheck(() -> !failed && check.getAsBoolean());
		executionAction2.setCheck(() -> !failed && check.getAsBoolean());
		return this;
	}

	@Override
	public BooleanSupplier getCheck(){
		return () ->
			(executionAction1.getCheck() == null || executionAction1.getCheck().getAsBoolean())
				&& (executionAction2.getCheck() == null || executionAction2.getCheck().getAsBoolean())
				&& !failed;
	}

	@Override
	public ExecutionAction<Void, MAPPED> setExecutor(Executor executor){
		Objects.requireNonNull(executor);
		executionAction1.setExecutor(executor);
		executionAction2.setExecutor(executor);
		return this;
	}

	@Override
	public Executor getExecutor(){
		return executionAction1.getExecutor();
	}

	@Override
	public Supplier<Void> getInputSupplier(){
		return () -> null;
	}

	@Override
	public ExecutionAction<Void, MAPPED> setInputSupplier(Supplier<Void> inputSupplier){
		throw new UnsupportedOperationException("Not allowed on combined action");
	}

	@Override
	public Function<Void, MAPPED> getActionFunction(){
		throw new UnsupportedOperationException("Not allowed on combined action");
	}

	@Override
	public ExecutionAction<Void, MAPPED> setActionFunction(Function<Void, MAPPED> actionFunction){
		throw new UnsupportedOperationException("Not allowed on combined action");
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
	public ExecutionAction<Void, MAPPED> passExecutionStack(ExecutionStack executionStack){
		this.executionStack.push(executionStack);
		return this;
	}

	@Override
	public void queue(Void input, Consumer<? super MAPPED> successConsumer, Consumer<? super Throwable> exceptionConsumer){
		logger.trace("Initializing execution of " + getClass().getSimpleName() + "#" + hashCode());
		var startTime = System.nanoTime();

		var lock = new ReentrantLock();
		var done1 = new AtomicBoolean(false);
		var done2 = new AtomicBoolean(false);
		var result1 = new AtomicReference<OUT1>();
		var result2 = new AtomicReference<OUT2>();

		Consumer<Throwable> failureCallback = throwable -> {
			if(failed){
				return;
			}
			failed = true;

			lastExecutionDuration = (System.nanoTime() - startTime);
			logger.trace("Failed execution of " + getClass().getSimpleName() + "#" + hashCode() + " after " + lastExecutionDuration() + " ms");

			if(exceptionConsumer != null){
				exceptionConsumer.accept(throwable instanceof ExecutionException ? throwable : new ExecutionException(throwable));
			}
		};

		Consumer<MAPPED> successMappedCallback = mapped -> {
			lastExecutionDuration = (System.nanoTime() - startTime);
			logger.trace("Finished execution of " + getClass().getSimpleName() + "#" + hashCode() + " after " + lastExecutionDuration() + " ms");

			if(successConsumer != null){
				successConsumer.accept(mapped);
			}
		};

		BiConsumer<OUT1, OUT2> mappingConsumer = (o1, o2) -> {
			try{
				var mapped = accumulator.apply(o1, o2);
				successMappedCallback.accept(mapped);
			}
			catch(Throwable t){
				failureCallback.accept(t);
			}
		};

		try{
			executionAction1.queue((result) -> LockUtil.executeLocked(lock, () -> {
				try{
					done1.set(true);
					result1.set(result);
					if(done2.get()){
						mappingConsumer.accept(result1.get(), result2.get());
					}
				}
				catch(Exception e){
					failureCallback.accept(e);
				}
			}), failureCallback);
			executionAction2.queue((result) -> LockUtil.executeLocked(lock, () -> {
				try{
					done2.set(true);
					result2.set(result);
					if(done1.get()){
						mappingConsumer.accept(result1.get(), result2.get());
					}
				}
				catch(Exception e){
					failureCallback.accept(e);
				}
			}), failureCallback);
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
	public MAPPED execute(Void input) throws ExecutionException{
		logger.trace("Started execution of " + getClass().getSimpleName() + "#" + hashCode());
		var startTime = System.nanoTime();
		try{
			if(!getCheck().getAsBoolean()){
				return null;
			}
			var a = executionAction1.execute();
			var b = executionAction2.execute();
			var result = accumulator.apply(a, b);
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

}
