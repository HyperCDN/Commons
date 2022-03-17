package de.hypercdn.commons.imp.execution.task;

import de.hypercdn.commons.api.execution.action.ExecutionAction;
import de.hypercdn.commons.api.execution.task.ExecutionStage;
import de.hypercdn.commons.imp.execution.action.GenericExecutionAction;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class GenericExecutionStage<IN, OUT> extends ExecutableBase implements ExecutionStage<IN, OUT>{

	@Override
	public abstract Function<IN, OUT> getActionFunction();

	@Override
	public ExecutionAction<IN, OUT> asExecutionActionWith(Supplier<IN> inputSupplier){
		return new GenericExecutionAction<>(inputSupplier, input -> {
			try{
				setState(ExecutionState.STARTED);
				logger.trace(this.toString());
				var result = getActionFunction().apply(input);
				setState(new ExecutionState(ExecutionState.Reference.COMPLETED, result));
				return result;
			}
			catch(Exception e){
				setState(new ExecutionState(ExecutionState.Reference.FAILED, e));
				throw e;
			}
			finally{
				logger.trace(this.toString());
			}
		});
	}

	@Override
	public String toString(){
		return String.format("Stage \"%s\" | %s | at %s for %s | %s",
			getName(),
			getState().reference(),
			getLastExecutionTime(),
			getLastExecutionDuration(),
			getDescription()
		);
	}

}
