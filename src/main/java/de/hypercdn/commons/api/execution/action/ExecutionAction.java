package de.hypercdn.commons.api.execution.action;

import de.hypercdn.commons.imp.execution.action.ChainedExecutionAction;
import de.hypercdn.commons.imp.execution.action.CombinedExecutionAction;
import de.hypercdn.commons.imp.execution.action.GenericExecutionAction;
import de.hypercdn.commons.imp.execution.action.MapExecutionAction;
import de.hypercdn.commons.imp.execution.misc.exception.ExecutionException;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Represents an action or task which can be processed
 *
 * @param <IN>  data type
 * @param <OUT> data type
 */
public interface ExecutionAction<IN, OUT>{

	/**
	 * Used as default throwable consumer which gets called on exceptions when executed async
	 */
	Consumer<Throwable> DEFAULT_THROWABLE_CONSUMER = Throwable::printStackTrace;

	/**
	 * Executor used by default for async execution
	 */
	Executor DEFAULT_EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	/**
	 * Returns an execution action which combines multiple execution actions
	 *
	 * @param first  action
	 * @param others actions
	 * @param <U>    return type
	 *
	 * @return mapped execution action
	 */
	static <U> ExecutionAction<?, List<U>> allOf(ExecutionAction<?, U> first, ExecutionAction<?, U>... others){
		List<ExecutionAction<?, U>> list = new ArrayList<>(){{
			add(first);
		}};
		Collections.addAll(list, others);
		return allOf(list);
	}

	/**
	 * Returns an execution action which combines multiple execution actions
	 *
	 * @param list of actions
	 * @param <U>  return type
	 *
	 * @return mapped execution action
	 */
	static <U> ExecutionAction<?, List<U>> allOf(List<ExecutionAction<?, U>> list){
		return accumulate(list, Collectors.toList());
	}

	/**
	 * Accumulates multiple actions to a single one using the provided collector
	 *
	 * @param actions
	 * @param collector
	 * @param <E>       input output type
	 * @param <A>       accumulator type
	 * @param <O>       output type
	 *
	 * @return
	 */
	static <E, A, O> ExecutionAction<?, O> accumulate(Collection<? extends ExecutionAction<?, ? extends E>> actions, Collector<? super E, A, ? extends O> collector){
		Supplier<A> accumulator = collector.supplier();
		BiConsumer<A, ? super E> add = collector.accumulator();
		Function<A, ? extends O> output = collector.finisher();

		var actionLHS = new LinkedHashSet<>(actions);
		Iterator<? extends ExecutionAction<?, ? extends E>> iterator = actionLHS.iterator();
		ExecutionAction<?, A> result = iterator.next().map(it -> {
			A list = accumulator.get();
			add.accept(list, it);
			return list;
		});

		while(iterator.hasNext()){
			ExecutionAction<?, ? extends E> next = iterator.next();
			result = result.and(next, (list, b) -> {
				add.accept(list, b);
				return list;
			});
		}

		return result.map(output);
	}

	/**
	 * Returns a new execution action of the provided function
	 *
	 * @param function to execute
	 * @param <IN>     type
	 * @param <OUT>    type
	 *
	 * @return execution action
	 */
	static <IN, OUT> ExecutionAction<IN, OUT> of(Function<IN, OUT> function){
		return new GenericExecutionAction<>(() -> null, function);
	}

	/**
	 * Returns a new execution action of the provided supplier
	 *
	 * @param supplier to execute
	 * @param <OUT>    type
	 *
	 * @return execution action
	 */
	static <OUT> ExecutionAction<Void, OUT> of(Supplier<OUT> supplier){
		return of(unused -> {
			return supplier.get();
		});
	}

	/**
	 * Returns a new execution action of the provided consumer
	 *
	 * @param consumer to execute
	 * @param <IN>     type
	 *
	 * @return execution action
	 */
	static <IN> ExecutionAction<IN, Void> of(Consumer<IN> consumer){
		return of(in -> {
			consumer.accept(in);
			return null;
		});
	}

	/**
	 * Returns a new execution action of the provided runnable
	 *
	 * @param runnable to execute
	 *
	 * @return execution action
	 */
	static ExecutionAction<Void, Void> of(Runnable runnable){
		return of(unused -> {
			runnable.run();
		});
	}

	/**
	 * Returns the check which determines if an action actually gets execute
	 *
	 * @return check
	 */
	BooleanSupplier getCheck();

	/**
	 * Sets the check which determines if an action actually gets execute
	 *
	 * @param check to set
	 *
	 * @return current instance
	 */
	ExecutionAction<IN, OUT> setCheck(BooleanSupplier check);

	/**
	 * Appends the existing check by the specified one
	 *
	 * @param check to append
	 *
	 * @return current instance
	 */
	default ExecutionAction<IN, OUT> addCheck(BooleanSupplier check){
		return setCheck(() -> (getCheck() == null || getCheck().getAsBoolean()) && check.getAsBoolean());
	}

	/**
	 * Returns the executor this action would use if executed asynchronous
	 *
	 * @return executor
	 */
	Executor getExecutor();

	/**
	 * Sets the executor which should be used if this action would be executed asynchronous
	 *
	 * @param executor for async execution
	 *
	 * @return current instance
	 */
	ExecutionAction<IN, OUT> setExecutor(Executor executor);

	/**
	 * Returns the supplier for the input
	 *
	 * @return input supplier
	 */
	Supplier<IN> getInputSupplier();

	/**
	 * Sets the supplier for the action input
	 *
	 * @param inputSupplier to supply an input when the execution starts
	 *
	 * @return current instance
	 */
	ExecutionAction<IN, OUT> setInputSupplier(Supplier<IN> inputSupplier);

