package de.hypercdn.commons.api.execution.task;

import de.hypercdn.commons.imp.execution.task.ExecutionState;

import java.time.Duration;
import java.time.Instant;

public interface Executable extends Runnable{

	/**
	 * Returns the name of the executable
	 * <p>
	 * Will use the simplified class name by default
	 *
	 * @return name
	 */
	default String getName(){
		return getClass().getSimpleName();
	}

	/**
	 * Returns the description of the executable
	 *
	 * @return description
	 */
	String getDescription();

	/**
	 * Returns the last execution state of the executable
	 *
	 * @return execution state
	 */
	ExecutionState getExecutionState();

	/**
	 * Sets the execution state
	 *
	 * @param executionState to set
	 */
	void setExecutionState(ExecutionState executionState);

	/**
	 * Returns the last timestamp of when execution of the executable started
	 *
	 * @return timestamp of execution start
	 */
	Instant getLastExecutionTime();

	/**
	 * Sets the last execution time
	 *
	 * @param time of execution start
	 */
	void setLastExecutionTime(Instant time);

	/**
	 * Returns the last timestamp of when execution of the executable finished
	 *
	 * @return timestamp of execution start
	 */
	Instant getLastExecutionEndTime();

	/**
	 * Sets the last execution end time
	 *
	 * @param time of execution end
	 */
	void setLastExecutionEndTime(Instant time);

	/**
	 * Returns the last duration between the start and end time
	 *
	 * @return duration
	 */
	default Duration getLastExecutionDuration(){
		return Duration.between(getLastExecutionTime(), getLastExecutionEndTime());
	}

}
