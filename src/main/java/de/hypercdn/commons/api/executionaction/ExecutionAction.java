package de.hypercdn.commons.api.executionaction;

import de.hypercdn.commons.imp.executionaction.*;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public interface ExecutionAction<IN, OUT> {

    ExecutionAction<IN, OUT> setCheck(BooleanSupplier check);

    BooleanSupplier getCheck();

    default ExecutionAction<IN, OUT> addCheck(BooleanSupplier check) {
        return setCheck(() -> (getCheck() == null || getCheck().getAsBoolean()) && check.getAsBoolean());
    }

    ExecutionAction<IN, OUT> setExecutor(Executor executor);

    Executor getExecutor();

    Supplier<IN> getInputSupplier();

    ExecutionAction<IN, OUT> setInputSupplier(Supplier<IN> inputSupplier);

    Function<IN, OUT> getActionFunction();

    ExecutionAction<IN, OUT> setActionFunction(Function<IN, OUT> actionFunction);


    default void queue() {
        var supplier = getInputSupplier();
        queue(supplier != null ? supplier.get() : null, null, null);
    }

    default void queue(IN input) {
        queue(input, null, null);
    }

    default void queue(Consumer<? super OUT> successConsumer) {
        var supplier = getInputSupplier();
        queue(supplier != null ? supplier.get() : null, successConsumer, null);
    }

    default void queue(IN input, Consumer<? super OUT> successConsumer) {
        queue(input, successConsumer, null);
    }

    default void queue(Consumer<? super OUT> successConsumer, Consumer<? super Throwable> exceptionConsumer) {
        var supplier = getInputSupplier();
        queue(supplier != null ? supplier.get() : null, successConsumer, exceptionConsumer);
    }

    void queue(IN input, Consumer<? super OUT> successConsumer, Consumer<? super Throwable> exceptionConsumer);

    default OUT execute() throws ExecutionException {
        var supplier = getInputSupplier();
        return execute(supplier != null ? supplier.get() : null);
    }

    OUT execute(IN input) throws ExecutionException ;

    static <U> ExecutionAction<?, List<U>> allOf(ExecutionAction<?, U> first, ExecutionAction<?, U>... others) {
        List<ExecutionAction<?, U>> list = new ArrayList<>(){{
            add(first);
        }};
        Collections.addAll(list, others);
        return allOf(list);
    }

    static <U> ExecutionAction<?, List<U>> allOf(List<ExecutionAction<?, U>> list){
        return accumulate(list, Collectors.toList());
    }

    static <E, A, O> ExecutionAction<?, O> accumulate(Collection<? extends ExecutionAction<?, ? extends E>> actions, Collector<? super E, A, ? extends O> collector) {
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

        while (iterator.hasNext()){
            ExecutionAction<?, ? extends E> next = iterator.next();
            result = result.and(next, (list, b) -> {
                add.accept(list, b);
                return list;
            });
        }

        return result.map(output);
    }

    default <MAPPED> ExecutionAction<?, MAPPED> map(Function<? super OUT, ? extends MAPPED> map) {
        return new MapExecutionAction<>(this, map);
    }

    default <OTHER, MAPPED> ExecutionAction<?, MAPPED> and(ExecutionAction<?, OTHER> other, BiFunction<? super OUT, ? super OTHER, ? extends MAPPED> accumulator) {
        return new CombinedExecutionAction<>(this, other, accumulator);
    }

    default <MAPPED> ExecutionAction<IN, MAPPED> then(ExecutionAction<OUT, MAPPED> next) {
        return new ChainedExecutionAction<>(this, next);
    }

}
