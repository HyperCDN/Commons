package de.hypercdn.commons.imp.executionaction;

import de.hypercdn.commons.api.executionaction.ExecutionAction;
import de.hypercdn.commons.imp.lock.LockUtil;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;

public class CombinedExecutionAction<OUT1, OUT2, MAPPED> implements ExecutionAction<Void, MAPPED> {

    private final ExecutionAction<?, OUT1> executionAction1;
    private final ExecutionAction<?, OUT2> executionAction2;
    private final BiFunction<? super OUT1, ? super  OUT2, ? extends MAPPED> accumulator;
    private volatile boolean failed = false;

    public CombinedExecutionAction(ExecutionAction<?, OUT1> executionAction1, ExecutionAction<?, OUT2> executionAction2, BiFunction<? super OUT1, ? super  OUT2, ? extends MAPPED> accumulator) {
        this.executionAction1 = executionAction1;
        this.executionAction2 = executionAction2;
        this.accumulator = accumulator;
    }

    @Override
    public ExecutionAction<Void, MAPPED> setCheck(BooleanSupplier check) {
        Objects.requireNonNull(check);
        executionAction1.setCheck(() -> !failed && check.getAsBoolean());
        executionAction2.setCheck(() -> !failed && check.getAsBoolean());
        return this;
    }

    @Override
    public BooleanSupplier getCheck() {
        return () ->
                (executionAction1.getCheck() == null || executionAction1.getCheck().getAsBoolean())
            &&  (executionAction2.getCheck() == null || executionAction2.getCheck().getAsBoolean())
            &&  !failed;
    }

    @Override
    public ExecutionAction<Void, MAPPED> setExecutor(Executor executor) {
        Objects.requireNonNull(executor);
        executionAction1.setExecutor(executor);
        executionAction2.setExecutor(executor);
        return this;
    }

    @Override
    public Executor getExecutor() {
        return executionAction1.getExecutor();
    }

    @Override
    public Supplier<Void> getInputSupplier() {
        return () -> null;
    }

    @Override
    public ExecutionAction<Void, MAPPED> setInputSupplier(Supplier<Void> inputSupplier) {
        throw new UnsupportedOperationException("Not allowed on combined action");
    }

    @Override
    public Function<Void, MAPPED> getActionFunction() {
        throw new UnsupportedOperationException("Not allowed on combined action");
    }

    @Override
    public ExecutionAction<Void, MAPPED> setActionFunction(Function<Void, MAPPED> actionFunction) {
        throw new UnsupportedOperationException("Not allowed on combined action");
    }

    @Override
    public void queue(Void input, Consumer<? super MAPPED> successConsumer, Consumer<? super Throwable> exceptionConsumer) {
        ReentrantLock lock = new ReentrantLock();
        AtomicBoolean done1 = new AtomicBoolean(false);
        AtomicBoolean done2 = new AtomicBoolean(false);
        AtomicReference<OUT1> result1 = new AtomicReference<>();
        AtomicReference<OUT2> result2 = new AtomicReference<>();

        Consumer<Throwable> failureCallback = (throwable) -> {
             if(failed) return;
             failed = true;
             if(exceptionConsumer != null){
                 exceptionConsumer.accept(throwable);
             }
        };

        executionAction1.queue((result) -> LockUtil.executeLocked(lock, () -> {
            try {
                done1.set(true);
                result1.set(result);
                if(done2.get()){
                    if(successConsumer != null)
                        successConsumer.accept(accumulator.apply(result1.get(), result2.get()));
                }
            }catch(Exception e){
                failureCallback.accept(e);
            }
        }), failureCallback);
        executionAction2.queue((result) -> LockUtil.executeLocked(lock, () -> {
            try {
                done2.set(true);
                result2.set(result);
                if(done1.get()){
                    if(successConsumer != null)
                        successConsumer.accept(accumulator.apply(result1.get(), result2.get()));
                }
            }catch(Exception e){
                failureCallback.accept(e);
            }
        }), failureCallback);
    }

    @Override
    public MAPPED execute(Void input) throws ExecutionException {
        try {
            var a = executionAction1.execute();
            var b = executionAction2.execute();
            return accumulator.apply(a, b);
        }catch (Throwable t){
            if (t instanceof Error){
                throw t;
            }
            throw new ExecutionException(t);
        }
    }
}
