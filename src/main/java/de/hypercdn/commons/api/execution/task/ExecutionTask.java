package de.hypercdn.commons.api.execution.task;

import de.hypercdn.commons.imp.execution.action.RunnableExecutionAction;

import java.util.List;

public interface ExecutionTask extends Executable{

	/**
	 * Returns the stages within this task in their respective order
	 *
	 * @return execution stages
	 */
	List<ExecutionStage<Object, Object>> getStages();

	/**
	 * Returns a runnable execution action representing this execution task
	 *
	 * @return runnable execution action
	 */
	RunnableExecutionAction asExecutionAction();

}
