package de.hypercdn.commons.imp.executionaction;

import de.hypercdn.commons.api.executionaction.ExecutionAction;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class GenericExecutionAction<IN, OUT> implements ExecutionAction<IN, OUT> {

    private Executor executor = ForkJoinPool.commonPool();
    private Supplier<IN> inputSupplier = () -> null;
    private Function<IN, OUT> actionFunction = (unused) -> null;
    private BooleanSupplier check = () -> true;

    public GenericExecutionAction(){}

    public GenericExecutionAction(Supplier<IN> inputSupplier, Function<IN, OUT> actionFunction){
        Objects.requireNonNull(inputSupplier);
        Objects.requireNonNull(actionFunction);
        this.inputSupplier = inputSupplier;
        this.actionFunction = actionFunction;
    }

    @Override
    public ExecutionAction<IN, OUT> setCheck(BooleanSupplier check) {
        Objects.requireNonNull(check);
        this.check = check;
        return this;
    }

    @Override
    public BooleanSupplier getCheck() {
        return check;
    }

    @Override
    public ExecutionAction<IN, OUT> setExecutor(Executor executor) {
        Objects.requireNonNull(executor);
        this.executor = executor;
        return this;
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public Supplier<IN> getInputSupplier() {
        return inputSupplier;
    }

    @Override
    public ExecutionAction<IN, OUT> setInputSupplier(Supplier<IN> inputSupplier) {
        Objects.requireNonNull(inputSupplier);
        this.inputSupplier = inputSupplier;
        return this;
    }

    @Override
    public Function<IN, OUT> getActionFunction() {
        return actionFunction;
    }

    @Override
    public ExecutionAction<IN, OUT> setActionFunction(Function<IN, OUT> actionFunction) {
        Objects.requireNonNull(actionFunction);
        this.actionFunction = actionFunction;
        return this;
    }

    @Override
    public void queue(IN input, Consumer<? super OUT> successConsumer, Consumer<? super Throwable> exceptionConsumer) {
        try {
            executor.execute(() -> {
                try {
                    if(!getCheck().getAsBoolean()){
                        if(successConsumer != null){
                            successConsumer.accept(null);
                        }
                        return;
                    }
                    var result = actionFunction.apply(input);
                    if(successConsumer != null){
                        successConsumer.accept(result);
                    }
                }catch (Throwable t){
                    if(t instanceof Error){
                        throw t;
                    }
                    if(exceptionConsumer != null){
                        exceptionConsumer.accept(new ExecutionException(t));
                    }
                }
            });
        }catch (Throwable t){
            if(t instanceof Error){
                throw t;
            }
            if(exceptionConsumer != null){
                exceptionConsumer.accept(new ExecutionException(t));
            }
        }
    }

    @Override
    public OUT execute(IN input) {
        try {
            return actionFunction.apply(input);
        }catch (Throwable t){
            if(t instanceof Error){
                throw t;
            }
            throw new ExecutionException(t);
        }
    }
}
