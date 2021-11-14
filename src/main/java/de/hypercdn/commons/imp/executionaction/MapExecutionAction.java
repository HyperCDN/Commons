package de.hypercdn.commons.imp.executionaction;

import de.hypercdn.commons.api.executionaction.ExecutionAction;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MapExecutionAction<IN, OUT, MAPPED> implements ExecutionAction<IN, MAPPED> {

    private final ExecutionAction<IN, OUT> originalAction;
    private final Function<? super OUT, ? extends MAPPED> mapping;

    public MapExecutionAction(ExecutionAction<IN, OUT> originalAction, Function<? super OUT, ? extends MAPPED> mapping){
        Objects.requireNonNull(originalAction);
        Objects.requireNonNull(mapping);
        this.originalAction = originalAction;
        this.mapping = mapping;
    }

    @Override
    public ExecutionAction<IN, MAPPED> setCheck(BooleanSupplier check) {
        Objects.requireNonNull(check);
        originalAction.setCheck(check);
        return this;
    }

    @Override
    public BooleanSupplier getCheck() {
        return originalAction.getCheck();
    }

    @Override
    public ExecutionAction<IN, MAPPED> setExecutor(Executor executor) {
        Objects.requireNonNull(executor);
        originalAction.setExecutor(executor);
        return this;
    }

    @Override
    public Executor getExecutor() {
        return originalAction.getExecutor();
    }

    @Override
    public Supplier<IN> getInputSupplier() {
        return originalAction.getInputSupplier();
    }

    @Override
    public ExecutionAction<IN, MAPPED> setInputSupplier(Supplier<IN> inputSupplier) {
        Objects.requireNonNull(inputSupplier);
        originalAction.setInputSupplier(inputSupplier);
        return this;
    }

    @Override
    public Function<IN, MAPPED> getActionFunction() {
        throw new UnsupportedOperationException("Not allowed on mapped action");
    }

    @Override
    public ExecutionAction<IN, MAPPED> setActionFunction(Function<IN, MAPPED> actionFunction) {
        throw new UnsupportedOperationException("Not allowed on mapped action");
    }

    @Override
    public ExecutionStack getExecutionStack() {
        return originalAction.getExecutionStack();
    }

    @Override
    public ExecutionAction<IN, MAPPED> passExecutionStack(ExecutionStack executionStack) {
        originalAction.passExecutionStack(executionStack);
        return this;
    }

    @Override
    public void queue(IN input, Consumer<? super MAPPED> successConsumer, Consumer<? super Throwable> exceptionConsumer) {
        originalAction.queue(result -> {
            var applied = mapping.apply(result);
            if(successConsumer != null){
                successConsumer.accept(applied);
            }
        });
    }

    @Override
    public MAPPED execute(IN input) throws ExecutionException {
        try {
            return mapping.apply(originalAction.execute());
        }catch (Throwable t){
            if(t instanceof Error){
                throw t;
            }
            throw new ExecutionException(t);
        }
    }
}
