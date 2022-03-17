package de.hypercdn.commons.imp.execution.task;

/**
 * Represents the state of an executable
 */
public record ExecutionState(Reference reference, Object payload){

	/**
	 * Reference values for identification purposes
	 */
	public enum Reference{
		COMPLETED,
		FAILED,
		STARTED,
		SKIPPED,
		UNKNOWN
	}

	public static final ExecutionState COMPLETED = new ExecutionState(ExecutionState.Reference.COMPLETED, "Execution Completed");
	public static final ExecutionState FAILED = new ExecutionState(ExecutionState.Reference.FAILED, "Execution Failed");
	public static final ExecutionState STARTED = new ExecutionState(ExecutionState.Reference.STARTED, "Executing");
	public static final ExecutionState SKIPPED = new ExecutionState(ExecutionState.Reference.SKIPPED, "Execution Skipped");
	public static final ExecutionState UNKNOWN = new ExecutionState(ExecutionState.Reference.UNKNOWN, "Unknown State");

}