	/**
	 * Returns the underlying action as function
	 *
	 * @return action fuction
	 */
	Function<IN, OUT> getActionFunction();

	/**
	 * Sets the underlying action function of this action
	 *
	 * @param actionFunction to use
	 *
	 * @return current instance
	 */
	ExecutionAction<IN, OUT> setActionFunction(Function<IN, OUT> actionFunction);

	/**
	 * Returns the duration of the last execution of this action
	 *
	 * @return execution duration in ms
	 */
	default float lastExecutionDuration(){
		return -1F;
	}

	/**
	 * Queues this action to be executed asynchronous
	 * This will use the default throwable consumer and will supply null as input if no input supplier has been defined
	 */
	default void queue(){
		var supplier = getInputSupplier();
		queue(supplier != null ? supplier.get() : null, null, DEFAULT_THROWABLE_CONSUMER);
	}

	/**
	 * Queues this action to be executed asynchronous
	 * This will use the default throwable consumer
	 *
	 * @param input which should be supplied
	 */
	default void queue(IN input){
		queue(input, null, DEFAULT_THROWABLE_CONSUMER);
	}

	/**
	 * Queues this action to be executed asynchronous
	 * This will use the default throwable consumer and will supply null as input if no input supplier has been defined
	 *
	 * @param successConsumer executed on success with the result
	 */
	default void queue(Consumer<? super OUT> successConsumer){
		var supplier = getInputSupplier();
		queue(supplier != null ? supplier.get() : null, successConsumer, DEFAULT_THROWABLE_CONSUMER);
	}

	/**
	 * Queues this action to be executed asynchronous
	 * This will use the default throwable consumer
	 *
	 * @param input           which should be supplied
	 * @param successConsumer executed on success with the result
	 */
	default void queue(IN input, Consumer<? super OUT> successConsumer){
		queue(input, successConsumer, DEFAULT_THROWABLE_CONSUMER);
	}

	/**
	 * Queues this action to be executed asynchronous
	 * This will supply null as input if no input supplier has been defined
	 *
	 * @param successConsumer   executed on success with the result
	 * @param exceptionConsumer executed on failure with the exception
	 */
	default void queue(Consumer<? super OUT> successConsumer, Consumer<? super Throwable> exceptionConsumer){
		var supplier = getInputSupplier();
		queue(supplier != null ? supplier.get() : null, successConsumer, exceptionConsumer);
	}

	/**
	 * Queues this action to be executed asynchronous
	 *
	 * @param input             which should be supplied
	 * @param successConsumer   executed on success with the result
	 * @param exceptionConsumer executed on failure with the exception
	 */
	void queue(IN input, Consumer<? super OUT> successConsumer, Consumer<? super Throwable> exceptionConsumer);

	/**
	 * Executes this action synchronous
	 * This will supply null as input if no input supplier has been defined
	 *
	 * @return result
	 *
	 * @throws ExecutionException on exception
	 */
	default OUT execute() throws ExecutionException{
		var supplier = getInputSupplier();
		return execute(supplier != null ? supplier.get() : null);
	}

	/**
	 * Executes this action synchronous
	 *
	 * @param input to be supplied
	 *
	 * @return result
	 *
	 * @throws ExecutionException on exception
	 */
	OUT execute(IN input) throws ExecutionException;

	/**
	 * Will map the result of the current action through the provided function
	 *
	 * @param map      function
	 * @param <MAPPED> return type
	 *
	 * @return mapped execution action
	 */
	default <MAPPED> ExecutionAction<?, MAPPED> map(Function<? super OUT, ? extends MAPPED> map){
		return new MapExecutionAction<>(this, map);
	}

	/**
	 * Will execute the current and another action and combines the results
	 *
	 * @param other       action
	 * @param accumulator
	 * @param <OTHER>     return type
	 * @param <MAPPED>    return type
	 *
	 * @return combined execution action
	 */
	default <OTHER, MAPPED> ExecutionAction<?, MAPPED> and(ExecutionAction<?, OTHER> other, BiFunction<? super OUT, ? super OTHER, ? extends MAPPED> accumulator){
		return new CombinedExecutionAction<>(this, other, accumulator);
	}

	/**
	 * Will execute the specified action after this one
	 *
	 * @param next     action
	 * @param <MAPPED> return type
	 *
	 * @return chained execution action
	 */
	default <MAPPED> ExecutionAction<IN, MAPPED> then(ExecutionAction<OUT, MAPPED> next){
		return new ChainedExecutionAction<>(this, next);
	}

	/**
	 * Will execute the specified function after this one
	 *
	 * @param next     action
	 * @param <MAPPED> type
	 *
	 * @return chained execution action
	 */
	default <MAPPED> ExecutionAction<IN, MAPPED> then(Function<OUT, MAPPED> next){
		return then(new GenericExecutionAction<>(() -> null, next));
	}

	/**
	 * Will execute the specified supplier after this one
	 *
	 * @param next     action
	 * @param <MAPPED> type
	 *
	 * @return chained execution action
	 */
	default <MAPPED> ExecutionAction<IN, MAPPED> then(Supplier<MAPPED> next){
		return then(in -> {
			return next.get();
		});
	}

	/**
	 * Will execute the specified consumer after this one
	 *
	 * @param next action
	 *
	 * @return chained execution action
	 */
	default ExecutionAction<IN, Void> then(Consumer<OUT> next){
		return then(in -> {
			next.accept(in);
			return null;
		});
	}

	/**
	 * Will execute the specified runnable after this one
	 *
	 * @param next action
	 *
	 * @return chained execution action
	 */
	default ExecutionAction<IN, Void> then(Runnable next){
		return then(in -> {
			next.run();
			return null;
		});
	}

}
