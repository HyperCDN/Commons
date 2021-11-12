package de.hypercdn.commons.imp.executionaction;

import de.hypercdn.commons.api.executionaction.ExecutionAction;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ChainedExecutionAction<IN, TRANS, OUT> implements ExecutionAction<IN, OUT> {

    private final ExecutionAction<IN, TRANS> firstExecutionAction;
    private final ExecutionAction<TRANS, OUT> secondExecutionAction;

    public ChainedExecutionAction(ExecutionAction<IN, TRANS> firstExecutionAction, ExecutionAction<TRANS, OUT> secondExecutionAction){
        this.firstExecutionAction = firstExecutionAction;
        this.secondExecutionAction = secondExecutionAction;
    }

    @Override
    public ExecutionAction<IN, OUT> setCheck(BooleanSupplier check) {
        Objects.requireNonNull(check);
        firstExecutionAction.setCheck(check);
        secondExecutionAction.setCheck(check);
        return this;
    }

    @Override
    public BooleanSupplier getCheck() {
        return () ->
                firstExecutionAction.getCheck().getAsBoolean()
                && secondExecutionAction.getCheck().getAsBoolean();
    }

    @Override
    public ExecutionAction<IN, OUT> setExecutor(Executor executor) {
        Objects.requireNonNull(executor);
        firstExecutionAction.setExecutor(executor);
        secondExecutionAction.setExecutor(executor);
        return this;
    }

    @Override
    public Executor getExecutor() {
        return firstExecutionAction.getExecutor();
    }

    @Override
    public Supplier<IN> getInputSupplier() {
        return firstExecutionAction.getInputSupplier();
    }

    @Override
    public ExecutionAction<IN, OUT> setInputSupplier(Supplier<IN> inputSupplier) {
        Objects.requireNonNull(inputSupplier);
        firstExecutionAction.setInputSupplier(inputSupplier);
        return this;
    }

    @Override
    public Function<IN, OUT> getActionFunction() {
        throw new UnsupportedOperationException("Not allowed on chained action");
    }

    @Override
    public ExecutionAction<IN, OUT> setActionFunction(Function<IN, OUT> actionFunction) {
        throw new UnsupportedOperationException("Not allowed on chained action");
    }

    @Override
    public void queue(IN input, Consumer<? super OUT> successConsumer, Consumer<? super Throwable> exceptionConsumer) {
        try {
            Consumer<Throwable> throwableConsumer = (throwable) -> {
              if(exceptionConsumer != null){
                  exceptionConsumer.accept(throwable);
              }
            };
            firstExecutionAction.queue(firstResult -> {
                secondExecutionAction.queue(firstResult, secondResult -> {
                    if(successConsumer != null){
                        successConsumer.accept(secondResult);
                    }
                }, throwableConsumer);
            }, throwableConsumer);
        }catch (Throwable t) {
            if(t instanceof Error){
                throw t;
            }
            if(exceptionConsumer != null){
                exceptionConsumer.accept(new ExecutionException(t));
            }
        }
    }

    @Override
    public OUT execute(IN input) throws ExecutionException {
        try {
            return secondExecutionAction.execute(firstExecutionAction.execute());
        }catch (Throwable t) {
            if (t instanceof Error) {
                throw t;
            }
            throw new ExecutionException(t);
        }
    }
}
