package de.hypercdn.commons.api.execution.task;

import de.hypercdn.commons.api.execution.action.ExecutionAction;

import java.util.function.Function;
import java.util.function.Supplier;

public interface ExecutionStage<IN, OUT> extends Executable{

	/**
	 * Provides the processing function of this execution stage
	 *
	 * @return processing function
	 */
	Function<IN, OUT> getActionFunction();

	/**
	 * Returns an execution action with a null input
	 *
	 * @return execution action
	 */
	default ExecutionAction<IN, OUT> asExecutionAction(){
		return asExecutionActionWith(() -> null);
	}

	/**
	 * Returns an execution action representing this execution stage
	 *
	 * @param inputSupplier to supply inputs
	 *
	 * @return execution action
	 */
	ExecutionAction<IN, OUT> asExecutionActionWith(Supplier<IN> inputSupplier);

}
