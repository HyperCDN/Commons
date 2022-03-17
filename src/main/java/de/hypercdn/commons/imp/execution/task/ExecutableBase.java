package de.hypercdn.commons.imp.execution.task;

import de.hypercdn.commons.api.execution.task.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public abstract class ExecutableBase implements Executable{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	private ExecutionState executionState = new ExecutionState(ExecutionState.Reference.UNKNOWN, "Unknown State");
	private Instant lastExecutionTime = Instant.MIN;
	private Instant lastExecutionEndTime = Instant.MIN;

	@Override
	public abstract String getDescription();

	@Override
	public ExecutionState getState(){
		return executionState;
	}

	@Override
	public void setState(ExecutionState executionState){
		this.executionState = executionState;
		switch(executionState.reference()){
			case STARTED -> setLastExecutionTime(Instant.now());
			case FAILED, COMPLETED -> setLastExecutionEndTime(Instant.now());
		}
	}

	@Override
	public Instant getLastExecutionTime(){
		return lastExecutionTime;
	}

	@Override
	public void setLastExecutionTime(Instant lastExecutionTime){
		this.lastExecutionTime = lastExecutionTime;
	}

	@Override
	public Instant getLastExecutionEndTime(){
		return lastExecutionEndTime;
	}

	@Override
	public void setLastExecutionEndTime(Instant time){
		this.lastExecutionEndTime = time;
	}

}
