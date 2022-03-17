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

}
