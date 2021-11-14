package de.hypercdn.commons.api.executionaction;

import de.hypercdn.commons.api.properties.logging.Loggable;
import de.hypercdn.commons.imp.executionaction.*;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * The interface Execution action.
 *
 * @param <IN>  the type parameter
 * @param <OUT> the type parameter
 */
public interface ExecutionAction<IN, OUT> extends Loggable{

	Consumer<Throwable> DEFAULT_THROWABLE_CONSUMER = Throwable::printStackTrace;

	/**
	 * All of execution action.
	 *
	 * @param <U>    the type parameter
	 * @param first  the first
	 * @param others the others
	 *
	 * @return the execution action
	 */
	static <U> ExecutionAction<?, List<U>> allOf(ExecutionAction<?, U> first, ExecutionAction<?, U>... others){
		List<ExecutionAction<?, U>> list = new ArrayList<>(){{
			add(first);
		}};
		Collections.addAll(list, others);
		return allOf(list);
	}

	/**
	 * All of execution action.
	 *
	 * @param <U>  the type parameter
	 * @param list the list
	 *
	 * @return the execution action
	 */
	static <U> ExecutionAction<?, List<U>> allOf(List<ExecutionAction<?, U>> list){
		return accumulate(list, Collectors.toList());
	}

	/**
	 * Accumulate execution action.
	 *
	 * @param <E>       the type parameter
	 * @param <A>       the type parameter
	 * @param <O>       the type parameter
	 * @param actions   the actions
	 * @param collector the collector
	 *
	 * @return the execution action
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
	 * Gets check.
	 *
	 * @return the check
	 */
	BooleanSupplier getCheck();

	/**
	 * Sets check.
	 *
	 * @param check the check
	 *
	 * @return the check
	 */
	ExecutionAction<IN, OUT> setCheck(BooleanSupplier check);

	/**
	 * Add check execution action.
	 *
	 * @param check the check
	 *
	 * @return the execution action
	 */
	default ExecutionAction<IN, OUT> addCheck(BooleanSupplier check){
		return setCheck(() -> (getCheck() == null || getCheck().getAsBoolean()) && check.getAsBoolean());
	}

	/**
	 * Gets executor.
	 *
	 * @return the executor
	 */
	Executor getExecutor();

	/**
	 * Sets executor.
	 *
	 * @param executor the executor
	 *
	 * @return the executor
	 */
	ExecutionAction<IN, OUT> setExecutor(Executor executor);

	/**
	 * Gets input supplier.
	 *
	 * @return the input supplier
	 */
	Supplier<IN> getInputSupplier();

	/**
	 * Sets input supplier.
	 *
	 * @param inputSupplier the input supplier
	 *
	 * @return the input supplier
	 */
	ExecutionAction<IN, OUT> setInputSupplier(Supplier<IN> inputSupplier);

	/**
	 * Gets action function.
	 *
	 * @return the action function
	 */
	Function<IN, OUT> getActionFunction();

	/**
	 * Sets action function.
	 *
	 * @param actionFunction the action function
	 *
	 * @return the action function
	 */
	ExecutionAction<IN, OUT> setActionFunction(Function<IN, OUT> actionFunction);

	/**
	 * Last execution duration float.
	 *
	 * @return the float
	 */
	default float lastExecutionDuration(){
		return -1F;
	}

	/**
	 * Gets execution stack.
	 *
	 * @return the execution stack
	 */
	ExecutionStack getExecutionStack();

	/**
	 * Pass execution stack execution action.
	 *
	 * @param executionStack the execution stack
	 *
	 * @return the execution action
	 */
	ExecutionAction<IN, OUT> passExecutionStack(ExecutionStack executionStack);

	/**
	 * Queue.
	 */
	default void queue(){
		var supplier = getInputSupplier();
		queue(supplier != null ? supplier.get() : null, null, DEFAULT_THROWABLE_CONSUMER);
	}

	/**
	 * Queue.
	 *
	 * @param input the input
	 */
	default void queue(IN input){
		queue(input, null, DEFAULT_THROWABLE_CONSUMER);
	}

	/**
	 * Queue.
	 *
	 * @param successConsumer the success consumer
	 */
	default void queue(Consumer<? super OUT> successConsumer){
		var supplier = getInputSupplier();
		queue(supplier != null ? supplier.get() : null, successConsumer, DEFAULT_THROWABLE_CONSUMER);
	}

	/**
	 * Queue.
	 *
	 * @param input           the input
	 * @param successConsumer the success consumer
	 */
	default void queue(IN input, Consumer<? super OUT> successConsumer){
		queue(input, successConsumer, DEFAULT_THROWABLE_CONSUMER);
	}

	/**
	 * Queue.
	 *
	 * @param successConsumer   the success consumer
	 * @param exceptionConsumer the exception consumer
	 */
	default void queue(Consumer<? super OUT> successConsumer, Consumer<? super Throwable> exceptionConsumer){
		var supplier = getInputSupplier();
		queue(supplier != null ? supplier.get() : null, successConsumer, exceptionConsumer);
	}

	/**
	 * Queue.
	 *
	 * @param input             the input
	 * @param successConsumer   the success consumer
	 * @param exceptionConsumer the exception consumer
	 */
	void queue(IN input, Consumer<? super OUT> successConsumer, Consumer<? super Throwable> exceptionConsumer);

	/**
	 * Execute out.
	 *
	 * @return the out
	 *
	 * @throws ExecutionException the execution exception
	 */
	default OUT execute() throws ExecutionException{
		var supplier = getInputSupplier();
		return execute(supplier != null ? supplier.get() : null);
	}

	/**
	 * Execute out.
	 *
	 * @param input the input
	 *
	 * @return the out
	 *
	 * @throws ExecutionException the execution exception
	 */
	OUT execute(IN input) throws ExecutionException;

	/**
	 * Map execution action.
	 *
	 * @param <MAPPED> the type parameter
	 * @param map      the map
	 *
	 * @return the execution action
	 */
	default <MAPPED> ExecutionAction<?, MAPPED> map(Function<? super OUT, ? extends MAPPED> map){
		return new MapExecutionAction<>(this, map);
	}

	/**
	 * And execution action.
	 *
	 * @param <OTHER>     the type parameter
	 * @param <MAPPED>    the type parameter
	 * @param other       the other
	 * @param accumulator the accumulator
	 *
	 * @return the execution action
	 */
	default <OTHER, MAPPED> ExecutionAction<?, MAPPED> and(ExecutionAction<?, OTHER> other, BiFunction<? super OUT, ? super OTHER, ? extends MAPPED> accumulator){
		return new CombinedExecutionAction<>(this, other, accumulator);
	}

	/**
	 * Then execution action.
	 *
	 * @param <MAPPED> the type parameter
	 * @param next     the next
	 *
	 * @return the execution action
	 */
	default <MAPPED> ExecutionAction<IN, MAPPED> then(ExecutionAction<OUT, MAPPED> next){
		return new ChainedExecutionAction<>(this, next);
	}

}
